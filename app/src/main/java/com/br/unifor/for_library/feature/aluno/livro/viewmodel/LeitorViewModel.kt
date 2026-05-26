package com.br.unifor.for_library.feature.aluno.livro.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.io.File
import java.net.URL

@Serializable private data class LivroLeitorDb(val arquivo_url: String? = null)
@Serializable private data class ProgressoLeitorDb(val pagina_atual: Int = 0)
@Serializable private data class UsuarioLeitorModel(val id: Int)
@Serializable private data class ConfiguracaoDb(val valor: String = "50")
@Serializable private data class ProgressoPayload(
    val usuario_id: Int,
    val livro_id: Int,
    val pagina_atual: Int,
    val porcentagem_conclusao: Double,
    val status: String
)
@Serializable private data class HistoricoPayload(
    val usuario_id: Int,
    val pontos_ganhos: Int,
    val descricao: String,
    val tipo_referencia: String,
    val referencia_id: Int
)

data class LeitorState(
    val livroId: Int? = null,
    val isLoading: Boolean = true,
    val pdfPronto: Boolean = false,
    val totalPaginas: Int = 0,
    val paginaInicial: Int = 0,
    val pontosGanhos: Int = 50,
    val error: String? = null
)

class LeitorViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(LeitorState())
    val state: StateFlow<LeitorState> = _state.asStateFlow()

    private var pdfRenderer: PdfRenderer? = null
    private val renderMutex = Mutex()
    private var usuarioId: Int? = null

    fun carregarLivro(livroId: Int) {
        if (_state.value.livroId == livroId && _state.value.pdfPronto) return
        viewModelScope.launch {
            _state.value = LeitorState(livroId = livroId, isLoading = true)
            try {
                val userAuth = supabase.auth.currentUserOrNull()
                    ?: throw Exception("Não autenticado")

                val usuario = supabase.from("usuarios")
                    .select { filter { eq("auth_user_id", userAuth.id) } }
                    .decodeSingle<UsuarioLeitorModel>()
                usuarioId = usuario.id

                val livro = supabase.from("livros")
                    .select { filter { eq("id", livroId) } }
                    .decodeSingle<LivroLeitorDb>()

                val paginaInicial = supabase.from("progresso_leitura")
                    .select { filter { eq("usuario_id", usuario.id); eq("livro_id", livroId) } }
                    .decodeList<ProgressoLeitorDb>()
                    .firstOrNull()?.pagina_atual ?: 0

                val arquivoUrl = livro.arquivo_url ?: throw Exception("Livro sem arquivo PDF")

                val cacheFile = File(getApplication<Application>().cacheDir, "livro_$livroId.pdf")
                withContext(Dispatchers.IO) {
                    if (!cacheFile.exists() || cacheFile.length() == 0L) {
                        URL(arquivoUrl).openStream().use { input ->
                            cacheFile.outputStream().use { output -> input.copyTo(output) }
                        }
                    }
                    val fd = ParcelFileDescriptor.open(cacheFile, ParcelFileDescriptor.MODE_READ_ONLY)
                    pdfRenderer?.close()
                    pdfRenderer = PdfRenderer(fd)
                }

                val total = pdfRenderer!!.pageCount
                _state.value = LeitorState(
                    livroId = livroId,
                    isLoading = false,
                    pdfPronto = true,
                    totalPaginas = total,
                    paginaInicial = paginaInicial.coerceIn(0, (total - 1).coerceAtLeast(0))
                )
            } catch (e: Exception) {
                _state.value = LeitorState(isLoading = false, error = e.message ?: "Erro ao carregar livro")
            }
        }
    }

    suspend fun renderizarPagina(pageIndex: Int, widthPx: Int): Bitmap? =
        renderMutex.withLock {
            withContext(Dispatchers.Default) {
                val renderer = pdfRenderer ?: return@withContext null
                if (pageIndex < 0 || pageIndex >= renderer.pageCount) return@withContext null
                val page = renderer.openPage(pageIndex)
                val height = (widthPx.toFloat() * page.height / page.width).toInt().coerceAtLeast(1)
                val bmp = Bitmap.createBitmap(widthPx, height, Bitmap.Config.ARGB_8888)
                bmp.eraseColor(android.graphics.Color.WHITE)
                page.render(bmp, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                page.close()
                bmp
            }
        }

    fun salvarProgresso(pagina: Int) {
        val uid = usuarioId ?: return
        val livroId = _state.value.livroId ?: return
        val total = _state.value.totalPaginas
        val pct = if (total > 0) (pagina + 1).toDouble() / total * 100.0 else 0.0
        val status = if (total > 0 && pagina >= total - 1) "Concluído" else "Lendo"
        viewModelScope.launch {
            try {
                supabase.from("progresso_leitura").upsert(
                    ProgressoPayload(
                        usuario_id = uid,
                        livro_id = livroId,
                        pagina_atual = pagina,
                        porcentagem_conclusao = pct,
                        status = status
                    )
                ) { onConflict = "usuario_id,livro_id" }
            } catch (_: Exception) {}
        }
    }

    // RF11: chamado ao atingir a última página
    fun concluirLeitura() {
        val uid = usuarioId ?: return
        val livroId = _state.value.livroId ?: return
        viewModelScope.launch {
            try {
                // Busca pontos configurados na tabela configuracoes_sistema
                val pontos = supabase.from("configuracoes_sistema")
                    .select { filter { eq("chave", "pontos_por_livro") } }
                    .decodeList<ConfiguracaoDb>()
                    .firstOrNull()?.valor?.toIntOrNull() ?: 50

                _state.value = _state.value.copy(pontosGanhos = pontos)

                // Registra no histórico de pontos
                supabase.from("historico_pontos").insert(
                    HistoricoPayload(
                        usuario_id = uid,
                        pontos_ganhos = pontos,
                        descricao = "Leitura concluída",
                        tipo_referencia = "livro_concluido",
                        referencia_id = livroId
                    )
                )
            } catch (_: Exception) {}
        }
    }

    override fun onCleared() {
        pdfRenderer?.close()
        super.onCleared()
    }
}

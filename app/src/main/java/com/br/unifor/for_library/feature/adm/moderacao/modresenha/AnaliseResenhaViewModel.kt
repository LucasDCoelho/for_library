package com.br.unifor.for_library.feature.adm.moderacao.modresenha

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
private data class ResenhaAdminDb(
    val id: Int,
    val usuario_id: Int,
    val livro_id: Int,
    val nota: Int,
    val texto: String? = null,
    val status: String,
    val created_at: String? = null
)

@Serializable
private data class UsuarioNomeDb(val id: Int, val nome: String? = null)

@Serializable
private data class LivroTituloDb(val id: Int, val titulo: String)

@Serializable
private data class StatusUpdate(val status: String, val motivo_rejeicao: String? = null)

@Serializable
private data class HistoricoPontosInsert(
    val usuario_id: Int,
    val pontos: Int,
    val descricao: String
)

data class ResenhaAdminState(
    val isLoading: Boolean = true,
    val autorNome: String = "",
    val livroTitulo: String = "",
    val nota: Int = 0,
    val texto: String = "",
    val dataEnvio: String = "",
    val processando: Boolean = false,
    val concluido: Boolean = false,
    val erro: String? = null
)

class AnaliseResenhaViewModel(private val resenhaId: Int) : ViewModel() {
    private val _state = MutableStateFlow(ResenhaAdminState())
    val state: StateFlow<ResenhaAdminState> = _state.asStateFlow()

    private var usuarioId: Int = 0

    init {
        carregarResenha()
    }

    private fun carregarResenha() {
        viewModelScope.launch {
            try {
                val resenha = supabase.from("resenhas")
                    .select { filter { eq("id", resenhaId) } }
                    .decodeSingle<ResenhaAdminDb>()

                usuarioId = resenha.usuario_id

                val usuario = runCatching {
                    supabase.from("usuarios")
                        .select { filter { eq("id", resenha.usuario_id) } }
                        .decodeSingle<UsuarioNomeDb>()
                }.getOrNull()

                val livro = runCatching {
                    supabase.from("livros")
                        .select { filter { eq("id", resenha.livro_id) } }
                        .decodeSingle<LivroTituloDb>()
                }.getOrNull()

                _state.value = ResenhaAdminState(
                    isLoading = false,
                    autorNome = usuario?.nome ?: "Aluno #${resenha.usuario_id}",
                    livroTitulo = livro?.titulo ?: "Livro #${resenha.livro_id}",
                    nota = resenha.nota,
                    texto = resenha.texto ?: "",
                    dataEnvio = formatarData(resenha.created_at)
                )
            } catch (e: Exception) {
                _state.value = ResenhaAdminState(
                    isLoading = false,
                    erro = "Erro ao carregar resenha: ${e.message}"
                )
            }
        }
    }

    fun aprovar(pontosPorResenha: Int = 15, onSucesso: () -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(processando = true)
            try {
                supabase.from("resenhas").update(StatusUpdate(status = "Aprovado")) {
                    filter { eq("id", resenhaId) }
                }

                runCatching {
                    supabase.from("historico_pontos").insert(
                        HistoricoPontosInsert(
                            usuario_id = usuarioId,
                            pontos = pontosPorResenha,
                            descricao = "Resenha aprovada"
                        )
                    )
                }

                _state.value = _state.value.copy(processando = false, concluido = true)
                onSucesso()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    processando = false,
                    erro = e.message ?: "Erro ao aprovar resenha"
                )
            }
        }
    }

    fun rejeitar(motivo: String, onSucesso: () -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(processando = true)
            try {
                supabase.from("resenhas").update(
                    StatusUpdate(
                        status = "Rejeitado",
                        motivo_rejeicao = motivo.trim().ifBlank { null }
                    )
                ) {
                    filter { eq("id", resenhaId) }
                }
                _state.value = _state.value.copy(processando = false, concluido = true)
                onSucesso()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    processando = false,
                    erro = e.message ?: "Erro ao rejeitar resenha"
                )
            }
        }
    }

    fun consumirErro() {
        _state.value = _state.value.copy(erro = null)
    }

    private fun formatarData(raw: String?): String {
        if (raw == null) return ""
        return try {
            val partes = raw.split("T")
            val (ano, mes, dia) = partes[0].split("-")
            val meses = listOf("jan","fev","mar","abr","mai","jun","jul","ago","set","out","nov","dez")
            val horario = if (partes.size > 1) partes[1].take(5) else ""
            "$dia de ${meses[mes.toInt() - 1]} de $ano${if (horario.isNotBlank()) " às $horario" else ""}"
        } catch (e: Exception) { raw }
    }
}

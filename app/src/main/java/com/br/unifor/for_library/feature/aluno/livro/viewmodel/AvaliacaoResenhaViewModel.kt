package com.br.unifor.for_library.feature.aluno.livro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
private data class LivroAvaliacaoDb(
    val titulo: String = "",
    val autor: String = "",
    val ano_publicacao: Int? = null,
    val isbn: String? = null
)

@Serializable private data class UsuarioAvaliacaoModel(val id: Int)

@Serializable
private data class ResenhaPayload(
    val usuario_id: Int,
    val livro_id: Int,
    val nota: Int,
    val texto: String? = null,
    val status: String
)

enum class ResultadoAvaliacao { NENHUM, MODERACAO, REGISTRADA, ERRO }

data class AvaliacaoState(
    val isLoading: Boolean = true,
    val tituloLivro: String = "",
    val autor: String = "",
    val anoPublicacao: String = "",
    val isbn: String? = null,
    val enviando: Boolean = false,
    val resultado: ResultadoAvaliacao = ResultadoAvaliacao.NENHUM,
    val error: String? = null
)

class AvaliacaoResenhaViewModel : ViewModel() {

    private val _state = MutableStateFlow(AvaliacaoState())
    val state: StateFlow<AvaliacaoState> = _state.asStateFlow()

    private var usuarioId: Int? = null

    fun carregarLivro(livroId: Int) {
        viewModelScope.launch {
            _state.value = AvaliacaoState(isLoading = true)
            try {
                val userAuth = supabase.auth.currentUserOrNull()
                    ?: throw Exception("Não autenticado")

                val usuario = supabase.from("usuarios")
                    .select { filter { eq("auth_user_id", userAuth.id) } }
                    .decodeSingle<UsuarioAvaliacaoModel>()
                usuarioId = usuario.id

                val livro = supabase.from("livros")
                    .select { filter { eq("id", livroId) } }
                    .decodeSingle<LivroAvaliacaoDb>()

                _state.value = AvaliacaoState(
                    isLoading = false,
                    tituloLivro = livro.titulo,
                    autor = livro.autor,
                    anoPublicacao = livro.ano_publicacao?.toString() ?: "",
                    isbn = livro.isbn
                )
            } catch (e: Exception) {
                _state.value = AvaliacaoState(
                    isLoading = false,
                    error = e.message ?: "Erro ao carregar livro"
                )
            }
        }
    }

    // RF14.6: com texto >= 20 → Pendente (moderação); sem texto → Aprovado (só nota)
    fun enviarResenha(livroId: Int, nota: Int, texto: String) {
        val uid = usuarioId ?: return
        val textoFinal = texto.trim()
        val comTexto = textoFinal.length >= 20

        _state.value = _state.value.copy(enviando = true)
        viewModelScope.launch {
            try {
                supabase.from("resenhas").insert(
                    ResenhaPayload(
                        usuario_id = uid,
                        livro_id = livroId,
                        nota = nota,
                        texto = if (comTexto) textoFinal else null,
                        status = if (comTexto) "Pendente" else "Aprovado"
                    )
                )
                _state.value = _state.value.copy(
                    enviando = false,
                    resultado = if (comTexto) ResultadoAvaliacao.MODERACAO
                                else          ResultadoAvaliacao.REGISTRADA
                )
            } catch (_: Exception) {
                _state.value = _state.value.copy(
                    enviando = false,
                    resultado = ResultadoAvaliacao.ERRO
                )
            }
        }
    }
}

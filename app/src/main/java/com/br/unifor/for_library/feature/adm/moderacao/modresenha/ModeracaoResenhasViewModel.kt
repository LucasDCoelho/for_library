package com.br.unifor.for_library.feature.adm.moderacao.modresenha

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class ResenhaModeracao(
    val id: Int,
    val nota: Int,
    val texto: String? = null,
    val status: String,
    val usuarios: UsuarioSimples? = null,
    val livros: LivroSimples? = null
)

@Serializable
data class UsuarioSimples(val nome: String? = null)

@Serializable
data class LivroSimples(val titulo: String)

@Serializable
private data class ResenhaStatusUpdate(val status: String)

data class ModeracaoResenhasState(
    val isLoading: Boolean = false,
    val resenhas: List<ResenhaModeracao> = emptyList(),
    val erro: String? = null
)

class ModeracaoResenhasViewModel : ViewModel() {
    private val _state = MutableStateFlow(ModeracaoResenhasState())
    val state: StateFlow<ModeracaoResenhasState> = _state.asStateFlow()

    init {
        carregarResenhas()
    }

    fun carregarResenhas() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                // Seleciona resenhas pendentes com joins para usuário e livro
                val colunas = Columns.raw("id, nota, texto, status, usuarios(nome), livros(titulo)")
                val lista = supabase.from("resenhas")
                    .select(colunas) {
                        filter {
                            eq("status", "Pendente")
                        }
                    }
                    .decodeList<ResenhaModeracao>()

                _state.value = ModeracaoResenhasState(
                    isLoading = false,
                    resenhas = lista
                )
            } catch (e: Exception) {
                _state.value = ModeracaoResenhasState(
                    isLoading = false,
                    erro = "Erro ao carregar resenhas: ${e.message}"
                )
            }
        }
    }

    fun analisarResenha(id: Int, aprovado: Boolean) {
        viewModelScope.launch {
            try {
                val novoStatus = if (aprovado) "Aprovado" else "Rejeitado"
                supabase.from("resenhas").update(ResenhaStatusUpdate(status = novoStatus)) {
                    filter { eq("id", id) }
                }
                // Remove da lista local para refletir a ação
                _state.value = _state.value.copy(
                    resenhas = _state.value.resenhas.filter { it.id != id }
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    erro = "Erro ao processar resenha: ${e.message}"
                )
            }
        }
    }
}

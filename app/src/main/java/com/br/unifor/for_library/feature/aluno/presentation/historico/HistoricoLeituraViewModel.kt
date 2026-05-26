package com.br.unifor.for_library.feature.aluno.presentation.historico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class LivroHistorico(
    val id: Int,
    val titulo: String,
    val autor: String,
    val isbn: String? = null
)

@Serializable
data class ProgressoLeitura(
    val id: Int,
    val data_conclusao: String? = null,
    val livros: LivroHistorico? = null
)

@Serializable
private data class UsuarioId(val id: Int)

data class HistoricoState(
    val isLoading: Boolean = false,
    val error: String? = null
)

class HistoricoLeituraViewModel : ViewModel() {
    private val _state = MutableStateFlow(HistoricoState())
    val state = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _historicoOriginal = MutableStateFlow<List<ProgressoLeitura>>(emptyList())

    val historicoFiltrado = combine(_historicoOriginal, _searchQuery) { historico, query ->
        if (query.isBlank()) {
            historico
        } else {
            historico.filter {
                it.livros?.titulo?.contains(query, ignoreCase = true) == true ||
                it.livros?.autor?.contains(query, ignoreCase = true) == true
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        carregarHistorico()
    }

    fun carregarHistorico() {
        viewModelScope.launch {
            _state.value = HistoricoState(isLoading = true)
            try {
                val userAuth = supabase.auth.currentUserOrNull()
                    ?: throw Exception("Usuário não autenticado")

                val usuario = supabase.from("usuarios")
                    .select { filter { eq("auth_user_id", userAuth.id) } }
                    .decodeSingle<UsuarioId>()

                val resultado = supabase.from("progresso_leitura")
                    .select(Columns.raw("id, data_conclusao, livros(id, titulo, autor, isbn)")) {
                        filter {
                            eq("usuario_id", usuario.id)
                            eq("status", "Concluido")
                        }
                        order("data_conclusao", Order.DESCENDING)
                    }
                    .decodeList<ProgressoLeitura>()

                _historicoOriginal.value = resultado
                _state.value = HistoricoState(isLoading = false)
            } catch (e: Exception) {
                _state.value = HistoricoState(
                    isLoading = false,
                    error = e.message ?: "Erro ao carregar histórico"
                )
            }
        }
    }

    fun updateSearchQuery(newQuery: String) {
        _searchQuery.value = newQuery
    }
}

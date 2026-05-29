package com.br.unifor.for_library.feature.adm.moderacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class UsuarioObraDb(
    val nome: String = "",
    val matricula: String = ""
)

@Serializable
data class ObraAutoralItem(
    val id: Int,
    val titulo: String,
    val genero: String,
    val status: String = "Pendente",
    val data_envio: String? = null,
    val usuarios: UsuarioObraDb? = null
) {
    val nomeAutor: String get() = usuarios?.nome ?: "—"
    val matriculaAutor: String get() = usuarios?.matricula ?: "—"
}

data class ModeracaoObrasUiState(
    val isLoading: Boolean = false,
    val obras: List<ObraAutoralItem> = emptyList(),
    val erro: String? = null
)

class ModeracaoObrasViewModel : ViewModel() {

    private val _state = MutableStateFlow(ModeracaoObrasUiState())
    val state: StateFlow<ModeracaoObrasUiState> = _state.asStateFlow()

    init {
        carregarObras()
    }

    fun carregarObras() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, erro = null)
            try {
                val obras = supabase
                    .from("obras_autorais")
                    .select(Columns.raw("*, usuarios(nome, matricula)")) {
                        filter { eq("status", "Pendente") }
                        order("data_envio", Order.DESCENDING)
                    }
                    .decodeList<ObraAutoralItem>()

                _state.value = _state.value.copy(isLoading = false, obras = obras)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    erro = "Erro ao carregar obras pendentes."
                )
            }
        }
    }

    fun consumirErro() {
        _state.value = _state.value.copy(erro = null)
    }
}

package com.br.unifor.for_library.feature.adm.moderacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
private data class IdOnly(val id: Int)

data class HubModeracaoUiState(
    val isLoading: Boolean = false,
    val resenhasPendentes: Int = 0,
    val obrasPendentes: Int = 0,
    val totalUsuarios: Int = 0
)

class HubModeracaoViewModel : ViewModel() {

    private val _state = MutableStateFlow(HubModeracaoUiState())
    val state: StateFlow<HubModeracaoUiState> = _state.asStateFlow()

    init {
        carregarContadores()
    }

    fun carregarContadores() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val resenhasDeferred = async {
                    supabase.from("resenhas")
                        .select { filter { eq("status", "Pendente") } }
                        .decodeList<IdOnly>().size
                }
                val obrasDeferred = async {
                    supabase.from("obras_autorais")
                        .select { filter { eq("status", "Pendente") } }
                        .decodeList<IdOnly>().size
                }
                val usuariosDeferred = async {
                    supabase.from("usuarios")
                        .select { filter { eq("tipo", "Aluno") } }
                        .decodeList<IdOnly>().size
                }

                _state.value = HubModeracaoUiState(
                    isLoading = false,
                    resenhasPendentes = resenhasDeferred.await(),
                    obrasPendentes = obrasDeferred.await(),
                    totalUsuarios = usuariosDeferred.await()
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }
}

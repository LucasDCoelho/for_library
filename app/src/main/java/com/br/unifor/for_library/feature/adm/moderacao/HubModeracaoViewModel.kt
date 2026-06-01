package com.br.unifor.for_library.feature.adm.moderacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Count
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
                    val response = supabase.from("resenhas")
                        .select {
                            filter { eq("status", "Pendente") }
                            count(Count.EXACT)
                            limit(0)
                        }
                    response.countOrNull()?.toInt() ?: 0
                }
                
                val obrasDeferred = async {
                    val response = supabase.from("obras_autorais")
                        .select {
                            filter { eq("status", "Pendente") }
                            count(Count.EXACT)
                            limit(0)
                        }
                    response.countOrNull()?.toInt() ?: 0
                }
                
                val usuariosDeferred = async {
                    val response = supabase.from("usuarios")
                        .select {
                            filter {
                                or {
                                    eq("tipo", "Aluno")
                                    eq("tipo", "ALUNO")
                                    eq("tipo", "aluno")
                                }
                            }
                            count(Count.EXACT)
                            limit(0)
                        }
                    response.countOrNull()?.toInt() ?: 0
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

package com.br.unifor.for_library.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.feature.aluno.eventos.viewmodel.EventoDb
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GestaoEventosState(
    val isLoading: Boolean = true,
    val eventos: List<EventoDb> = emptyList(),
    val erro: String? = null,
    val excluindo: Boolean = false
)

class GestaoEventosViewModel : ViewModel() {
    private val _state = MutableStateFlow(GestaoEventosState())
    val state: StateFlow<GestaoEventosState> = _state.asStateFlow()

    init {
        carregarEventos()
    }

    fun carregarEventos() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, erro = null)
            try {
                val eventos = supabase.from("eventos")
                    .select()
                    .decodeList<EventoDb>()
                _state.value = _state.value.copy(isLoading = false, eventos = eventos)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    erro = e.message ?: "Erro ao carregar eventos"
                )
            }
        }
    }

    fun deletarEvento(id: Int, onSucesso: () -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(excluindo = true)
            try {
                supabase.from("eventos").delete {
                    filter { eq("id", id) }
                }
                _state.value = _state.value.copy(
                    excluindo = false,
                    eventos = _state.value.eventos.filter { it.id != id }
                )
                onSucesso()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    excluindo = false,
                    erro = e.message ?: "Erro ao excluir evento"
                )
            }
        }
    }

    fun consumirErro() {
        _state.value = _state.value.copy(erro = null)
    }
}

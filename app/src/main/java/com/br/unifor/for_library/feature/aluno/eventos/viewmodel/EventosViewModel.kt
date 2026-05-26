package com.br.unifor.for_library.feature.aluno.eventos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

enum class TipoEvento(val label: String) {
    TODOS("Todos"),
    WORKSHOP("Workshops"),
    PALESTRA("Palestras"),
    LANCAMENTO("Lançamentos")
}

data class Evento(
    val id: Int,
    val titulo: String,
    val local: String,
    val descricao: String,
    val mes: String,
    val dia: String,
    val tipo: TipoEvento,
    val bannerUrl: String = ""
)


data class EventosState(
    val isLoading: Boolean = true,
    val eventos: List<Evento> = emptyList(),
    val error: String? = null
)

class EventosViewModel : ViewModel() {
    private val _state = MutableStateFlow(EventosState())
    val state: StateFlow<EventosState> = _state.asStateFlow()

    init {
        carregarEventos()
    }

    fun carregarEventos() {
        viewModelScope.launch {
            _state.value = EventosState(isLoading = true)
            try {
                val eventos = supabase.from("eventos")
                    .select()
                    .decodeList<EventoDb>()
                    .map { it.toEvento() }
                _state.value = EventosState(isLoading = false, eventos = eventos)
            } catch (e: Exception) {
                _state.value = EventosState(
                    isLoading = false,
                    error = e.message ?: "Erro ao carregar eventos"
                )
            }
        }
    }

    private fun EventoDb.toEvento(): Evento {
        val (mes, dia) = parseMesDia(data_inicio)
        return Evento(
            id = id,
            titulo = titulo,
            local = endereco ?: "",
            descricao = descricao ?: "",
            mes = mes,
            dia = dia,
            tipo = mapTipo(tipo),
            bannerUrl = banner_url ?: ""
        )
    }

    private fun parseMesDia(dataInicio: String?): Pair<String, String> {
        if (dataInicio == null) return Pair("", "")
        return try {
            val datePart = dataInicio.split("T")[0]
            val parts = datePart.split("-")
            val meses = listOf("JAN","FEV","MAR","ABR","MAI","JUN","JUL","AGO","SET","OUT","NOV","DEZ")
            val mes = meses[parts[1].toInt() - 1]
            val dia = parts[2].trimStart('0').ifEmpty { "0" }
            Pair(mes, dia)
        } catch (e: Exception) {
            Pair("", "")
        }
    }

    private fun mapTipo(tipo: String): TipoEvento = when (tipo.lowercase().trim()) {
        "workshop" -> TipoEvento.WORKSHOP
        "palestra" -> TipoEvento.PALESTRA
        "lançamento", "lancamento" -> TipoEvento.LANCAMENTO
        else -> TipoEvento.TODOS
    }
}

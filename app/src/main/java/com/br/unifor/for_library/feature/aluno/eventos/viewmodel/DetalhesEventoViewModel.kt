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
import java.time.LocalDateTime
import java.time.ZoneOffset

@Serializable
private data class EventoDb(
    val id: Int,
    val titulo: String,
    val tipo: String = "",
    val descricao: String? = null,
    val sobre: String? = null,
    val banner_url: String? = null,
    val data_inicio: String,
    val data_fim: String? = null,
    val fuso_horario: String = "GMT-3",
    val endereco: String? = null,
    val complemento: String? = null
)

data class EventoDetalhesUi(
    val id: Int,
    val titulo: String,
    val tipo: String,
    val sobreEvento: String,
    val bannerUrl: String,
    val dataFormatada: String,
    val horarioInicio: String,
    val horarioFim: String,
    val fusoHorario: String,
    val local: String,
    val complemento: String,
    val dtStartMillis: Long,
    val dtEndMillis: Long
)

data class DetalhesEventoState(
    val isLoading: Boolean = true,
    val evento: EventoDetalhesUi? = null,
    val error: String? = null
)

class DetalhesEventoViewModel : ViewModel() {
    private val _state = MutableStateFlow(DetalhesEventoState())
    val state: StateFlow<DetalhesEventoState> = _state.asStateFlow()

    fun carregarEvento(eventoId: Int) {
        if (_state.value.evento?.id == eventoId) return
        viewModelScope.launch {
            _state.value = DetalhesEventoState(isLoading = true)
            try {
                val db = supabase.from("eventos")
                    .select { filter { eq("id", eventoId) } }
                    .decodeSingle<EventoDb>()
                _state.value = DetalhesEventoState(isLoading = false, evento = db.toUi())
            } catch (e: Exception) {
                _state.value = DetalhesEventoState(
                    isLoading = false,
                    error = e.message ?: "Erro ao carregar evento"
                )
            }
        }
    }

    private fun EventoDb.toUi(): EventoDetalhesUi {
        val dtInicio = parsarDateTime(data_inicio)
        val dtFim = parsarDateTime(data_fim)
        return EventoDetalhesUi(
            id = id,
            titulo = titulo,
            tipo = tipo.uppercase(),
            sobreEvento = sobre ?: descricao ?: "",
            bannerUrl = banner_url ?: "",
            dataFormatada = formatarData(dtInicio),
            horarioInicio = formatarHora(dtInicio),
            horarioFim = formatarHora(dtFim),
            fusoHorario = fuso_horario,
            local = endereco ?: "",
            complemento = complemento ?: "",
            dtStartMillis = dtInicio?.let { toEpochMillis(it) } ?: 0L,
            dtEndMillis = dtFim?.let { toEpochMillis(it) } ?: 0L
        )
    }

    private fun parsarDateTime(iso: String?): LocalDateTime? {
        if (iso == null) return null
        return try {
            LocalDateTime.parse(iso.take(19))
        } catch (e: Exception) { null }
    }

    private fun formatarData(dt: LocalDateTime?): String {
        dt ?: return ""
        val meses = listOf(
            "Janeiro","Fevereiro","Março","Abril","Maio","Junho",
            "Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"
        )
        return "${dt.dayOfMonth} de ${meses[dt.monthValue - 1]}, ${dt.year}"
    }

    private fun formatarHora(dt: LocalDateTime?): String {
        dt ?: return ""
        return String.format("%02d:%02d", dt.hour, dt.minute)
    }

    private fun toEpochMillis(dt: LocalDateTime): Long =
        dt.toInstant(ZoneOffset.of("-03:00")).toEpochMilli()
}

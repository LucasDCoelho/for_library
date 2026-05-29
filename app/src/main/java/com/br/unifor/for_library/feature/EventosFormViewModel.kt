package com.br.unifor.for_library.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class EventoFormState(
    val titulo: String = "",
    val descricao: String = "",
    val dataIso: String = "",       // "yyyy-MM-dd"
    val dataExibicao: String = "",  // "dd MMM yyyy"
    val horario: String = "",       // "HH:mm"
    val tipo: String = "",
    val endereco: String = "",

    val erroTitulo: String? = null,
    val erroData: String? = null,
    val erroDescricao: String? = null,
    val erroHorario: String? = null,

    val isLoading: Boolean = false,
    val sucesso: Boolean = false,
    val erro: String? = null
)

@Serializable
private data class EventoInsert(
    val titulo: String,
    val descricao: String?,
    val tipo: String,
    val data_inicio: String?,
    val endereco: String?
)

val tiposEvento = listOf("Workshop", "Palestra", "Lançamento")

class EventosFormViewModel(private val eventoId: Int? = null) : ViewModel() {
    private val _state = MutableStateFlow(EventoFormState())
    val state: StateFlow<EventoFormState> = _state.asStateFlow()

    init {
        if (eventoId != null) carregarEvento(eventoId)
    }

    private fun carregarEvento(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val evento = supabase.from("eventos")
                    .select { filter { eq("id", id) } }
                    .decodeSingle<com.br.unifor.for_library.feature.aluno.eventos.viewmodel.EventoDb>()

                val (dataIso, horario) = splitDataHorario(evento.data_inicio)
                val dataExibicao = formatarDataExibicao(dataIso)

                _state.value = _state.value.copy(
                    titulo = evento.titulo,
                    descricao = evento.descricao ?: "",
                    tipo = evento.tipo,
                    dataIso = dataIso,
                    dataExibicao = dataExibicao,
                    horario = horario,
                    endereco = evento.endereco ?: "",
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    erro = "Erro ao carregar evento: ${e.message}"
                )
            }
        }
    }

    fun onTituloChange(v: String) = update { copy(titulo = v, erroTitulo = null) }
    fun onDescricaoChange(v: String) = update { copy(descricao = v, erroDescricao = null) }
    fun onHorarioChange(v: String) = update { copy(horario = v, erroHorario = null) }
    fun onTipoChange(v: String) = update { copy(tipo = v) }
    fun onEnderecoChange(v: String) = update { copy(endereco = v) }
    fun consumirSucesso() = update { copy(sucesso = false) }
    fun consumirErro() = update { copy(erro = null) }

    fun onDataSelecionada(epochMilli: Long) {
        val date = java.time.Instant.ofEpochMilli(epochMilli)
            .atZone(java.time.ZoneOffset.UTC)
            .toLocalDate()
        val iso = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        val exibicao = formatarDataExibicao(iso)
        update { copy(dataIso = iso, dataExibicao = exibicao, erroData = null) }
    }

    fun salvar(onSucesso: () -> Unit) {
        if (!validar()) return

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val s = _state.value
                val dataInicio = if (s.dataIso.isNotBlank()) {
                    val hora = if (s.horario.matches(Regex("\\d{2}:\\d{2}"))) s.horario else "00:00"
                    "${s.dataIso}T$hora:00"
                } else null

                val payload = EventoInsert(
                    titulo = s.titulo.trim(),
                    descricao = s.descricao.trim().ifBlank { null },
                    tipo = s.tipo.lowercase(),
                    data_inicio = dataInicio,
                    endereco = s.endereco.trim().ifBlank { null }
                )

                if (eventoId == null) {
                    supabase.from("eventos").insert(payload)
                } else {
                    supabase.from("eventos").update(payload) {
                        filter { eq("id", eventoId) }
                    }
                }

                _state.value = _state.value.copy(isLoading = false, sucesso = true)
                onSucesso()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    erro = e.message ?: "Erro ao salvar evento"
                )
            }
        }
    }

    private fun validar(): Boolean {
        val s = _state.value
        var valido = true

        val erroTitulo = if (s.titulo.isBlank()) "Título obrigatório" else null
        val erroData = if (s.dataIso.isBlank()) "Data obrigatória"
                       else if (eventoId == null && !isDataFutura(s.dataIso)) "A data deve ser futura"
                       else null
        val erroDescricao = if (s.descricao.isBlank()) "Descrição obrigatória" else null
        val erroHorario = if (s.horario.isNotBlank() && !s.horario.matches(Regex("\\d{2}:\\d{2}")))
                              "Formato: HH:mm" else null

        if (erroTitulo != null || erroData != null || erroDescricao != null || erroHorario != null) {
            valido = false
        }

        update { copy(erroTitulo = erroTitulo, erroData = erroData, erroDescricao = erroDescricao, erroHorario = erroHorario) }
        return valido
    }

    private fun isDataFutura(iso: String): Boolean = try {
        !LocalDate.parse(iso).isBefore(LocalDate.now())
    } catch (e: Exception) { false }

    private fun splitDataHorario(dataInicio: String?): Pair<String, String> {
        if (dataInicio == null) return "" to ""
        val parts = dataInicio.split("T")
        val horario = if (parts.size > 1) parts[1].take(5) else ""
        return parts[0] to horario
    }

    private fun formatarDataExibicao(iso: String): String = try {
        val date = LocalDate.parse(iso)
        val meses = listOf("jan","fev","mar","abr","mai","jun","jul","ago","set","out","nov","dez")
        "${date.dayOfMonth} ${meses[date.monthValue - 1]} ${date.year}"
    } catch (e: Exception) { iso }

    private fun update(block: EventoFormState.() -> EventoFormState) {
        _state.value = _state.value.block()
    }
}

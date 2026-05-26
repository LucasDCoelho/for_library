package com.br.unifor.for_library.feature.aluno.notificacao.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
private data class UsuarioIdModel(val id: Int)

@Serializable
private data class LidoUpdate(val lido: Boolean)

@Serializable
private data class NotificacaoDb(
    val id: Int,
    val titulo: String = "",
    val mensagem: String = "",
    val lido: Boolean = false,
    val data_envio: String = ""
)

data class NotificacaoUi(
    val id: Int,
    val titulo: String,
    val mensagem: String,
    val horarioFormatado: String,
    val lido: Boolean
)

data class NotificacoesState(
    val isLoading: Boolean = true,
    val grupos: LinkedHashMap<String, List<NotificacaoUi>> = linkedMapOf(),
    val error: String? = null
)

class NotificacoesViewModel : ViewModel() {

    private val _state = MutableStateFlow(NotificacoesState())
    val state: StateFlow<NotificacoesState> = _state.asStateFlow()

    private var usuarioId: Int? = null

    init {
        carregarNotificacoes()
    }

    fun carregarNotificacoes() {
        viewModelScope.launch {
            _state.value = NotificacoesState(isLoading = true)
            try {
                val authUser = supabase.auth.currentUserOrNull()
                    ?: throw Exception("Não autenticado")

                val usuario = supabase.from("usuarios")
                    .select { filter { eq("auth_user_id", authUser.id) } }
                    .decodeSingle<UsuarioIdModel>()
                usuarioId = usuario.id

                val lista = supabase.from("notificacoes")
                    .select {
                        filter { eq("usuario_id", usuario.id) }
                        order("data_envio", Order.DESCENDING)
                    }
                    .decodeList<NotificacaoDb>()

                _state.value = NotificacoesState(
                    isLoading = false,
                    grupos = agrupar(lista)
                )
            } catch (e: Exception) {
                _state.value = NotificacoesState(
                    isLoading = false,
                    error = e.message ?: "Erro ao carregar notificações"
                )
            }
        }
    }

    fun marcarComoLida(notificacaoId: Int) {
        // Atualização otimista
        _state.value = _state.value.copy(
            grupos = atualizarLido(_state.value.grupos, notificacaoId, true)
        )
        viewModelScope.launch {
            try {
                supabase.from("notificacoes").update(LidoUpdate(true)) {
                    filter { eq("id", notificacaoId) }
                }
            } catch (_: Exception) {
                // Reverte se falhar
                _state.value = _state.value.copy(
                    grupos = atualizarLido(_state.value.grupos, notificacaoId, false)
                )
            }
        }
    }

    fun marcarTodasComoLidas() {
        val uid = usuarioId ?: return
        // Atualização otimista
        _state.value = _state.value.copy(grupos = marcarTodasLidas(_state.value.grupos))
        viewModelScope.launch {
            try {
                supabase.from("notificacoes").update(LidoUpdate(true)) {
                    filter {
                        eq("usuario_id", uid)
                        eq("lido", false)
                    }
                }
            } catch (_: Exception) {
                carregarNotificacoes()
            }
        }
    }

    private fun agrupar(lista: List<NotificacaoDb>): LinkedHashMap<String, List<NotificacaoUi>> {
        val hoje = LocalDate.now()
        val ontem = hoje.minusDays(1)
        val result = linkedMapOf<String, MutableList<NotificacaoUi>>()

        lista.forEach { db ->
            val dt = parsarDateTime(db.data_envio)
            val data = dt?.toLocalDate()
            val grupo = when (data) {
                hoje   -> "Hoje"
                ontem  -> "Ontem"
                else   -> "Anteriores"
            }
            val horario = formatarHorario(dt, grupo)
            result.getOrPut(grupo) { mutableListOf() }.add(
                NotificacaoUi(
                    id = db.id,
                    titulo = db.titulo,
                    mensagem = db.mensagem,
                    horarioFormatado = horario,
                    lido = db.lido
                )
            )
        }

        // Garante a ordem: Hoje → Ontem → Anteriores
        val ordered = linkedMapOf<String, List<NotificacaoUi>>()
        listOf("Hoje", "Ontem", "Anteriores").forEach { chave ->
            result[chave]?.let { ordered[chave] = it }
        }
        return ordered
    }

    private fun parsarDateTime(iso: String): LocalDateTime? = try {
        LocalDateTime.parse(iso.take(19))
    } catch (_: Exception) { null }

    private fun formatarHorario(dt: LocalDateTime?, grupo: String): String {
        dt ?: return ""
        return when (grupo) {
            "Hoje", "Ontem" -> String.format("%02d:%02d", dt.hour, dt.minute)
            else -> {
                val meses = listOf(
                    "Jan","Fev","Mar","Abr","Mai","Jun",
                    "Jul","Ago","Set","Out","Nov","Dez"
                )
                "${dt.dayOfMonth} ${meses[dt.monthValue - 1]}, ${String.format("%02d:%02d", dt.hour, dt.minute)}"
            }
        }
    }

    private fun atualizarLido(
        grupos: LinkedHashMap<String, List<NotificacaoUi>>,
        id: Int,
        lido: Boolean
    ): LinkedHashMap<String, List<NotificacaoUi>> {
        val novo = LinkedHashMap<String, List<NotificacaoUi>>()
        grupos.forEach { (chave, lista) ->
            novo[chave] = lista.map { if (it.id == id) it.copy(lido = lido) else it }
        }
        return novo
    }

    private fun marcarTodasLidas(
        grupos: LinkedHashMap<String, List<NotificacaoUi>>
    ): LinkedHashMap<String, List<NotificacaoUi>> {
        val novo = LinkedHashMap<String, List<NotificacaoUi>>()
        grupos.forEach { (chave, lista) ->
            novo[chave] = lista.map { it.copy(lido = true) }
        }
        return novo
    }
}

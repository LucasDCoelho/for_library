package com.br.unifor.for_library.feature.aluno.presentation.gamificacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Serializable
data class HistoricoPontos(
    val id: Int,
    val descricao: String,
    val pontos: Int,
    @SerialName("data_ganho")
    val dataGanho: String
) {
    val dataFormatada: String
        get() = try {
            val odt = OffsetDateTime.parse(dataGanho)
            val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.forLanguageTag("pt-BR"))
            odt.format(formatter)
        } catch (e: Exception) {
            dataGanho
        }
}

@Serializable
data class UsuarioGamificacao(
    val id: Int = 0,
    @SerialName("pontos_gamificacao")
    val pontos: Int = 0,
    @SerialName("nivel_gamificacao")
    val nivel: Int = 1
)

data class GamificacaoState(
    val isLoading: Boolean = true,
    val saldoPontos: Int = 0,
    val nivelAtual: Int = 1,
    val historico: List<HistoricoPontos> = emptyList(),
    val error: String? = null
)

class GamificacaoViewModel : ViewModel() {
    private val _state = MutableStateFlow(GamificacaoState())
    val state: StateFlow<GamificacaoState> = _state.asStateFlow()

    init {
        carregarGamificacao()
    }

    fun carregarGamificacao() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val userAuth = supabase.auth.currentUserOrNull()
                    ?: throw Exception("Usuário não autenticado")

                val deferredUsuario = async {
                    supabase.from("usuarios")
                        .select {
                            filter { eq("auth_user_id", userAuth.id) }
                        }
                        .decodeSingle<UsuarioGamificacao>()
                }

                val usuario = deferredUsuario.await()
                
                val historico = supabase.from("historico_pontos")
                    .select {
                        filter { eq("usuario_id", usuario.id) }
                        order("data_ganho", Order.DESCENDING)
                    }
                    .decodeList<HistoricoPontos>()

                _state.value = _state.value.copy(
                    isLoading = false,
                    saldoPontos = usuario.pontos,
                    nivelAtual = usuario.nivel,
                    historico = historico
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Erro ao carregar dados: ${e.message}"
                )
            }
        }
    }

    fun getTituloNivel(nivel: Int): String {
        return when (nivel) {
            1 -> "Leitor Iniciante"
            2 -> "Leitor Assíduo"
            3 -> "Leitor Voraz"
            4 -> "Leitor Especialista"
            5 -> "Leitor Mestre"
            else -> "Leitor"
        }
    }
}

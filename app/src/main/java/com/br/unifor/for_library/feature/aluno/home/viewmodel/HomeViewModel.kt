package com.br.unifor.for_library.feature.aluno.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class UsuarioHome(
    val nome: String = "",
    val foto_perfil: String? = null,
    val pontos_gamificacao: Int = 0,
    val livros_lidos: Int = 0,
    val tempo_total_leitura: String = "0h"
) {
    val primeiroNome: String
        get() = nome.trim().split("\\s+".toRegex()).firstOrNull() ?: ""
}

@Serializable
data class LivroHome(
    val id: Int,
    val titulo: String,
    val autor: String,
    val capa_url: String? = null,
    val isbn: String? = null
)

@Serializable
data class ProgressoHome(
    val capitulo_atual: String? = null,
    val porcentagem_conclusao: Double = 0.0,
    val livros: LivroHome
)

data class HomeState(
    val isLoading: Boolean = false,
    val usuario: UsuarioHome = UsuarioHome(),
    val continueLendo: ProgressoHome? = null,
    val destaques: List<LivroHome> = emptyList(),
    val error: String? = null
)

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        carregarDadosHome()
    }

    fun carregarDadosHome() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val userAuth = supabase.auth.currentUserOrNull() ?: throw Exception("Usuário não autenticado")
                
                // 1. Buscar dados do usuário
                val usuarioPublico = supabase.from("usuarios")
                    .select {
                        filter { eq("auth_user_id", userAuth.id) }
                    }
                    .decodeSingle<UsuarioHome>()

                // 2. Buscar "Continue Lendo" (último livro com status 'Lendo')
                val progresso = try {
                    supabase.from("progresso_leitura")
                        .select(Columns.raw("capitulo_atual, porcentagem_conclusao, livros(id, titulo, autor, capa_url, isbn)")) {
                            filter {
                                eq("usuario_id", userAuth.id)
                                eq("status", "Lendo")
                            }
                            order("data_ultimo_acesso", io.github.jan.supabase.postgrest.query.Order.DESCENDING)
                            limit(1)
                        }
                        .decodeList<ProgressoHome>()
                        .firstOrNull()
                } catch (e: Exception) {
                    null
                }

                // 3. Buscar Destaques (Ex: últimos 10 livros cadastrados)
                val destaques = supabase.from("livros")
                    .select() {
                        order("data_cadastro", io.github.jan.supabase.postgrest.query.Order.DESCENDING)
                        limit(10)
                    }
                    .decodeList<LivroHome>()

                _state.value = _state.value.copy(
                    isLoading = false,
                    usuario = usuarioPublico,
                    continueLendo = progresso,
                    destaques = destaques
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Erro desconhecido ao carregar home"
                )
            }
        }
    }
}

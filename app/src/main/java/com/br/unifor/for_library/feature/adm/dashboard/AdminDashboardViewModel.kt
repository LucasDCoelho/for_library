package com.br.unifor.for_library.feature.adm.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.feature.auth.UsuarioPublicoPayload
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.query.filter.PostgrestFilterBuilder
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class AtividadeAdmin(
    val id: Int,
    val admin_id: Int? = null,
    val titulo_atividade: String,
    val icone_referencia: String? = null,
    val livro_nome: String? = null,
    val autor_nome: String? = null,
    val data_atividade: String
)

data class DashboardUiState(
    val isLoading: Boolean = false,
    val nomeAdmin: String = "Admin",
    val totalLivros: Int = 0,
    val alunosAtivos: Int = 0,
    val resenhasPendentes: Int = 0,
    val obrasPendentes: Int = 0,
    val atividades: List<AtividadeAdmin> = emptyList(),
    val error: String? = null
)

class AdminDashboardViewModel : ViewModel() {

    private val _state = MutableStateFlow(DashboardUiState())
    val state: StateFlow<DashboardUiState> = _state.asStateFlow()

    init {
        carregarDados()
    }

    fun carregarDados() {

        viewModelScope.launch {

            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )

            try {

                val user = supabase.auth.currentUserOrNull()
                    ?: throw Exception("Usuário não autenticado")

                val totalLivrosDef = async {
                    buscarContagem("livros")
                }

                val alunosAtivosDef = async {
                    buscarContagemUsuarios()
                }

                val resenhasPendentesDef = async {
                    buscarContagem("resenhas") {
                        eq("status", "Pendente")
                    }
                }

                val obrasPendentesDef = async {
                    buscarContagem("obras_autorais") {
                        eq("status", "Pendente")
                    }
                }

                val atividadesDef = async {

                    try {

                        supabase
                            .from("atividades_admin")
                            .select {

                                order(
                                    column = "data_atividade",
                                    order = Order.DESCENDING
                                )

                                limit(10)
                            }
                            .decodeList<AtividadeAdmin>()

                    } catch (e: Exception) {
                        emptyList()
                    }
                }

                _state.value = _state.value.copy(
                    isLoading = false,
                    nomeAdmin = user.email ?: "Admin",
                    totalLivros = totalLivrosDef.await(),
                    alunosAtivos = alunosAtivosDef.await(),
                    resenhasPendentes = resenhasPendentesDef.await(),
                    obrasPendentes = obrasPendentesDef.await(),
                    atividades = atividadesDef.await()
                )

            } catch (e: Exception) {

                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Erro ao carregar dashboard"
                )
            }
        }
    }

    private suspend fun buscarContagem(
        tabela: String,
        filtro: (PostgrestFilterBuilder.() -> Unit)? = null
    ): Int {

        return try {

            val response = supabase
                .from(tabela)
                .select {

                    if (filtro != null) {
                        filter(filtro)
                    }
                }

            response.decodeList<Map<String, Any>>().size

        } catch (e: Exception) {
            0
        }
    }

    private suspend fun buscarContagemUsuarios(): Int {

        for (tabela in listOf("usuarios", "usuario")) {

            try {

                val list = supabase
                    .from(tabela)
                    .select {

                        filter {

                            or {
                                eq("tipo", "ALUNO")
                                eq("tipo", "aluno")
                            }
                        }
                    }
                    .decodeList<UsuarioPublicoPayload>()

                if (list.isNotEmpty()) {
                    return list.size
                }

            } catch (e: Exception) {
                continue
            }
        }

        return 0
    }
}
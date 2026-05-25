package com.br.unifor.for_library.feature.aluno.acervo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.core.components.FiltroAvancadoState
import com.br.unifor.for_library.core.components.OrdemFiltro
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.temporal.ChronoUnit

@Serializable
data class UsuarioAcervo(
    val nome: String = "",
    val foto_perfil: String? = null
) {
    val primeiroNome: String
        get() = nome.trim().split("\\s+".toRegex()).firstOrNull() ?: ""
}

@Serializable
data class LivroAcervo(
    val id: Int,
    val titulo: String,
    val autor: String,
    val capa_url: String? = null,
    val isbn: String? = null,
    val genero: String? = null,
    val data_cadastro: String? = null,
    val avaliacao: Float? = null
) {
    val isNovo: Boolean
        get() {
            return try {
                if (data_cadastro == null) return false
                val data = Instant.parse(data_cadastro)
                val seteDiasAtras = Instant.now().minus(7, ChronoUnit.DAYS)
                data.isAfter(seteDiasAtras)
            } catch (e: Exception) {
                false
            }
        }
}

data class AcervoState(
    val isLoading: Boolean = false,
    val usuario: UsuarioAcervo = UsuarioAcervo(),
    val livros: List<LivroAcervo> = emptyList(),
    val query: String = "",
    val categoriaSelecionada: String = "Tudo",
    val filtrosAvancados: FiltroAvancadoState = FiltroAvancadoState(),
    val error: String? = null
)

class AcervoViewModel : ViewModel() {
    private val _state = MutableStateFlow(AcervoState())
    val state: StateFlow<AcervoState> = _state.asStateFlow()

    init {
        carregarDadosIniciais()
        carregarLivros()
    }

    private fun carregarDadosIniciais() {
        viewModelScope.launch {
            try {
                val userAuth = supabase.auth.currentUserOrNull()
                if (userAuth != null) {
                    val usuario = supabase.from("usuarios")
                        .select {
                            filter { eq("auth_user_id", userAuth.id) }
                        }
                        .decodeSingle<UsuarioAcervo>()
                    _state.value = _state.value.copy(usuario = usuario)
                }
            } catch (e: Exception) {
                // Silently fail or handle error
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _state.value = _state.value.copy(query = newQuery)
        carregarLivros()
    }

    fun onCategoriaChange(novaCategoria: String) {
        _state.value = _state.value.copy(categoriaSelecionada = novaCategoria)
        carregarLivros()
    }

    fun onAplicarFiltrosAvancados(novosFiltros: FiltroAvancadoState) {
        _state.value = _state.value.copy(filtrosAvancados = novosFiltros)
        carregarLivros()
    }

    fun limparFiltros() {
        _state.value = _state.value.copy(
            query = "",
            categoriaSelecionada = "Tudo",
            filtrosAvancados = FiltroAvancadoState()
        )
        carregarLivros()
    }

    fun carregarLivros() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val queryStr = _state.value.query
                val categoria = _state.value.categoriaSelecionada
                val filtrosAvancados = _state.value.filtrosAvancados

                val response = supabase.from("livros").select {
                    filter {
                        if (queryStr.isNotEmpty()) {
                            or {
                                ilike("titulo", "%$queryStr%")
                                ilike("autor", "%$queryStr%")
                            }
                        }

                        // Filtro de Gêneros (Combina categoria rápida e filtros avançados)
                        val generosParaFiltrar = filtrosAvancados.generosSelecionados.toMutableSet()
                        if (categoria != "Tudo") {
                            generosParaFiltrar.add(categoria)
                        }

                        if (generosParaFiltrar.isNotEmpty()) {
                            isIn("genero", generosParaFiltrar.toList())
                        }
                    }

                    // Ordenação Dinâmica (RF07.3)
                    when (filtrosAvancados.ordem) {
                        OrdemFiltro.A_Z -> order("titulo", Order.ASCENDING)
                        OrdemFiltro.Z_A -> order("titulo", Order.DESCENDING)
                        OrdemFiltro.MAIS_RECENTES -> order("data_cadastro", Order.DESCENDING)
                        OrdemFiltro.MELHOR_AVALIADOS -> order("avaliacao", Order.DESCENDING)
                    }
                }

                val livros = response.decodeList<LivroAcervo>()
                _state.value = _state.value.copy(isLoading = false, livros = livros)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Erro ao carregar acervo"
                )
            }
        }
    }
}

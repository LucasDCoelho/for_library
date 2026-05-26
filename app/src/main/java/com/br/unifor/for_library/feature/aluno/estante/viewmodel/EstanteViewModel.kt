package com.br.unifor.for_library.feature.aluno.estante.viewmodel

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
data class LivroEstante(
    val id: Int,
    val titulo: String,
    val autor: String,
    val total_paginas: Int = 0,
    val isbn: String? = null
)

@Serializable
data class ProgressoEstante(
    val id: Int,
    val pagina_atual: Int = 0,
    val livros: LivroEstante
) {
    val progressoPct: Float
        get() = if (livros.total_paginas > 0)
            pagina_atual.toFloat() / livros.total_paginas.toFloat()
        else 0f
}

@Serializable
data class FavoritoEstante(
    val id: Int,
    val livros: LivroEstante
)

@Serializable
private data class UsuarioId(val id: Int)

data class EstanteState(
    val isLoading: Boolean = true,
    val livrosLendo: List<ProgressoEstante> = emptyList(),
    val livrosFavoritos: List<FavoritoEstante> = emptyList(),
    val error: String? = null
)

class EstanteViewModel : ViewModel() {
    private val _state = MutableStateFlow(EstanteState())
    val state: StateFlow<EstanteState> = _state.asStateFlow()

    init {
        carregarEstante()
    }

    fun carregarEstante() {
        viewModelScope.launch {
            _state.value = EstanteState(isLoading = true)
            try {
                val userAuth = supabase.auth.currentUserOrNull()
                    ?: throw Exception("Usuário não autenticado")

                val usuario = supabase.from("usuarios")
                    .select { filter { eq("auth_user_id", userAuth.id) } }
                    .decodeSingle<UsuarioId>()

                val lendo = supabase.from("progresso_leitura")
                    .select(Columns.raw("id, pagina_atual, livros(id, titulo, autor, total_paginas, isbn)")) {
                        filter {
                            eq("usuario_id", usuario.id)
                            eq("status", "Lendo")
                        }
                        order("data_ultimo_acesso", io.github.jan.supabase.postgrest.query.Order.DESCENDING)
                    }
                    .decodeList<ProgressoEstante>()

                val favoritos = supabase.from("favoritos")
                    .select(Columns.raw("id, livros(id, titulo, autor, total_paginas, isbn)")) {
                        filter { eq("usuario_id", usuario.id) }
                        order("data_adicionado", io.github.jan.supabase.postgrest.query.Order.DESCENDING)
                    }
                    .decodeList<FavoritoEstante>()

                _state.value = EstanteState(
                    isLoading = false,
                    livrosLendo = lendo,
                    livrosFavoritos = favoritos
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Erro ao carregar estante"
                )
            }
        }
    }

    fun removerFavorito(favoritoId: Int) {
        // Remoção otimista: atualiza a UI antes de confirmar no banco
        _state.value = _state.value.copy(
            livrosFavoritos = _state.value.livrosFavoritos.filter { it.id != favoritoId }
        )
        viewModelScope.launch {
            try {
                supabase.from("favoritos").delete {
                    filter { eq("id", favoritoId) }
                }
            } catch (e: Exception) {
                // Reverte se a deleção falhar
                carregarEstante()
            }
        }
    }
}

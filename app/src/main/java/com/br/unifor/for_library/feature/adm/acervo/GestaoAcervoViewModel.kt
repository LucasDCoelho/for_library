package com.br.unifor.for_library.feature.adm.acervo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class LivroAdmin(
    val id: Int,
    val titulo: String,
    val autor: String,
    val isbn: String? = null,
    val capa_url: String? = null,
    val arquivo_url: String? = null,
    val genero: String? = null,
    val total_paginas: Int? = null,
    val sinopse: String? = null,
    val ano_publicacao: Int? = null
)

data class GestaoAcervoUiState(
    val isLoading: Boolean = false,
    val livros: List<LivroAdmin> = emptyList(),
    val query: String = "",
    val erro: String? = null,
    val mensagemSucesso: String? = null
)

class GestaoAcervoViewModel : ViewModel() {

    private val _state = MutableStateFlow(GestaoAcervoUiState())
    val state: StateFlow<GestaoAcervoUiState> = _state.asStateFlow()

    init {
        carregarLivros()
    }

    fun onQueryChange(nova: String) {
        _state.value = _state.value.copy(query = nova)
        carregarLivros()
    }

    fun carregarLivros() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, erro = null)
            try {
                val query = _state.value.query
                val livros = supabase.from("livros").select {
                    filter {
                        if (query.isNotEmpty()) {
                            or {
                                ilike("titulo", "%$query%")
                                ilike("autor", "%$query%")
                            }
                        }
                    }
                    order("titulo", Order.ASCENDING)
                }.decodeList<LivroAdmin>()
                _state.value = _state.value.copy(isLoading = false, livros = livros)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    erro = e.message ?: "Erro ao carregar acervo"
                )
            }
        }
    }

    fun deletarLivro(id: Int) {
        viewModelScope.launch {
            try {
                supabase.from("livros").delete {
                    filter { eq("id", id) }
                }
                _state.value = _state.value.copy(
                    livros = _state.value.livros.filter { it.id != id },
                    mensagemSucesso = "Obra removida com sucesso"
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    erro = e.message ?: "Erro ao excluir livro"
                )
            }
        }
    }

    fun limparMensagem() {
        _state.value = _state.value.copy(mensagemSucesso = null, erro = null)
    }
}

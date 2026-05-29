package com.br.unifor.for_library.feature.adm.acervo

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
private data class AtualizacaoLivro(
    val titulo: String,
    val autor: String,
    val isbn: String? = null,
    val genero: String? = null,
    val total_paginas: Int? = null,
    val sinopse: String? = null,
    val ano_publicacao: Int? = null,
    val capa_url: String? = null,
    val arquivo_url: String? = null
)

data class EdicaoLivroUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val livroId: Int = 0,
    val titulo: String = "",
    val autor: String = "",
    val isbn: String = "",
    val genero: String = "",
    val totalPaginas: String = "",
    val sinopse: String = "",
    val anoPublicacao: String = "",
    val capaUrl: String? = null,
    val arquivoUrl: String? = null,
    val novaCapaUri: Uri? = null,
    val novoArquivoUri: Uri? = null,
    val sucesso: Boolean = false,
    val erro: String? = null
)

class EdicaoLivroViewModel : ViewModel() {

    private val _state = MutableStateFlow(EdicaoLivroUiState())
    val state: StateFlow<EdicaoLivroUiState> = _state.asStateFlow()

    private var carregado = false

    fun carregarLivro(livroId: String) {
        if (carregado) return
        val id = livroId.toIntOrNull() ?: return
        carregado = true
        viewModelScope.launch {
            update { copy(isLoading = true, erro = null) }
            try {
                val livro = supabase.from("livros").select {
                    filter { eq("id", id) }
                }.decodeSingle<LivroAdmin>()
                update {
                    copy(
                        isLoading = false,
                        livroId = livro.id,
                        titulo = livro.titulo,
                        autor = livro.autor,
                        isbn = livro.isbn ?: "",
                        genero = livro.genero ?: "",
                        totalPaginas = livro.total_paginas?.toString() ?: "",
                        sinopse = livro.sinopse ?: "",
                        anoPublicacao = livro.ano_publicacao?.toString() ?: "",
                        capaUrl = livro.capa_url,
                        arquivoUrl = livro.arquivo_url
                    )
                }
            } catch (e: Exception) {
                update { copy(isLoading = false, erro = "Erro ao carregar livro: ${e.message}") }
            }
        }
    }

    fun onTituloChange(v: String) = update { copy(titulo = v) }
    fun onAutorChange(v: String) = update { copy(autor = v) }
    fun onIsbnChange(v: String) = update { copy(isbn = v) }
    fun onGeneroChange(v: String) = update { copy(genero = v) }
    fun onTotalPaginasChange(v: String) = update { copy(totalPaginas = v) }
    fun onSinopseChange(v: String) = update { copy(sinopse = v) }
    fun onAnoPublicacaoChange(v: String) = update { copy(anoPublicacao = v) }
    fun onCapaSelecionada(uri: Uri?) = update { copy(novaCapaUri = uri) }
    fun onArquivoSelecionado(uri: Uri?) = update { copy(novoArquivoUri = uri) }

    fun salvar(context: Context) {
        val s = _state.value
        if (s.titulo.isBlank() || s.autor.isBlank()) return
        viewModelScope.launch {
            update { copy(isSaving = true, erro = null) }
            try {
                var capaUrl = s.capaUrl
                s.novaCapaUri?.let { uri ->
                    val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    if (bytes != null) {
                        val fileName = "livros/${System.currentTimeMillis()}.jpg"
                        val bucket = supabase.storage.from("imagens_livros")
                        bucket.upload(fileName, bytes)
                        capaUrl = bucket.publicUrl(fileName)
                    }
                }

                var arquivoUrl = s.arquivoUrl
                s.novoArquivoUri?.let { uri ->
                    val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    if (bytes != null) {
                        val fileName = "livros/${System.currentTimeMillis()}.pdf"
                        val bucket = supabase.storage.from("arquivos_livros")
                        bucket.upload(fileName, bytes)
                        arquivoUrl = bucket.publicUrl(fileName)
                    }
                }

                supabase.from("livros").update(
                    AtualizacaoLivro(
                        titulo = s.titulo.trim(),
                        autor = s.autor.trim(),
                        isbn = s.isbn.trim().ifBlank { null },
                        genero = s.genero.ifBlank { null },
                        total_paginas = s.totalPaginas.trim().toIntOrNull(),
                        sinopse = s.sinopse.trim().ifBlank { null },
                        ano_publicacao = s.anoPublicacao.trim().toIntOrNull(),
                        capa_url = capaUrl,
                        arquivo_url = arquivoUrl
                    )
                ) {
                    filter { eq("id", s.livroId) }
                }
                update {
                    copy(
                        isSaving = false,
                        sucesso = true,
                        capaUrl = capaUrl,
                        arquivoUrl = arquivoUrl,
                        novaCapaUri = null,
                        novoArquivoUri = null
                    )
                }
            } catch (e: Exception) {
                update { copy(isSaving = false, erro = "Erro ao salvar: ${e.message}") }
            }
        }
    }

    fun limparSucesso() = update { copy(sucesso = false) }

    private fun update(block: EdicaoLivroUiState.() -> EdicaoLivroUiState) {
        _state.value = _state.value.block()
    }
}

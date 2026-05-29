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
private data class NovoLivro(
    val titulo: String,
    val autor: String,
    val genero: String,
    val total_paginas: Int,
    val sinopse: String? = null,
    val ano_publicacao: Int? = null,
    val capa_url: String? = null,
    val arquivo_url: String? = null
)

data class CadastroLivroUiState(
    val titulo: String = "",
    val autor: String = "",
    val genero: String = "",
    val totalPaginas: String = "",
    val sinopse: String = "",
    val anoPublicacao: String = "",
    val capaUri: Uri? = null,
    val arquivoUri: Uri? = null,
    val erros: Map<String, String> = emptyMap(),
    val isLoading: Boolean = false,
    val sucesso: Boolean = false,
    val erro: String? = null
)

class CadastroLivroViewModel : ViewModel() {

    private val _state = MutableStateFlow(CadastroLivroUiState())
    val state: StateFlow<CadastroLivroUiState> = _state.asStateFlow()

    fun onTituloChange(v: String) = update { copy(titulo = v, erros = erros - "titulo") }
    fun onAutorChange(v: String) = update { copy(autor = v, erros = erros - "autor") }
    fun onGeneroChange(v: String) = update { copy(genero = v, erros = erros - "genero") }
    fun onTotalPaginasChange(v: String) = update { copy(totalPaginas = v, erros = erros - "totalPaginas") }
    fun onSinopseChange(v: String) = update { copy(sinopse = v) }
    fun onAnoPublicacaoChange(v: String) = update { copy(anoPublicacao = v, erros = erros - "anoPublicacao") }
    fun onCapaSelecionada(uri: Uri?) = update { copy(capaUri = uri) }
    fun onArquivoSelecionado(uri: Uri?) = update { copy(arquivoUri = uri) }

    fun salvar(context: Context) {
        if (!validar()) return
        viewModelScope.launch {
            update { copy(isLoading = true, erro = null) }
            try {
                val s = _state.value
                var capaUrl: String? = null
                s.capaUri?.let { uri ->
                    val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    if (bytes != null) {
                        val fileName = "livros/${System.currentTimeMillis()}.jpg"
                        val bucket = supabase.storage.from("imagens_livros")
                        bucket.upload(fileName, bytes)
                        capaUrl = bucket.publicUrl(fileName)
                    }
                }

                var arquivoUrl: String? = null
                s.arquivoUri?.let { uri ->
                    val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    if (bytes != null) {
                        val fileName = "livros/${System.currentTimeMillis()}.pdf"
                        val bucket = supabase.storage.from("arquivos_livros")
                        bucket.upload(fileName, bytes)
                        arquivoUrl = bucket.publicUrl(fileName)
                    }
                }

                supabase.from("livros").insert(
                    NovoLivro(
                        titulo = s.titulo.trim(),
                        autor = s.autor.trim(),
                        genero = s.genero,
                        total_paginas = s.totalPaginas.trim().toInt(),
                        sinopse = s.sinopse.trim().ifBlank { null },
                        ano_publicacao = s.anoPublicacao.trim().toIntOrNull(),
                        capa_url = capaUrl,
                        arquivo_url = arquivoUrl
                    )
                )
                update { CadastroLivroUiState(sucesso = true) }
            } catch (e: Exception) {
                update { copy(isLoading = false, erro = "Erro ao salvar: ${e.message}") }
            }
        }
    }

    fun limparSucesso() = update { copy(sucesso = false) }

    private fun validar(): Boolean {
        val s = _state.value
        val erros = mutableMapOf<String, String>()
        if (s.titulo.isBlank()) erros["titulo"] = "Título é obrigatório"
        if (s.autor.isBlank()) erros["autor"] = "Autor é obrigatório"
        if (s.genero.isBlank()) erros["genero"] = "Categoria é obrigatória"
        val paginas = s.totalPaginas.trim().toIntOrNull()
        if (paginas == null || paginas <= 0) erros["totalPaginas"] = "Total de páginas inválido"
        val ano = s.anoPublicacao.trim()
        if (ano.isNotBlank()) {
            val anoInt = ano.toIntOrNull()
            if (anoInt == null || anoInt < 1000 || anoInt > 2100) erros["anoPublicacao"] = "Ano inválido"
        }
        update { copy(erros = erros) }
        return erros.isEmpty()
    }

    private fun update(block: CadastroLivroUiState.() -> CadastroLivroUiState) {
        _state.value = _state.value.block()
    }
}

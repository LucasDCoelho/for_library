package com.br.unifor.for_library.feature.adm.moderacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
private data class UsuarioAnaliseObraDb(
    val id: Int? = null,
    val nome: String? = null,
    val matricula: String? = null
)

@Serializable
private data class AnaliseObraDetalheDb(
    val id: Int,
    val titulo: String,
    val genero: String,
    val sinopse_curta: String? = null,
    val arquivo_pdf_url: String? = null,
    val status: String = "Pendente",
    val data_envio: String? = null,
    val usuarios: UsuarioAnaliseObraDb? = null
)

@Serializable
private data class StatusObraUpdate(
    val status: String,
    val motivo_rejeicao: String? = null
)

data class AnaliseObraState(
    val isLoading: Boolean = true,
    val titulo: String = "",
    val genero: String = "",
    val sinopse: String = "",
    val pdfUrl: String = "",
    val nomeAutor: String = "",
    val matriculaAutor: String = "",
    val dataEnvio: String = "",
    val processando: Boolean = false,
    val erro: String? = null
)

@Serializable
private data class LivroModeracaoInsert(
    val titulo: String,
    val autor: String,
    val genero: String,
    val sinopse: String? = null,
    val arquivo_url: String? = null,
    val capa_url: String? = null,
    val total_paginas: Int = 0,
    val ano_publicacao: Int? = null
)

class AnaliseObraViewModel(private val obraId: Int) : ViewModel() {
    private val _state = MutableStateFlow(AnaliseObraState())
    val state: StateFlow<AnaliseObraState> = _state.asStateFlow()

    init {
        carregarObra()
    }

    private fun carregarObra() {
        viewModelScope.launch {
            try {
                val obra = supabase.from("obras_autorais")
                    .select(Columns.raw("*, usuarios(nome, matricula)")) {
                        filter { eq("id", obraId) }
                    }
                    .decodeSingle<AnaliseObraDetalheDb>()

                _state.value = AnaliseObraState(
                    isLoading = false,
                    titulo = obra.titulo,
                    genero = obra.genero,
                    sinopse = obra.sinopse_curta ?: "",
                    pdfUrl = obra.arquivo_pdf_url ?: "",
                    nomeAutor = obra.usuarios?.nome ?: "—",
                    matriculaAutor = obra.usuarios?.matricula ?: "—",
                    dataEnvio = formatarData(obra.data_envio)
                )
            } catch (e: Exception) {
                _state.value = AnaliseObraState(
                    isLoading = false,
                    erro = "Erro ao carregar obra: ${e.message}"
                )
            }
        }
    }

    fun aprovar(onSucesso: () -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(processando = true)
            try {
                // 1. Busca os dados atuais da obra com o nome do autor para replicar na tabela de livros
                val obra = supabase.from("obras_autorais")
                    .select(Columns.raw("*, usuarios(id, nome)")) {
                        filter { eq("id", obraId) }
                    }
                    .decodeSingle<AnaliseObraDetalheDb>()

                // Validação de integridade referencial: garante que o autor existe na tabela usuarios
                if (obra.usuarios?.id == null) {
                    throw IllegalStateException("Usuário autor da obra não encontrado no sistema.")
                }

                // 2. Insere na tabela de livros para que apareça no acervo
                val novoLivro = LivroModeracaoInsert(
                    titulo = obra.titulo,
                    autor = obra.usuarios.nome ?: "Autor Desconhecido",
                    genero = obra.genero,
                    sinopse = obra.sinopse_curta,
                    arquivo_url = obra.arquivo_pdf_url,
                    total_paginas = 0
                )
                
                println("ModeracaoDebug: Tentando inserir livro: $novoLivro")

                supabase.from("livros").insert(novoLivro)

                // 3. Atualiza o status da obra autoral para 'Aprovado'
                supabase.from("obras_autorais")
                    .update(StatusObraUpdate(status = "Aprovado")) {
                        filter { eq("id", obraId) }
                    }

                _state.value = _state.value.copy(processando = false)
                onSucesso()
            } catch (e: Exception) {
                println("ModeracaoDebug: Erro ao aprovar obra $obraId")
                e.printStackTrace()
                _state.value = _state.value.copy(
                    processando = false,
                    erro = "Erro ao aprovar obra: ${e.localizedMessage ?: e.message}"
                )
            }
        }
    }

    fun rejeitar(motivo: String, onSucesso: () -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(processando = true)
            try {
                supabase.from("obras_autorais")
                    .update(
                        StatusObraUpdate(
                            status = "Rejeitado",
                            motivo_rejeicao = motivo.trim().ifBlank { null }
                        )
                    ) {
                        filter { eq("id", obraId) }
                    }
                _state.value = _state.value.copy(processando = false)
                onSucesso()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    processando = false,
                    erro = e.message ?: "Erro ao rejeitar obra"
                )
            }
        }
    }

    fun consumirErro() {
        _state.value = _state.value.copy(erro = null)
    }

    private fun formatarData(raw: String?): String {
        if (raw == null) return ""
        return try {
            val partes = raw.split("T")
            val (ano, mes, dia) = partes[0].split("-")
            val meses = listOf("jan","fev","mar","abr","mai","jun","jul","ago","set","out","nov","dez")
            "$dia de ${meses[mes.toInt() - 1]} de $ano"
        } catch (e: Exception) { raw }
    }
}

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
private data class ObraAutoralDetalheDb(
    val id: Int,
    val titulo: String,
    val genero: String,
    val sinopse_curta: String? = null,
    val arquivo_pdf_url: String? = null,
    val status: String = "Pendente",
    val data_envio: String? = null,
    val usuarios: UsuarioObraDb? = null
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
                    .decodeSingle<ObraAutoralDetalheDb>()

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
                supabase.from("obras_autorais")
                    .update(StatusObraUpdate(status = "Aprovado")) {
                        filter { eq("id", obraId) }
                    }
                _state.value = _state.value.copy(processando = false)
                onSucesso()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    processando = false,
                    erro = e.message ?: "Erro ao aprovar obra"
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

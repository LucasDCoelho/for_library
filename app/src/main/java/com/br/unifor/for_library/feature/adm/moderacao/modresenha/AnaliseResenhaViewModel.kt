package com.br.unifor.for_library.feature.adm.moderacao.modresenha

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
private data class ResenhaAdminDb(
    val id: Int,
    val usuario_id: Int,
    val livro_id: Int,
    val nota: Int,
    val texto: String? = null,
    val status: String,
    val created_at: String? = null
)

@Serializable
private data class UsuarioResenhaDetalheDb(val id: Int, val nome: String? = null)

@Serializable
private data class LivroResenhaDetalheDb(val id: Int, val titulo: String)

@Serializable
private data class AnaliseResenhaStatusUpdate(val status: String, val motivo_rejeicao: String? = null)

@Serializable
private data class HistoricoPontosInsert(
    val usuario_id: Int,
    val pontos_ganhos: Int,
    val descricao: String,
    val tipo_referencia: String? = null,
    val referencia_id: Int? = null
)

@Serializable
private data class NotificacaoInsert(
    val usuario_id: Int,
    val titulo: String,
    val mensagem: String
)

@Serializable
private data class UsuarioPontosDb(val pontos_gamificacao: Int? = 0)

@Serializable
private data class UsuarioPontosUpdate(val pontos_gamificacao: Int)

@Serializable
private data class LivroEstatisticasUpdate(val nota_media: Double, val qtd_avaliacoes: Int)

@Serializable
private data class ResenhaNotaDb(val nota: Int)

data class ResenhaAdminState(
    val isLoading: Boolean = true,
    val autorNome: String = "",
    val livroTitulo: String = "",
    val nota: Int = 0,
    val texto: String = "",
    val dataEnvio: String = "",
    val processando: Boolean = false,
    val concluido: Boolean = false,
    val erro: String? = null
)

class AnaliseResenhaViewModel(private val resenhaId: Int) : ViewModel() {
    private val _state = MutableStateFlow(ResenhaAdminState())
    val state: StateFlow<ResenhaAdminState> = _state.asStateFlow()

    private var usuarioId: Int = 0
    private var livroId: Int = 0

    init {
        carregarResenha()
    }

    private fun carregarResenha() {
        viewModelScope.launch {
            try {
                val resenha = supabase.from("resenhas")
                    .select { filter { eq("id", resenhaId) } }
                    .decodeSingle<ResenhaAdminDb>()

                usuarioId = resenha.usuario_id
                livroId = resenha.livro_id

                val usuario = runCatching {
                    supabase.from("usuarios")
                        .select { filter { eq("id", resenha.usuario_id) } }
                        .decodeSingle<UsuarioResenhaDetalheDb>()
                }.getOrNull()

                val livro = runCatching {
                    supabase.from("livros")
                        .select { filter { eq("id", resenha.livro_id) } }
                        .decodeSingle<LivroResenhaDetalheDb>()
                }.getOrNull()

                _state.value = ResenhaAdminState(
                    isLoading = false,
                    autorNome = usuario?.nome ?: "Aluno #${resenha.usuario_id}",
                    livroTitulo = livro?.titulo ?: "Livro #${resenha.livro_id}",
                    nota = resenha.nota,
                    texto = resenha.texto ?: "",
                    dataEnvio = formatarData(resenha.created_at)
                )
            } catch (e: Exception) {
                _state.value = ResenhaAdminState(
                    isLoading = false,
                    erro = "Erro ao carregar resenha: ${e.message}"
                )
            }
        }
    }

    fun aprovar(pontosPorResenha: Int = 15, onSucesso: () -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(processando = true)
            try {
                // 1. Aprovar a resenha
                supabase.from("resenhas").update(AnaliseResenhaStatusUpdate(status = "Aprovado")) {
                    filter { eq("id", resenhaId) }
                }

                // 2. Dar pontos ao usuário e registrar histórico
                runCatching {
                    val usuario = supabase.from("usuarios")
                        .select { filter { eq("id", usuarioId) } }
                        .decodeSingle<UsuarioPontosDb>()
                    
                    val novosPontos = (usuario.pontos_gamificacao ?: 0) + pontosPorResenha
                    
                    supabase.from("usuarios").update(UsuarioPontosUpdate(pontos_gamificacao = novosPontos)) {
                        filter { eq("id", usuarioId) }
                    }

                    supabase.from("historico_pontos").insert(
                        HistoricoPontosInsert(
                            usuario_id = usuarioId,
                            pontos_ganhos = pontosPorResenha,
                            descricao = "Resenha aprovada: ${_state.value.livroTitulo}",
                            tipo_referencia = "resenha",
                            referencia_id = resenhaId
                        )
                    )
                }

                // 3. Enviar notificação ao usuário
                runCatching {
                    supabase.from("notificacoes").insert(
                        NotificacaoInsert(
                            usuario_id = usuarioId,
                            titulo = "Resenha Aprovada! \uD83C\uDF89",
                            mensagem = "Sua resenha do livro '${_state.value.livroTitulo}' foi aprovada e você ganhou $pontosPorResenha pontos!"
                        )
                    )
                }

                // 4. Atualizar estatísticas do livro (nota média e qtd avaliações)
                runCatching {
                    val resenhasAprovadas = supabase.from("resenhas")
                        .select {
                            filter {
                                eq("livro_id", livroId)
                                eq("status", "Aprovado")
                            }
                        }
                        .decodeList<ResenhaNotaDb>()
                    
                    val qtd = resenhasAprovadas.size
                    val media = if (qtd > 0) resenhasAprovadas.map { it.nota }.average() else 0.0
                    
                    supabase.from("livros").update(LivroEstatisticasUpdate(
                        nota_media = media,
                        qtd_avaliacoes = qtd
                    )) {
                        filter { eq("id", livroId) }
                    }
                }

                _state.value = _state.value.copy(processando = false, concluido = true)
                onSucesso()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    processando = false,
                    erro = e.message ?: "Erro ao aprovar resenha"
                )
            }
        }
    }

    fun rejeitar(motivo: String, onSucesso: () -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(processando = true)
            try {
                supabase.from("resenhas").update(
                    AnaliseResenhaStatusUpdate(
                        status = "Rejeitado",
                        motivo_rejeicao = motivo.trim().ifBlank { null }
                    )
                ) {
                    filter { eq("id", resenhaId) }
                }
                _state.value = _state.value.copy(processando = false, concluido = true)
                onSucesso()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    processando = false,
                    erro = e.message ?: "Erro ao rejeitar resenha"
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
            val horario = if (partes.size > 1) partes[1].take(5) else ""
            "$dia de ${meses[mes.toInt() - 1]} de $ano${if (horario.isNotBlank()) " às $horario" else ""}"
        } catch (e: Exception) { raw }
    }
}

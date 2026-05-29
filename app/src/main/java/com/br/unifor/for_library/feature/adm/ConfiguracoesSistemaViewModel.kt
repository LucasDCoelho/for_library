package com.br.unifor.for_library.feature.adm

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
private data class ConfiguracaoSistemaDb(
    val chave: String,
    val valor: String,
    val descricao: String? = null
)

data class ConfiguracoesSistemaUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val pontosPorLivro: String = "",
    val pontosPorResenha: String = "",
    val salvouComSucesso: Boolean = false,
    val erro: String? = null
)

class ConfiguracoesSistemaViewModel : ViewModel() {

    private val _state = MutableStateFlow(ConfiguracoesSistemaUiState())
    val state: StateFlow<ConfiguracoesSistemaUiState> = _state.asStateFlow()

    init {
        carregarConfiguracoes()
    }

    private fun carregarConfiguracoes() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, erro = null)
            try {
                val configs = supabase
                    .from("configuracoes_sistema")
                    .select()
                    .decodeList<ConfiguracaoSistemaDb>()

                _state.value = _state.value.copy(
                    isLoading = false,
                    pontosPorLivro = configs.find { it.chave == "pontos_por_livro" }?.valor ?: "50",
                    pontosPorResenha = configs.find { it.chave == "pontos_por_resenha" }?.valor ?: "15"
                )
            } catch (e: Exception) {
                // Fallback para defaults se a tabela ainda não foi populada
                _state.value = _state.value.copy(
                    isLoading = false,
                    pontosPorLivro = "50",
                    pontosPorResenha = "15",
                    erro = "Não foi possível carregar as configurações. Usando valores padrão."
                )
            }
        }
    }

    fun atualizarPontosPorLivro(valor: String) {
        if (valor.length <= 5) _state.value = _state.value.copy(pontosPorLivro = valor)
    }

    fun atualizarPontosPorResenha(valor: String) {
        if (valor.length <= 5) _state.value = _state.value.copy(pontosPorResenha = valor)
    }

    fun salvarConfiguracoes() {
        val livro = _state.value.pontosPorLivro.trim().toIntOrNull()
        val resenha = _state.value.pontosPorResenha.trim().toIntOrNull()

        if (livro == null || livro < 0) {
            _state.value = _state.value.copy(erro = "Pontos por livro deve ser um número inteiro válido.")
            return
        }
        if (resenha == null || resenha < 0) {
            _state.value = _state.value.copy(erro = "Pontos por resenha deve ser um número inteiro válido.")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true, erro = null)
            try {
                supabase.from("configuracoes_sistema").upsert(
                    ConfiguracaoSistemaDb(chave = "pontos_por_livro", valor = livro.toString())
                )
                supabase.from("configuracoes_sistema").upsert(
                    ConfiguracaoSistemaDb(chave = "pontos_por_resenha", valor = resenha.toString())
                )
                _state.value = _state.value.copy(isSaving = false, salvouComSucesso = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isSaving = false,
                    erro = "Erro ao salvar. Tente novamente."
                )
            }
        }
    }

    fun consumirSucesso() {
        _state.value = _state.value.copy(salvouComSucesso = false)
    }

    fun consumirErro() {
        _state.value = _state.value.copy(erro = null)
    }
}

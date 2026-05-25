package com.br.unifor.for_library.feature.aluno.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MensagemChat(
    val texto: String,
    val isBot: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class ChatBotState(
    val mensagens: List<MensagemChat> = listOf(
        MensagemChat("Olá! 👋 Sou o assistente da ForLibrary. Como posso te ajudar hoje?", isBot = true)
    ),
    val isDigitando: Boolean = false
)

class ChatBotViewModel : ViewModel() {
    private val _state = MutableStateFlow(ChatBotState())
    val state: StateFlow<ChatBotState> = _state.asStateFlow()

    fun enviarMensagem(texto: String) {
        if (texto.isBlank()) return

        val novaMensagemUsuario = MensagemChat(texto = texto, isBot = false)
        _state.value = _state.value.copy(
            mensagens = _state.value.mensagens + novaMensagemUsuario
        )

        processarRespostaBot(texto)
    }

    private fun processarRespostaBot(pergunta: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isDigitando = true)
            
            // Simulação de delay de processamento
            delay(1500)

            val resposta = quandoPergunta(pergunta)
            val novaMensagemBot = MensagemChat(texto = resposta, isBot = true)
            
            _state.value = _state.value.copy(
                mensagens = _state.value.mensagens + novaMensagemBot,
                isDigitando = false
            )
        }
    }

    private fun quandoPergunta(pergunta: String): String {
        val p = pergunta.lowercase()
        return when {
            p.contains("livro") || p.contains("acervo") -> "Você pode encontrar diversos livros no nosso 'Acervo Digital' na barra inferior!"
            p.contains("ponto") || p.contains("gamificação") -> "Seus pontos aumentam conforme você lê livros e participa de eventos!"
            p.contains("evento") -> "Confira a aba de 'Eventos' para ver os próximos encontros literários."
            p.contains("ajuda") || p.contains("como funciona") -> "Eu posso te ajudar a navegar pelo app, encontrar livros ou entender seus pontos. O que deseja saber?"
            else -> "Interessante! Posso tentar te ajudar com informações sobre o acervo, seus pontos ou eventos. Pode detalhar melhor sua dúvida?"
        }
    }
}

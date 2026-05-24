package com.br.unifor.for_library.feature.aluno.livro.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.br.unifor.for_library.core.components.CapaLivro
import com.br.unifor.for_library.core.designsystem.coresFallback
import com.br.unifor.for_library.feature.aluno.livro.viewmodel.AvaliacaoResenhaViewModel
import com.br.unifor.for_library.feature.aluno.livro.viewmodel.ResultadoAvaliacao
import kotlinx.coroutines.delay

private val AzulPrimario     = Color(0xFF1565C0)
private val AzulDesabilitado = Color(0xFFB0C4DE)
private val VerdeSucesso     = Color(0xFF2E7D32)
private val VerdeFundo       = Color(0xFFE8F5E9)
private val AmareloEstrela   = Color(0xFFFFD700)

@Composable
fun TelaAvaliacaoResenha(
    livroId: String,
    onClose: () -> Unit,
    onCancelar: () -> Unit,
    viewModel: AvaliacaoResenhaViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val livroIdInt = remember(livroId) { livroId.toIntOrNull() ?: 0 }

    LaunchedEffect(livroIdInt) {
        if (livroIdInt > 0) viewModel.carregarLivro(livroIdInt)
    }

    // Fecha automaticamente 2s após o sucesso
    LaunchedEffect(state.resultado) {
        if (state.resultado != ResultadoAvaliacao.NENHUM) {
            delay(2000L)
            onClose()
        }
    }

    var rating  by remember { mutableStateOf(0) }
    var resenha by remember { mutableStateOf("") }
    val maxChars = 500

    val resenhaInvalida = resenha.isNotEmpty() && resenha.length < 20
    val isValido = rating > 0 && !resenhaInvalida && !state.enviando

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Indicador de Bottom Sheet
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .background(Color.LightGray, CircleShape)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(16.dp))

        // ── RF14.1 + RF14.2: Cabeçalho ───────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text("AVALIAÇÃO", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                Text(
                    text = if (state.isLoading) "Carregando..." else state.tituloLivro,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, contentDescription = "Fechar")
            }
        }

        Spacer(Modifier.height(16.dp))

        if (state.isLoading) {
            Box(Modifier.fillMaxWidth().height(80.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AzulPrimario, modifier = Modifier.size(32.dp))
            }
        } else {
            // ── RF14.3: Card do livro ─────────────────────────────────────────
            Surface(
                color = Color(0xFFF9F9F9),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CapaLivro(
                        isbn = state.isbn ?: "",
                        tituloFallback = state.tituloLivro,
                        modifier = Modifier
                            .size(50.dp, 70.dp)
                            .background(Color.LightGray, RoundedCornerShape(4.dp)),
                        corFallback = coresFallback[livroIdInt % coresFallback.size]
                    )
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(state.tituloLivro, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(state.autor, color = Color.DarkGray, fontSize = 13.sp)
                        if (state.anoPublicacao.isNotEmpty()) {
                            Text("Publicado em ${state.anoPublicacao}", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── RF14.4: Estrelas ──────────────────────────────────────────────
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Toque nas estrelas para avaliar", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Row {
                    for (i in 1..5) {
                        Icon(
                            imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = "Estrela $i",
                            tint = if (i <= rating) AmareloEstrela else Color.LightGray,
                            modifier = Modifier
                                .size(48.dp)
                                .clickable { rating = i }
                                .padding(4.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── RF14.5: Campo de resenha ──────────────────────────────────────
            Text("SUA RESENHA", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = resenha,
                onValueChange = { if (it.length <= maxChars) resenha = it },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                placeholder = { Text("Escreva sua resenha sobre o livro...", color = Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedContainerColor   = Color(0xFFF5F5F5),
                    unfocusedBorderColor    = Color.Transparent,
                    focusedBorderColor      = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Mínimo 20 caracteres", fontSize = 11.sp, color = Color.Gray)
                Text("${resenha.length} / $maxChars", fontSize = 11.sp, color = Color.Gray)
            }

            AnimatedVisibility(visible = resenhaInvalida) {
                Text(
                    text = "A resenha deve ter ao menos 20 caracteres",
                    fontSize = 11.sp,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            // ── RF14.6: Botão enviar ──────────────────────────────────────────
            Button(
                onClick = { viewModel.enviarResenha(livroIdInt, rating, resenha) },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                enabled = isValido,
                colors = ButtonDefaults.buttonColors(
                    containerColor         = AzulPrimario,
                    disabledContainerColor = AzulDesabilitado
                ),
                shape = RoundedCornerShape(4.dp)
            ) {
                if (state.enviando) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Enviar Resenha", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // ── RF14.7: Cancelar ──────────────────────────────────────────────
            TextButton(
                onClick = onCancelar,
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("Cancelar", color = Color.Gray)
            }
        }
    }

    // ── Feedback de sucesso ───────────────────────────────────────────────────
    val mensagem = when (state.resultado) {
        ResultadoAvaliacao.MODERACAO  -> "Enviado para moderação!"
        ResultadoAvaliacao.REGISTRADA -> "Avaliação registrada!"
        ResultadoAvaliacao.ERRO       -> "Erro ao enviar. Tente novamente."
        ResultadoAvaliacao.NENHUM     -> null
    }

    if (mensagem != null) {
        Dialog(onDismissRequest = onClose) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = VerdeSucesso,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = mensagem,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TelaAvaliacaoResenhaPreview() {
    TelaAvaliacaoResenha(
        livroId    = "1",
        onClose    = {},
        onCancelar = {}
    )
}

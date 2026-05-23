package com.br.unifor.for_library.feature.aluno.livro.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import kotlinx.coroutines.delay

// ---------------------------------------------------------------------------
// Cores centralizadas â€” futuramente migrar para MaterialTheme.colorScheme
// ---------------------------------------------------------------------------
private val AzulPrimario     = Color(0xFF1565C0)
private val AzulDesabilitado = Color(0xFFB0C4DE)
private val VerdeSucesso     = Color(0xFF2E7D32)
private val VerdeFundo       = Color(0xFFE8F5E9)
private val AmareloEstrela   = Color(0xFFFFD700)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAvaliacaoResenha(
    tituloLivro: String = "O Pequeno PrÃ­ncipe",
    autor: String = "Antoine de Saint-ExupÃ©ry",
    anoPublicacao: String = "1943",
    onClose: () -> Unit,
    onCancelar: () -> Unit
) {
    var rating   by remember { mutableStateOf(0) }
    var resenha  by remember { mutableStateOf("") }
    var mensagemSucesso by remember { mutableStateOf<String?>(null) }

    val maxChars = 500

    // Regra de validaÃ§Ã£o: nota obrigatÃ³ria; resenha opcional, mas se preenchida
    // precisa ter ao menos 20 caracteres.
    val resenhaInvalida = resenha.isNotEmpty() && resenha.length < 20
    val isValido = rating > 0 && !resenhaInvalida

    // CORREÃ‡ÃƒO RF14.6: apÃ³s exibir o feedback de sucesso, navega automaticamente
    // para fora da tela apÃ³s 2 segundos, evitando que o usuÃ¡rio fique preso.
    LaunchedEffect(mensagemSucesso) {
        if (mensagemSucesso != null) {
            delay(2000L)
            onClose()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Indicador visual de Bottom Sheet
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .background(Color.LightGray, CircleShape)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // â”€â”€ RF14.1 e RF14.2: CabeÃ§alho â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                // CORREÃ‡ÃƒO: fontSize aumentado de 10.sp para 12.sp (mÃ­nimo legÃ­vel)
                Text(
                    text = "AVALIAÇÃO",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = tituloLivro,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, contentDescription = "Fechar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // â”€â”€ RF14.3: Card do livro â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        // CORREÃ‡ÃƒO: o card exibia apenas o autor em negrito, omitindo o tÃ­tulo.
        // A ordem correta Ã©: tÃ­tulo â†’ autor â†’ ano de publicaÃ§Ã£o.
        Surface(
            color = Color(0xFFF9F9F9),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // TODO: substituir por AsyncImage quando a URL da capa estiver disponÃ­vel
                Box(
                    modifier = Modifier
                        .size(50.dp, 70.dp)
                        .background(Color.LightGray, RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(tituloLivro, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(autor, color = Color.DarkGray, fontSize = 13.sp)
                    Text("Publicado em $anoPublicacao", color = Color.Gray, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // â”€â”€ RF14.4: Sistema de avaliaÃ§Ã£o por estrelas â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Toque nas estrelas para avaliar",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                for (i in 1..5) {
                    Icon(
                        imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = "Estrela $i",
                        tint = if (i <= rating) AmareloEstrela else Color.LightGray,
                        modifier = Modifier
                            // CORREÃ‡ÃƒO: clickable aplicado antes do padding para que
                            // a Ã¡rea de toque seja 48dp (mÃ­nimo Material Design),
                            // e nÃ£o 32dp como estava antes.
                            .size(48.dp)
                            .clickable { rating = i }
                            .padding(4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // â”€â”€ RF14.5: Campo de resenha (opcional, mÃ­nimo 20 caracteres) â”€â”€â”€â”€â”€â”€â”€â”€
        // CORREÃ‡ÃƒO: fontSize aumentado de 10.sp para 12.sp (mÃ­nimo legÃ­vel)
        Text("SUA RESENHA", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = resenha,
            onValueChange = { novoTexto ->
                if (novoTexto.length <= maxChars) resenha = novoTexto
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = {
                Text("Escreva sua resenha sobre o livro...", color = Color.Gray)
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF5F5F5),
                focusedContainerColor   = Color(0xFFF5F5F5),
                unfocusedBorderColor    = Color.Transparent,
                focusedBorderColor      = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Minimo 20 caracteres", fontSize = 11.sp, color = Color.Gray)
            Text("${resenha.length} / $maxChars", fontSize = 11.sp, color = Color.Gray)
        }

        // CORREÃ‡ÃƒO RF14.5: mensagem de erro visÃ­vel quando a resenha foi iniciada
        // mas ainda nÃ£o atingiu o mÃ­nimo de 20 caracteres. Antes o botÃ£o ficava
        // desabilitado silenciosamente, confundindo o usuÃ¡rio.
        AnimatedVisibility(visible = resenhaInvalida) {
            Text(
                text = "A resenha deve ter ao menos 20 caracteres",
                fontSize = 11.sp,
                color = Color.Red,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // â”€â”€ RF14.6: BotÃ£o "Enviar Resenha" â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        Button(
            onClick = {
                mensagemSucesso = "Avaliação registrada"
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = isValido,
            colors = ButtonDefaults.buttonColors(
                containerColor         = AzulPrimario,
                disabledContainerColor = AzulDesabilitado
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Enviar Resenha", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // â”€â”€ RF14.7: BotÃ£o "Cancelar" â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        TextButton(
            onClick = onCancelar,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Cancelar", color = Color.Gray)
        }

        // â”€â”€ Feedback de sucesso em Popup (Checkout Verde) ──────────────────────
        if (mensagemSucesso != null) {
            Dialog(onDismissRequest = { onClose() }) {
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
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Avaliação registrada",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TelaAvaliacaoResenhaPreview() {
    TelaAvaliacaoResenha(
        onClose    = {},
        onCancelar = {}
    )
}


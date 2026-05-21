package com.br.unifor.for_library.feature.aluno.home.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.br.unifor.for_library.core.designsystem.AzulPrimario

private data class MensagemChat(val texto: String, val isBot: Boolean)

@Composable
fun ChatBotAluno(modifier: Modifier = Modifier) {
    var aberto by remember { mutableStateOf(false) }
    var textoUsuario by remember { mutableStateOf("") }
    val mensagens = remember {
        mutableStateListOf(
            MensagemChat("Olá! 👋 Sou o assistente da ForLibrary. Como posso te ajudar hoje?", isBot = true)
        )
    }
    val listaState = rememberLazyListState()

    LaunchedEffect(mensagens.size) {
        if (mensagens.isNotEmpty()) listaState.animateScrollToItem(mensagens.size - 1)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        AnimatedVisibility(
            visible = aberto,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            Card(
                modifier = Modifier
                    .width(280.dp)
                    .height(400.dp),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {

                    // ── Header ──────────────────────────────────────────────────
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AzulPrimario)
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🤖", fontSize = 20.sp)
                        Spacer(Modifier.width(8.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                "Assistente ForLibrary",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text("Online", color = Color.White.copy(alpha = 0.75f), fontSize = 11.sp)
                        }
                        IconButton(
                            onClick = { aberto = false },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Fechar chat",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // ── Mensagens ────────────────────────────────────────────────
                    LazyColumn(
                        state = listaState,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(mensagens) { msg -> BolhaMensagem(msg) }
                    }

                    HorizontalDivider(color = Color(0xFFEEEEEE))

                    // ── Input ────────────────────────────────────────────────────
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = textoUsuario,
                            onValueChange = { textoUsuario = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Digite sua dúvida...", fontSize = 12.sp) },
                            maxLines = 2,
                            shape = RoundedCornerShape(20.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(AzulPrimario)
                                .clickable { /* envio real em breve */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("➤", fontSize = 14.sp, color = Color.White)
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // ── Botão flutuante ──────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(AzulPrimario)
                .clickable { aberto = !aberto },
            contentAlignment = Alignment.Center
        ) {
            Text("🤖", fontSize = 26.sp)
        }
    }
}

@Composable
private fun BolhaMensagem(mensagem: MensagemChat) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (mensagem.isBot) Arrangement.Start else Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 200.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = if (mensagem.isBot) 2.dp else 12.dp,
                        bottomEnd = if (mensagem.isBot) 12.dp else 2.dp
                    )
                )
                .background(if (mensagem.isBot) Color(0xFFE3EBF6) else AzulPrimario)
                .padding(horizontal = 10.dp, vertical = 7.dp)
        ) {
            Text(
                text = mensagem.texto,
                color = if (mensagem.isBot) Color(0xFF212121) else Color.White,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

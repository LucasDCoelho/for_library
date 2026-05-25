package com.br.unifor.for_library.feature.aluno.livro.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

private val AzulPrimario = Color(0xFF1565C0)
private val VerdeSucesso = Color(0xFF2E7D32)
private val VerdeEscuro  = Color(0xFF1B5E20)
private val VerdeFundo   = Color(0xFFE8F5E9)

@Composable
fun PopupFimLeitura(
    pontosGanhos: Int = 50,
    onAvaliarLivro: () -> Unit,
    onFechar: () -> Unit
) {
    Dialog(onDismissRequest = onFechar) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // ── RF11.2 + RF11.5: Ícone do livro com badge "Leitura finalizada" ──
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(Color(0xFFF5F8FF), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = "Livro Concluído",
                        tint = AzulPrimario,
                        modifier = Modifier.size(64.dp)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 8.dp, y = (-8).dp)
                            .size(32.dp)
                            .background(VerdeEscuro, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                // ── RF11.5: Label "Leitura finalizada" abaixo do ícone ──────────────
                Surface(
                    color = VerdeFundo,
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = VerdeSucesso,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = "Leitura finalizada",
                            color = VerdeSucesso,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                // ── RF11.2: Mensagem de parabéns ────────────────────────────────────
                Text(
                    text = "Parabéns!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = buildAnnotatedString {
                        append("Você concluiu a leitura e ganhou ")
                        withStyle(SpanStyle(color = AzulPrimario, fontWeight = FontWeight.Bold)) {
                            append("$pontosGanhos pontos")
                        }
                        append(".")
                    },
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(24.dp))

                // ── RF11.3: Botão "Avaliar Livro" ────────────────────────────────────
                Button(
                    onClick = onAvaliarLivro,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario)
                ) {
                    Text("Avaliar Livro", fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(8.dp))

                // ── RF11.4: Botão "Fechar" ────────────────────────────────────────────
                OutlinedButton(
                    onClick = onFechar,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Fechar", color = Color.DarkGray, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PopupFimLeituraPreview() {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        PopupFimLeitura(pontosGanhos = 50, onAvaliarLivro = {}, onFechar = {})
    }
}

// ── PopupExclusaoObra.kt (atualizado — reutilizável) ─────────────────────────
package com.br.unifor.for_library.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun PopupExclusaoObra(
    // ✅ Fix 2: parâmetro de mensagem — elimina a necessidade de PopupExclusaoEvento
    mensagem: String = "Atenção: A exclusão removerá a obra das estantes " +
            "de todos os alunos. Deseja continuar?",
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = { /* bloqueado — ação destrutiva */ }) {
        Card(
            shape = RoundedCornerShape(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {

                Text(
                    text = "Confirmar Exclusão",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )

                HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)

                // ✅ Fix 2: usa o parâmetro de mensagem
                Text(
                    text = mensagem,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(16.dp),
                    lineHeight = 20.sp
                )

                HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cancelar",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    VerticalDivider(
                        color = Color(0xFFEEEEEE),
                        thickness = 1.dp,
                        modifier = Modifier.fillMaxHeight()
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(bottomEnd = 4.dp))
                            .background(Color(0xFFD32F2F))
                            .clickable { onConfirm() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Excluir",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PopupExclusaoObraPreview() {
    PopupExclusaoObra(onDismiss = {}, onConfirm = {})
}
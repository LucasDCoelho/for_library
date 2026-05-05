package com.br.unifor.for_library.feature.adm.moderacao

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.br.unifor.for_library.core.designsystem.AzulPrimario
import com.br.unifor.for_library.core.designsystem.CinzaTexto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopupDetalhesUsuario(
    usuario: UsuarioMock,
    onFechar: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var bloqueado by remember { mutableStateOf(!usuario.ativo) }

    ModalBottomSheet(
        onDismissRequest = onFechar,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        ) {
            // RF39.2: Título
            Text(
                "Detalhes do Usuário",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )

            Spacer(Modifier.height(16.dp))

            // RF39.2: Dados Pessoais
            Text(
                "DADOS PESSOAIS",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = CinzaTexto,
                letterSpacing = 0.6.sp
            )

            Spacer(Modifier.height(10.dp))

            CampoDado(label = "Nome Completo", valor = usuario.nome)
            Spacer(Modifier.height(8.dp))
            CampoDado(label = "Matrícula", valor = usuario.matricula)
            Spacer(Modifier.height(8.dp))
            CampoDado(label = "E-mail Institucional", valor = usuario.email)

            Spacer(Modifier.height(16.dp))

            // RF39.5: Card de Alerta — Status de Moderação
            if (usuario.resenhasInadequadas > 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFDEAEA)),
                    border = BorderStroke(1.dp, Color(0xFFF8C9C9)),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(
                                "Status de Moderação",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFFB71C1C)
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                "Total de resenhas inadequadas: ${usuario.resenhasInadequadas}",
                                fontSize = 12.sp,
                                color = Color(0xFF424242)
                            )
                            if (usuario.resenhasInadequadas >= 3) {
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "O usuário atingiu o limite de alertas automáticos do sistema.",
                                    fontSize = 11.sp,
                                    color = CinzaTexto
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // RF39.3: Toggle Bloquear Acesso
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        "Bloquear Acesso à Plataforma",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF212121)
                    )
                    Text(
                        "Impede login e interações no acervo",
                        fontSize = 11.sp,
                        color = CinzaTexto
                    )
                }
                Switch(
                    checked = bloqueado,
                    onCheckedChange = { bloqueado = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = AzulPrimario,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFBDBDBD)
                    )
                )
            }

            Spacer(Modifier.height(20.dp))

            // RF39.4: Botão Fechar
            OutlinedButton(
                onClick = onFechar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, AzulPrimario)
            ) {
                Text("Fechar", color = AzulPrimario, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun CampoDado(label: String, valor: String) {
    Column {
        Text(label, fontSize = 11.sp, color = CinzaTexto)
        Spacer(Modifier.height(2.dp))
        Text(valor, fontSize = 13.sp, color = Color(0xFF212121))
    }
}

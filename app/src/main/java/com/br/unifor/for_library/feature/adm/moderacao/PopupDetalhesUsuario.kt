package com.br.unifor.for_library.feature.adm.moderacao

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
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
import com.br.unifor.for_library.core.designsystem.CinzaTexto
import com.br.unifor.for_library.core.designsystem.VermelhoErro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopupDetalhesUsuario(
    usuario: UsuarioAdminItem,
    onFechar: () -> Unit,
    onAlterarStatus: (bloqueado: Boolean) -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val snackbarHostState = remember { SnackbarHostState() }

    // Estado local sincronizado com o item passado — atualiza quando o pai reflete a mudança
    var bloqueado by remember(usuario.bloqueado) { mutableStateOf(usuario.bloqueado) }
    var salvando by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onFechar,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        // Gate 1: Snackbar de feedback de erro dentro do sheet
        SnackbarHost(snackbarHostState, modifier = Modifier.padding(horizontal = 16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // ── RF39.1: Cabeçalho ──────────────────────────────────────────────
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE3EEF9)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = AzulPrimario,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        usuario.nome,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121)
                    )
                    Text(
                        "Matrícula: ${usuario.matricula}",
                        fontSize = 12.sp,
                        color = CinzaTexto
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFEEEEEE))
            Spacer(Modifier.height(16.dp))

            // ── Dados pessoais ────────────────────────────────────────────────
            SectionLabel("DADOS PESSOAIS")
            Spacer(Modifier.height(10.dp))
            CampoDado(label = "Nome Completo", valor = usuario.nome)
            Spacer(Modifier.height(8.dp))
            CampoDado(label = "Matrícula", valor = usuario.matricula)
            if (usuario.email.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                CampoDado(label = "E-mail Institucional", valor = usuario.email)
            }

            // ── RF39.5: Card de alerta — resenhas inadequadas ─────────────────
            if (usuario.resenhasInadequadas > 0) {
                Spacer(Modifier.height(16.dp))
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
                            tint = VermelhoErro,
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
                            // Aviso de limite automático quando >= 3
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
            }

            Spacer(Modifier.height(16.dp))

            // ── RF39.3: Toggle bloquear/desbloquear ───────────────────────────
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
                if (salvando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = AzulPrimario,
                        strokeWidth = 2.dp
                    )
                } else {
                    Switch(
                        checked = bloqueado,
                        onCheckedChange = { novo ->
                            salvando = true
                            bloqueado = novo
                            onAlterarStatus(novo)
                            salvando = false
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = VermelhoErro,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFFBDBDBD)
                        )
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── RF39.4: Fechar ────────────────────────────────────────────────
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
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = CinzaTexto,
        letterSpacing = 0.6.sp
    )
}

@Composable
private fun CampoDado(label: String, valor: String) {
    Column {
        Text(label, fontSize = 11.sp, color = CinzaTexto)
        Spacer(Modifier.height(2.dp))
        Text(valor, fontSize = 13.sp, color = Color(0xFF212121))
    }
}

package com.br.unifor.for_library.feature.adm.moderacao

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.br.unifor.for_library.core.designsystem.AzulPrimario

@Composable
fun TelaListaModeracaoAdmin(
    onModeracaoUsuarios: () -> Unit = {},
    onModeracaoObras: () -> Unit = {},
    onModeracaoResenhas: () -> Unit = {},
    viewModel: HubModeracaoViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(24.dp))

        Text(
            text = "Moderação",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121)
        )
        Text(
            text = "Central de revisão e gestão do sistema",
            fontSize = 13.sp,
            color = Color(0xFF757575)
        )

        Spacer(Modifier.height(24.dp))

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AzulPrimario)
            }
        } else {
            CardModeracao(
                icone = Icons.Default.ManageAccounts,
                titulo = "Moderação de Usuários",
                subtitulo = "${state.totalUsuarios} aluno${if (state.totalUsuarios != 1) "s" else ""} cadastrado${if (state.totalUsuarios != 1) "s" else ""}",
                corIcone = Color(0xFF1565C0),
                fundoIcone = Color(0xFFE3EEF9),
                onClick = onModeracaoUsuarios
            )

            Spacer(Modifier.height(12.dp))

            CardModeracao(
                icone = Icons.Default.MenuBook,
                titulo = "Moderação de Obras",
                subtitulo = if (state.obrasPendentes == 0) "Nenhuma obra pendente"
                            else "${state.obrasPendentes} obra${if (state.obrasPendentes != 1) "s" else ""} pendente${if (state.obrasPendentes != 1) "s" else ""}",
                corIcone = Color(0xFF2E7D32),
                fundoIcone = Color(0xFFE8F5E9),
                onClick = onModeracaoObras
            )

            Spacer(Modifier.height(12.dp))

            CardModeracao(
                icone = Icons.Default.RateReview,
                titulo = "Moderação de Resenhas",
                subtitulo = if (state.resenhasPendentes == 0) "Nenhuma resenha pendente"
                            else "${state.resenhasPendentes} resenha${if (state.resenhasPendentes != 1) "s" else ""} pendente${if (state.resenhasPendentes != 1) "s" else ""}",
                corIcone = Color(0xFFD32F2F),
                fundoIcone = Color(0xFFFFF5F5),
                onClick = onModeracaoResenhas
            )
        }
    }
}

@Composable
private fun CardModeracao(
    icone: ImageVector,
    titulo: String,
    subtitulo: String,
    corIcone: Color,
    fundoIcone: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = RoundedCornerShape(10.dp),
                color = fundoIcone
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icone,
                        contentDescription = null,
                        tint = corIcone,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF212121)
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = subtitulo,
                    fontSize = 12.sp,
                    color = Color(0xFF757575)
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFFBDBDBD),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

package com.br.unifor.for_library.feature.adm.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.br.unifor.for_library.core.designsystem.AzulPrimario
import com.br.unifor.for_library.core.designsystem.CinzaTexto
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private data class CardResumo(
    val titulo: String,
    val valor: String,
    val icone: ImageVector,
    val corFundo: Color,
    val corIcone: Color,
    val temAlerta: Boolean = false,
    val corAlerta: Color = Color.Transparent
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDashboardAdmin(
    viewModel: AdminDashboardViewModel = viewModel(),
    onSinoClick: () -> Unit = {},
    onConfiguracoesClick: () -> Unit = {},
    onVerTodasAtividades: () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()
    var expandido by remember { mutableStateOf(false) }

    val resumos = listOf(
        CardResumo("LIVROS NO ACERVO", state.totalLivros.toString(), Icons.Default.MenuBook, Color(0xFFE3F2FD), Color(0xFF1976D2)),
        CardResumo("ALUNOS ATIVOS", state.alunosAtivos.toString(), Icons.Default.People, Color(0xFFE3F2FD), Color(0xFF1976D2)),
        CardResumo("RESENHAS PENDENTES", state.resenhasPendentes.toString(), Icons.Default.RateReview, Color(0xFFFDEAEA), Color(0xFFD32F2F), temAlerta = state.resenhasPendentes > 0, corAlerta = Color(0xFFD32F2F)),
        CardResumo("OBRAS PENDENTES", state.obrasPendentes.toString(), Icons.Default.Book, Color(0xFFE6F4EA), Color(0xFF388E3C), temAlerta = state.obrasPendentes > 0, corAlerta = Color(0xFF388E3C)),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Header (RF26.1) ───────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Olá, Admin!",
                fontSize = 14.sp,
                color = Color(0xFF424242),
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onConfiguracoesClick) {
                Icon(Icons.Default.Settings, contentDescription = "Configurações", tint = Color(0xFF424242))
            }
            IconButton(onClick = onSinoClick) {
                Icon(Icons.Default.Notifications, contentDescription = "Notificações", tint = Color(0xFF424242))
            }
        }

        // ── Título (RF26.2) ───────────────────────────────────────────────────
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text("Dashboard", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
            Spacer(Modifier.height(2.dp))
            Text(
                "Visão geral do sistema e métricas principais",
                fontSize = 12.sp,
                color = CinzaTexto
            )
        }

        Spacer(Modifier.height(16.dp))

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AzulPrimario)
            }
        } else {
            // ── 4 Cards de Resumo (RF26.3) ────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                resumos.chunked(2).forEach { linha ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        linha.forEach { resumo ->
                            CardResumoItem(resumo = resumo, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Atividades Recentes (RF26.4 + RF26.5) ─────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Atividades Recentes", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF212121))
                if (state.atividades.size > 4) {
                    Text(
                        text = if (expandido) "Ver menos" else "Ver todas",
                        fontSize = 13.sp,
                        color = AzulPrimario,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { expandido = !expandido }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (state.atividades.isEmpty()) {
                    Text(
                        "Nenhuma atividade recente encontrada.",
                        fontSize = 12.sp,
                        color = CinzaTexto,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                } else {
                    val atividadesExibidas = if (expandido) state.atividades else state.atividades.take(4)
                    atividadesExibidas.forEach { atividade ->
                        ItemAtividade(
                            titulo = atividade.titulo_atividade,
                            descricao = buildString {
                                if (atividade.livro_nome != null) {
                                    append(atividade.livro_nome)
                                    if (!atividade.autor_nome.isNullOrEmpty()) {
                                        append(" - ")
                                        append(atividade.autor_nome)
                                    }
                                }
                            },
                            tempo = formatarDataAtividade(atividade.data_atividade),
                            icone = mapearIcone(atividade.icone_referencia)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun CardResumoItem(
    resumo: CardResumo,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = resumo.corFundo),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = resumo.icone,
                    contentDescription = null,
                    tint = resumo.corIcone,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.weight(1f))
                if (resumo.temAlerta) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(resumo.corAlerta)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = resumo.titulo,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = CinzaTexto,
                letterSpacing = 0.4.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = resumo.valor,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )
        }
    }
}

private fun mapearIcone(referencia: String?): ImageVector {
    return when (referencia) {
        "check" -> Icons.Default.AssignmentTurnedIn
        "add" -> Icons.Default.LibraryAdd
        "cancel" -> Icons.Default.Cancel
        "person" -> Icons.Default.PersonAdd
        else -> Icons.Default.Notifications
    }
}

private fun formatarDataAtividade(dataIso: String): String {
    return try {
        val data = ZonedDateTime.parse(dataIso)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.getDefault())
        data.format(formatter)
    } catch (e: Exception) {
        dataIso
    }
}

@Composable
private fun ItemAtividade(
    titulo: String,
    descricao: String,
    tempo: String,
    icone: ImageVector
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE3F2FD)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icone,
                    contentDescription = null,
                    tint = AzulPrimario,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(titulo, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color(0xFF212121))
                if (descricao.isNotEmpty()) {
                    Spacer(Modifier.height(2.dp))
                    Text(descricao, fontSize = 11.sp, color = CinzaTexto)
                }
                Spacer(Modifier.height(2.dp))
                Text(tempo, fontSize = 10.sp, color = Color(0xFF9E9E9E))
            }
        }
    }
}

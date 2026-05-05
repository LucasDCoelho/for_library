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
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.br.unifor.for_library.core.designsystem.AzulPrimario
import com.br.unifor.for_library.core.designsystem.CinzaTexto

private data class CardResumo(
    val titulo: String,
    val valor: String,
    val icone: ImageVector,
    val corFundo: Color,
    val corIcone: Color,
    val temAlerta: Boolean = false,
    val corAlerta: Color = Color.Transparent
)

private data class AtividadeRecente(
    val titulo: String,
    val descricao: String,
    val tempo: String,
    val icone: ImageVector,
    val corIcone: Color
)

private val mockResumos = listOf(
    CardResumo("LIVROS NO ACERVO", "1.240", Icons.Default.MenuBook, Color(0xFFE3F2FD), Color(0xFF1976D2)),
    CardResumo("ALUNOS ATIVOS",    "850",   Icons.Default.People,   Color(0xFFE3F2FD), Color(0xFF1976D2)),
    CardResumo("RESENHAS PENDENTES", "12",  Icons.Default.RateReview, Color(0xFFFDEAEA), Color(0xFFD32F2F), temAlerta = true, corAlerta = Color(0xFFD32F2F)),
    CardResumo("OBRAS PENDENTES",    "05",  Icons.Default.Book,    Color(0xFFE6F4EA), Color(0xFF388E3C), temAlerta = true, corAlerta = Color(0xFF388E3C)),
)

private val mockAtividades = listOf(
    AtividadeRecente("Resenha aprovada",    "\"O Senhor dos Anéis\" - Por Marina Silva", "Há 15 minutos", Icons.Default.AssignmentTurnedIn, Color(0xFF1976D2)),
    AtividadeRecente("Nova obra cadastrada","\"A Metamorfose\" - Edição Digital",         "Há 4 horas",    Icons.Default.LibraryAdd,         Color(0xFF1976D2)),
    AtividadeRecente("Obra rejeitada",      "Título duplicado no catálogo",                "Há 1 dia",      Icons.Default.Cancel,             Color(0xFF1976D2)),
    AtividadeRecente("Novo aluno registrado","João Pedro - Matrícula #851",                "Há 2 dias",     Icons.Default.PersonAdd,          Color(0xFF1976D2)),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDashboardAdmin(
    nomeAdmin: String = "Admin",
    onSinoClick: () -> Unit = {},
    onVerTodasAtividades: () -> Unit = {},
) {
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
                text = "Olá, $nomeAdmin!",
                fontSize = 14.sp,
                color = Color(0xFF424242),
                modifier = Modifier.weight(1f)
            )
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

        // ── 4 Cards de Resumo (RF26.3) ────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            mockResumos.chunked(2).forEach { linha ->
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
            Text(
                "Ver todas",
                fontSize = 13.sp,
                color = AzulPrimario,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onVerTodasAtividades() }
            )
        }

        Spacer(Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            mockAtividades.forEach { ItemAtividade(atividade = it) }
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

@Composable
private fun ItemAtividade(atividade: AtividadeRecente) {
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
                    imageVector = atividade.icone,
                    contentDescription = null,
                    tint = atividade.corIcone,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(atividade.titulo, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color(0xFF212121))
                Spacer(Modifier.height(2.dp))
                Text(atividade.descricao, fontSize = 11.sp, color = CinzaTexto)
                Spacer(Modifier.height(2.dp))
                Text(atividade.tempo, fontSize = 10.sp, color = Color(0xFF9E9E9E))
            }
        }
    }
}

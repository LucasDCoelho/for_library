package com.br.unifor.for_library.feature.adm.moderacao.modresenha

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AssignmentLate
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.br.unifor.for_library.core.designsystem.AzulPrimario

data class ResenhaMock(
    val id: String,
    val nome: String,
    val livro: String,
    val estrelas: Int,
    val texto: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaModeracaoResenhas(
    onVoltar: () -> Unit,
    onResenhaClick: (String) -> Unit
) {
    val resenhasPendentes = listOf(
        ResenhaMock("1", "Ana Silva", "The Great Gatsby", 5, "A resenha explora profundamente a decadência do sonho americano através dos olhos de Nick..."),
        ResenhaMock("2", "Bruno Oliveira", "1984 - George Orwell", 4, "Impactante e ainda muito atual. A forma como o Big Brother controla a sociedade é assustadora...."),
        ResenhaMock("3", "Carla Mendes", "Dom Casmurro", 5, "A dúvida de Bentinho continua sendo um dos maiores mistérios da literatura brasileira. Machad..."),
        ResenhaMock("4", "Daniel Rocha", "O Hobbit", 3, "Uma aventura clássica que introduz o mundo de Tolkien de forma leve e divertida. A jornada de Bil...")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Moderação de Resenhas", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            
            // ── SEÇÃO STATUS (Conforme a imagem) ──────────────────────────
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(
                    text = "STATUS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AssignmentLate,
                        contentDescription = null,
                        tint = AzulPrimario,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "12 Pending Reviews",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                }
            }

            HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(resenhasPendentes) { resenha ->
                    ItemResenhaPendente(resenha = resenha, onClick = { onResenhaClick(resenha.id) })
                }

                // ── BOTÃO CARREGAR MAIS (Conforme a imagem) ──────────────────
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = { /* Lógica para carregar mais */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(1.dp, AzulPrimario),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AzulPrimario)
                    ) {
                        Text(
                            text = "CARREGAR MAIS RESENHAS",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            letterSpacing = 1.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun ItemResenhaPendente(resenha: ResenhaMock, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7)),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = resenha.nome, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Box(
                    modifier = Modifier
                        .background(Color(0xFFD0E4FF), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "PENDING",
                        color = Color(0xFF0056B3),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Text(text = resenha.livro, fontSize = 13.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                repeat(5) { index ->
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (index < resenha.estrelas) AzulPrimario else Color.LightGray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = resenha.texto,
                fontSize = 14.sp,
                color = Color.DarkGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 20.sp
            )
        }
    }
}

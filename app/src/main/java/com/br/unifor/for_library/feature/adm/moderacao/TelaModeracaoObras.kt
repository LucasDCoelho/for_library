package com.br.unifor.for_library.feature.adm.moderacao

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ObraPendenteMock(
    val id: String,
    val titulo: String,
    val genero: String,
    val autor: String,
    val matriculaAluno: String
)

private val mockObrasPendentes = listOf(
    ObraPendenteMock("1", "A Jornada Digital",   "Tecnologia",       "Ricardo Lima",    "2190333-9"),
    ObraPendenteMock("2", "O Eco das Sombras",   "Suspense",         "Beatriz Soares",  "2510887-5"),
    ObraPendenteMock("3", "Raízes do Amanhã",    "Ficção Científica","Marcos Vinicius", "2370112-7"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaModeracaoObras(
    onVoltar: () -> Unit = {},
    onRevisarObra: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Moderação de Obras",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ── Cabeçalho "Submissões Recentes" ──────────────────────────────
            item {
                Text(
                    text = "Moderação de Obras",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF616161)
                )
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Submissões ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF212121)
                    )
                    Text(
                        text = "Recentes",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1565C0)
                    )
                }
                Text(
                    text = "AGUARDANDO REVISÃO EDITORIAL",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF9E9E9E),
                    letterSpacing = 0.5.sp
                )
                Spacer(Modifier.height(8.dp))
                HorizontalDivider(color = Color(0xFFE0E0E0))
                Spacer(Modifier.height(4.dp))
            }

            // ── Cards das obras ───────────────────────────────────────────────
            // ✅ key estável: evita recomposição desnecessária nos itens
            items(mockObrasPendentes, key = { it.id }) { obra ->
                CardObraPendente(obra = obra, onRevisar = { onRevisarObra(obra.id) })
            }

            // ── Card de resumo total pendente ─────────────────────────────────
            item {
                Spacer(Modifier.height(4.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.LibraryBooks,
                                contentDescription = null,
                                tint = Color(0xFF757575),
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "${mockObrasPendentes.size}",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF212121)
                            )
                            Text(
                                text = "TOTAL PENDENTE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF9E9E9E),
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CardObraPendente(obra: ObraPendenteMock, onRevisar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            // ── Badge + Matrícula ─────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFFFF3CD), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "PENDENTE",
                        color = Color(0xFF856404),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Matrícula do aluno: ${obra.matriculaAluno}",
                    fontSize = 10.sp,
                    color = Color(0xFF9E9E9E)
                )
            }

            Spacer(Modifier.height(8.dp))

            // ── Título ────────────────────────────────────────────────────────
            Text(
                text = obra.titulo,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )

            Spacer(Modifier.height(10.dp))

            // ── Gênero / Autor ────────────────────────────────────────────────
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "GÊNERO",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF9E9E9E),
                        letterSpacing = 0.4.sp
                    )
                    Text(
                        text = obra.genero,
                        fontSize = 13.sp,
                        color = Color(0xFF424242)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "AUTOR",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF9E9E9E),
                        letterSpacing = 0.4.sp
                    )
                    Text(
                        text = obra.autor,
                        fontSize = 13.sp,
                        color = Color(0xFF424242)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // ── Botão Revisar ─────────────────────────────────────────────────
            Button(
                onClick = onRevisar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
            ) {
                Icon(
                    imageVector = Icons.Default.RemoveRedEye,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "REVISAR",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


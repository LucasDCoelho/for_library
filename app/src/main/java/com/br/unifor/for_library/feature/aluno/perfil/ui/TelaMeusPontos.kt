package com.br.unifor.for_library.feature.aluno.perfil.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AzulPrimario = Color(0xFF1565C0)

private data class EntradaPontos(
    val icone: ImageVector,
    val descricao: String,
    val data: String,
    val pontos: String,
    val positivo: Boolean = true
)

private val mockHistorico = listOf(
    EntradaPontos(Icons.Default.AutoStories, "+50 pontos por concluir livro X", "14 Out 2023", "+50"),
    EntradaPontos(Icons.Default.RateReview, "Resenha aprovada", "08 Out 2023", "+15"),
    EntradaPontos(Icons.Default.AutoStories, "+50 pontos por concluir livro Y", "02 Out 2023", "+50"),
    EntradaPontos(Icons.Default.RateReview, "Resenha aprovada", "25 Set 2023", "+15"),
    EntradaPontos(Icons.Default.AutoStories, "+50 pontos por concluir livro Z", "18 Set 2023", "+50"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaMeusPontos(
    onVoltar: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Pontos", fontWeight = FontWeight.SemiBold, fontSize = 17.sp) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF5F7FA)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Card de pontos totais
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "2.500",
                        fontSize = 52.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulPrimario
                    )
                    Text("PTS", fontSize = 13.sp, color = Color(0xFF9E9E9E),
                        fontWeight = FontWeight.Bold, letterSpacing = 1.sp)

                    Spacer(Modifier.height(20.dp))

                    // Barra de nível
                    Text("Nível 4: Leitor Especialista", fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold, color = Color(0xFF424242))
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        repeat(5) { i ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(6.dp)
                                    .background(
                                        if (i < 4) AzulPrimario else Color(0xFFE0E0E0),
                                        RoundedCornerShape(50)
                                    )
                            )
                        }
                    }
                }
            }

            // Histórico de Pontos
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text("Histórico de Pontos", fontSize = 16.sp,
                    fontWeight = FontWeight.Bold, color = Color(0xFF212121),
                    modifier = Modifier.padding(bottom = 10.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column {
                        mockHistorico.forEachIndexed { index, entrada ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(Color(0xFFE3F2FD), RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(entrada.icone, contentDescription = null,
                                        tint = AzulPrimario, modifier = Modifier.size(20.dp))
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(entrada.descricao, fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium, color = Color(0xFF212121))
                                    Text(entrada.data, fontSize = 11.sp, color = Color(0xFF9E9E9E))
                                }
                                Text(
                                    text = entrada.pontos,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32)
                                )
                            }
                            if (index < mockHistorico.lastIndex) {
                                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp),
                                    color = Color(0xFFEEEEEE), thickness = 0.5.dp)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Como ganhar pontos
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text("Como ganhar pontos?", fontSize = 16.sp,
                    fontWeight = FontWeight.Bold, color = Color(0xFF212121),
                    modifier = Modifier.padding(bottom = 10.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.Info, contentDescription = null,
                            tint = AzulPrimario, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("Transforme sua leitura em recompensas",
                                fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))
                            Spacer(Modifier.height(4.dp))
                            Text("Você acumula pontos lendo novos títulos, participando de discussões e avaliando suas leituras.",
                                fontSize = 12.sp, color = Color(0xFF424242), lineHeight = 18.sp)
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CardGanho(
                        modifier = Modifier.weight(1f),
                        icone = Icons.Default.AutoStories,
                        titulo = "Ler Livro",
                        pontos = "+50 pts"
                    )
                    CardGanho(
                        modifier = Modifier.weight(1f),
                        icone = Icons.Default.RateReview,
                        titulo = "Resenha Aprovada",
                        pontos = "+15 pts"
                    )
                }
            }

            Spacer(Modifier.height(28.dp))
        }
    }
}

@Composable
private fun CardGanho(
    modifier: Modifier = Modifier,
    icone: ImageVector,
    titulo: String,
    pontos: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icone, contentDescription = null, tint = AzulPrimario, modifier = Modifier.size(28.dp))
            Spacer(Modifier.height(8.dp))
            Text(titulo, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF424242))
            Spacer(Modifier.height(4.dp))
            Text(pontos, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
        }
    }
}


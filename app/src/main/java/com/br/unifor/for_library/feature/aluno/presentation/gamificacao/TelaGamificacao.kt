package com.br.unifor.for_library.feature.aluno.presentation.gamificacao

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoStories
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
import java.util.Locale

private val AzulPrimario = Color(0xFF1565C0)
private val VerdeSucesso = Color(0xFF2E7D32)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaGamificacao(
    onVoltar: () -> Unit,
    viewModel: GamificacaoViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

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
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AzulPrimario)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {
                item { Spacer(Modifier.height(16.dp)) }

                // RF21.2 - Medidor de Nível (Topo)
                item {
                    CardNivel(
                        pontos = state.saldoPontos,
                        nivel = state.nivelAtual,
                        tituloNivel = viewModel.getTituloNivel(state.nivelAtual)
                    )
                }

                item { Spacer(Modifier.height(24.dp)) }

                // RF21.3 - Histórico de Pontos
                item {
                    Text(
                        text = "Histórico de Pontos",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                if (state.historico.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(1.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Nenhum histórico encontrado", color = Color.Gray, fontSize = 14.sp)
                            }
                        }
                    }
                } else {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(1.dp)
                        ) {
                            Column {
                                state.historico.forEachIndexed { index, item ->
                                    ItemHistorico(
                                        historico = item,
                                        isLast = index == state.historico.lastIndex
                                    )
                                    if (index < state.historico.lastIndex) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(horizontal = 16.dp),
                                            color = Color(0xFFEEEEEE),
                                            thickness = 0.5.dp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item { Spacer(Modifier.height(24.dp)) }

                // RF21.4 - Regras (Embaixo, como na tela antiga)
                item {
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

                item { Spacer(Modifier.height(28.dp)) }
            }
        }
    }
}

@Composable
private fun CardNivel(pontos: Int, nivel: Int, tituloNivel: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                text = pointsFormatted(pontos),
                fontSize = 52.sp,
                fontWeight = FontWeight.Bold,
                color = AzulPrimario
            )
            Text(
                "PTS",
                fontSize = 13.sp,
                color = Color(0xFF9E9E9E),
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(Modifier.height(20.dp))

            Text(
                "Nível $nivel: $tituloNivel",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF424242)
            )
            Spacer(Modifier.height(8.dp))
            
            // Representação visual do nível
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(5) { i ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .background(
                                if (i < nivel) AzulPrimario else Color(0xFFE0E0E0),
                                RoundedCornerShape(50)
                            )
                    )
                }
            }
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
            Text(pontos, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = VerdeSucesso)
        }
    }
}

@Composable
private fun ItemHistorico(historico: HistoricoPontos, isLast: Boolean) {
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
            val icon = if (historico.descricao.contains("Resenha", ignoreCase = true)) 
                Icons.Default.RateReview else Icons.Default.AutoStories
            Icon(icon, contentDescription = null, tint = AzulPrimario, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                historico.descricao,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF212121)
            )
            Text(historico.dataFormatada, fontSize = 11.sp, color = Color(0xFF9E9E9E))
        }
        Text(
            text = "+${historico.pontos} pts",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = VerdeSucesso
        )
    }
}

private fun pointsFormatted(points: Int): String {
    return String.format(Locale.forLanguageTag("pt-BR"), "%,d", points)
}

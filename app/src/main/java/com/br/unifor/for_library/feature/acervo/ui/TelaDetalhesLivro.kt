package com.br.unifor.for_library.feature.acervo.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------------------------------------------------------------------------
// Cores centralizadas — futuramente migrar para MaterialTheme.colorScheme
// ---------------------------------------------------------------------------
private val AzulPrimario   = Color(0xFF1565C0)
private val AmareloEstrela = Color(0xFFFFC107)

// ---------------------------------------------------------------------------
// Modelo de dados simulado para as avaliações (Mock)
// TODO: mover para DetalhesLivroViewModel + UiState quando a camada de dados
//       estiver pronta.
// ---------------------------------------------------------------------------
data class Avaliacao(val nome: String, val tempo: String, val nota: Int, val texto: String)

@Composable
fun TelaDetalhesLivro(
    livroId: String,          // Recebemos o ID para no futuro buscar no banco de dados
    onVoltar: () -> Unit,
    onNotificacoes: () -> Unit,
    onLerAgora: () -> Unit
) {
    // Estado para o "Ler mais" da Sinopse (RF09.6)
    var sinopseExpandida by remember { mutableStateOf(false) }

    // CORREÇÃO RF09.5 — estado de favorito controla ícone E cor
    var favoritado by remember { mutableStateOf(false) }

    // Dados simulados
    // TODO: substituir por viewModel.uiState.collectAsStateWithLifecycle()
    val notaMedia    = 4.2f
    val totalAvaliacoes = 1_240

    val avaliacoes = listOf(
        Avaliacao("Mariana Silva",  "Há 2 dias", 5, "Absolutamente fascinante. A construção de mundo é impecável e o final me deixou sem palavras..."),
        Avaliacao("Carlos Eduardo", "Há 5 dias", 4, "Ótimo livro, leitura muito fluída, mas o meio da história é um pouco lento.")
    )

    val sinopseCompleta =
        "Em um futuro onde as viagens interestelares tornaram-se rotina, o capitão Elias Thorne " +
                "descobre uma anomalia nos confins da galáxia de Andrômeda que desafia todas as leis " +
                "conhecidas da física. Enquanto a tripulação da nave 'Vanguard' luta pela sobrevivência, " +
                "segredos ancestrais sobre a origem da humanidade começam a emergir das sombras do espaço profundo."

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // ── RF09.1: Cabeçalho ────────────────────────────────────────────────
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onVoltar) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                }
                Text(
                    text = "Detalhes do livro",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                IconButton(onClick = onNotificacoes) {
                    Icon(Icons.Outlined.Notifications, contentDescription = "Notificações")
                }
            }
        }

        // ── RF09.2: Capa do Livro ─────────────────────────────────────────────
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    // TODO: substituir por AsyncImage (Coil) quando a URL da capa
                    //       estiver disponível no UiState:
                    //       AsyncImage(model = state.capaUrl, contentDescription = "Capa")
                    .background(Color(0xFFE3EEF9)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Outlined.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = AzulPrimario
                    )
                    Text("Capa do Livro", color = AzulPrimario, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // ── RF09.2: Metadados (Gênero, Ano, Título, Autor) ───────────────────
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "GÊNERO: FICÇÃO CIENTÍFICA",
                        color = AzulPrimario,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "Ano: 2023", color = Color.Gray, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "O Horizonte de Eventos",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Text(
                    text = "Autor: Jonathan K. Sterling",
                    color = Color.DarkGray,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // ── RF09.3: Nota Média — CORRIGIDO ────────────────────────────────────
        // Antes: repeat(5) sempre renderizava 5 estrelas cheias.
        // Agora: estrelas preenchidas até o inteiro da nota, restantes em cinza.
        item {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val notaInt = notaMedia.toInt() // 4.2 → 4
                repeat(5) { index ->
                    val tint = if (index < notaInt) AmareloEstrela else Color.LightGray
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = tint,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$notaMedia ($totalAvaliacoes avaliações)",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // ── RF09.4 e RF09.5: Botões de Ação — CORRIGIDO ───────────────────────
        // Antes: ícone de favorito era sempre FavoriteBorder independente do estado.
        // Agora: alterna entre Favorite (preenchido) e FavoriteBorder (contorno).
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onLerAgora,
                    colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Icon(Icons.Outlined.MenuBook, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ler Agora", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))

                // CORREÇÃO RF09.5 — ícone preenchido quando favoritado
                OutlinedIconButton(
                    onClick = { favoritado = !favoritado },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = if (favoritado) Icons.Filled.Favorite
                        else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (favoritado) "Remover dos favoritos"
                        else "Adicionar aos favoritos",
                        tint = if (favoritado) Color.Red else Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(
                color = Color.LightGray,
                thickness = 0.5.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // ── RF09.6: Sinopse com "Ler Mais" ────────────────────────────────────
        item {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .animateContentSize()
            ) {
                Text(
                    text = "SINOPSE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = sinopseCompleta,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    maxLines = if (sinopseExpandida) Int.MAX_VALUE else 4,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (sinopseExpandida) "Ler menos" else "Ler mais",
                    color = AzulPrimario,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { sinopseExpandida = !sinopseExpandida }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(
                color = Color.LightGray,
                thickness = 0.5.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // ── RF09.7: Avaliações de Usuários ────────────────────────────────────
        item {
            Text(
                text = "Avaliações de Usuários",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        items(avaliacoes) { avaliacao ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Avatar circular simulado — inicial do nome
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.DarkGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                avaliacao.nome.first().toString(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = avaliacao.nome,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            // Estrelas da avaliação individual
                            Row {
                                repeat(5) { index ->
                                    val tint = if (index < avaliacao.nota) AmareloEstrela
                                    else Color.LightGray
                                    Icon(
                                        Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = tint,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }
                    }
                    Text(text = avaliacao.tempo, color = Color.Gray, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = avaliacao.texto, fontSize = 14.sp, color = Color.DarkGray)
            }
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

@Preview(showBackground = true)
@Composable
fun TelaDetalhesLivroPreview() {
    TelaDetalhesLivro(
        livroId = "123",
        onVoltar = {},
        onNotificacoes = {},
        onLerAgora = {}
    )
}
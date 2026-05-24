package com.br.unifor.for_library.feature.aluno.livro.ui

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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.BookmarkBorder
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.br.unifor.for_library.core.components.CapaLivro
import com.br.unifor.for_library.core.designsystem.coresFallback
import com.br.unifor.for_library.feature.aluno.livro.viewmodel.DetalhesLivroViewModel
import com.br.unifor.for_library.feature.aluno.livro.viewmodel.ResenhaUi

private val AzulPrimario   = Color(0xFF1565C0)
private val AmareloEstrela = Color(0xFFFFC107)

@Composable
fun TelaDetalhesLivro(
    livroId: String,
    onVoltar: () -> Unit,
    onNotificacoes: () -> Unit,
    onLerAgora: (titulo: String) -> Unit,
    viewModel: DetalhesLivroViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val livroIdInt = remember(livroId) { livroId.toIntOrNull() ?: 0 }

    LaunchedEffect(livroIdInt) {
        if (livroIdInt > 0) viewModel.carregarDetalhes(livroIdInt)
    }

    var sinopseExpandida by remember { mutableStateOf(false) }

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
                Text("Detalhes do livro", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                IconButton(onClick = onNotificacoes) {
                    Icon(Icons.Outlined.Notifications, contentDescription = "Notificações")
                }
            }
        }

        // ── Loading / Error ───────────────────────────────────────────────────
        if (state.isLoading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().height(300.dp),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = AzulPrimario) }
            }
            return@LazyColumn
        }

        if (state.error != null) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Não foi possível carregar o livro.",
                        color = Color(0xFF616161),
                        fontSize = 14.sp
                    )
                }
            }
            return@LazyColumn
        }

        val livro = state.livro ?: return@LazyColumn
        val corFallback = coresFallback[livro.id % coresFallback.size]

        // ── RF09.2: Capa ──────────────────────────────────────────────────────
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CapaLivro(
                    isbn = livro.isbn ?: "",
                    tituloFallback = livro.titulo,
                    modifier = Modifier
                        .width(180.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp)),
                    corFallback = corFallback
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // ── RF09.2: Metadados ─────────────────────────────────────────────────
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "GÊNERO: ${livro.genero.uppercase()}",
                        color = AzulPrimario,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    livro.ano_publicacao?.let {
                        Text(text = "Ano: $it", color = Color.Gray, fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = livro.titulo, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Text(text = "Autor: ${livro.autor}", color = Color.DarkGray, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // ── RF09.3: Nota Média ────────────────────────────────────────────────
        item {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val notaInt = livro.nota_media.toInt()
                repeat(5) { index ->
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = if (index < notaInt) AmareloEstrela else Color.LightGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${"%.1f".format(livro.nota_media)} (${livro.qtd_avaliacoes} avaliações)",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // ── RF09.4 + RF09.5: Botões de Ação ──────────────────────────────────
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { onLerAgora(livro.titulo) },
                    colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Icon(Icons.Outlined.MenuBook, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ler Agora", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedIconButton(
                    onClick = { viewModel.toggleFavorito(livroIdInt) },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = if (state.favoritado) Icons.Filled.Bookmark
                                      else Icons.Outlined.BookmarkBorder,
                        contentDescription = if (state.favoritado) "Remover dos favoritos"
                                             else "Adicionar aos favoritos",
                        tint = if (state.favoritado) AzulPrimario else Color.Gray
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

        // ── RF09.6: Sinopse expansível ────────────────────────────────────────
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
                    text = livro.sinopse ?: "Sem sinopse disponível.",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = if (sinopseExpandida) Int.MAX_VALUE else 4,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (!livro.sinopse.isNullOrBlank()) {
                    Text(
                        text = if (sinopseExpandida) "Ler menos" else "Ler mais",
                        color = AzulPrimario,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { sinopseExpandida = !sinopseExpandida }
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

        // ── RF09.7: Avaliações ────────────────────────────────────────────────
        item {
            Text(
                text = "Avaliações de Usuários",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        if (state.resenhas.isEmpty()) {
            item {
                Text(
                    text = "Nenhuma avaliação ainda.",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        } else {
            items(state.resenhas, key = { it.id }) { resenha ->
                ResenhaCard(resenha)
            }
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

@Composable
private fun ResenhaCard(resenha: ResenhaUi) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF424242)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        resenha.autorNome.firstOrNull()?.toString() ?: "?",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(resenha.autorNome, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Row {
                        repeat(5) { index ->
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = null,
                                tint = if (index < resenha.nota) AmareloEstrela else Color.LightGray,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }
            Text(resenha.tempoRelativo, color = Color.Gray, fontSize = 12.sp)
        }
        if (resenha.texto.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = resenha.texto,
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

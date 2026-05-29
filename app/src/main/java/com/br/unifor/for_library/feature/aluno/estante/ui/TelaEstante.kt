package com.br.unifor.for_library.feature.aluno.estante.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.br.unifor.for_library.feature.aluno.estante.viewmodel.EstanteViewModel
import com.br.unifor.for_library.feature.aluno.estante.viewmodel.FavoritoEstante
import com.br.unifor.for_library.feature.aluno.estante.viewmodel.ProgressoEstante

private val AzulPrimario = Color(0xFF1565C0)
private val CinzaTexto   = Color(0xFF616161)
private val FundoTela    = Color(0xFFF5F7FA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaEstante(
    onHistoricoClick: () -> Unit = {},
    viewModel: EstanteViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    var tabSelecionada by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoTela)
    ) {
        // Header (RF12.1 / RF13.1 — lupa suprimida conforme spec)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Minha Estante",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A2E)
            )
            IconButton(onClick = onHistoricoClick) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Histórico de leitura",
                    tint = Color(0xFF424242),
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // Tabs (RF12.2)
        TabRow(
            selectedTabIndex = tabSelecionada,
            containerColor = Color.White,
            contentColor = AzulPrimario,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[tabSelecionada]),
                    color = AzulPrimario
                )
            }
        ) {
            listOf("Lendo", "Favoritos").forEachIndexed { index, titulo ->
                Tab(
                    selected = tabSelecionada == index,
                    onClick = { tabSelecionada = index },
                    text = {
                        Text(
                            text = titulo,
                            fontSize = 14.sp,
                            fontWeight = if (tabSelecionada == index) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AzulPrimario)
            }
            return@Column
        }

        if (state.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Não foi possível carregar a estante.",
                    color = CinzaTexto,
                    fontSize = 14.sp
                )
            }
            return@Column
        }

        when (tabSelecionada) {
            0 -> AbaLendo(livros = state.livrosLendo)
            1 -> AbaFavoritos(
                favoritos = state.livrosFavoritos,
                onRemoverFavorito = { favoritoId -> viewModel.removerFavorito(favoritoId) }
            )
        }
    }
}

// ── Aba Lendo (RF12.3) ───────────────────────────────────────────────────────

@Composable
private fun AbaLendo(livros: List<ProgressoEstante>) {
    if (livros.isEmpty()) {
        EstanteVazia(
            mensagem = "Você não está lendo nenhum livro",
            submensagem = "Acesse o acervo e comece uma leitura!"
        )
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(livros, key = { it.id }) { progresso ->
            CardLivroLendo(progresso = progresso)
        }
    }
}

@Composable
private fun CardLivroLendo(progresso: ProgressoEstante) {
    val livro = progresso.livros
    val corFallback = coresFallback[livro.id % coresFallback.size]

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CapaLivro(
                isbn = livro.isbn ?: "",
                tituloFallback = livro.titulo,
                modifier = Modifier
                    .width(60.dp)
                    .height(85.dp)
                    .clip(RoundedCornerShape(6.dp)),
                corFallback = corFallback
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = livro.titulo,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A2E),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = livro.autor,
                    fontSize = 12.sp,
                    color = CinzaTexto
                )
                Spacer(Modifier.height(10.dp))

                Text(
                    text = "${(progresso.progressoPct * 100).toInt()}% concluído",
                    fontSize = 11.sp,
                    color = AzulPrimario,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { progresso.progressoPct.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(50)),
                    color = AzulPrimario,
                    trackColor = Color(0xFFE3EBF6)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${progresso.pagina_atual} / ${livro.total_paginas} págs",
                    fontSize = 11.sp,
                    color = CinzaTexto
                )
            }
        }
    }
}

// ── Aba Favoritos (RF13.2 / RF13.3) ─────────────────────────────────────────

@Composable
private fun AbaFavoritos(
    favoritos: List<FavoritoEstante>,
    onRemoverFavorito: (Int) -> Unit
) {
    if (favoritos.isEmpty()) {
        EstanteVazia(
            mensagem = "Nenhum livro favorito ainda",
            submensagem = "Marque livros como favoritos para encontrá-los aqui."
        )
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(favoritos, key = { it.id }) { favorito ->
            CardLivroFavorito(
                favorito = favorito,
                onRemover = { onRemoverFavorito(favorito.id) }
            )
        }
    }
}

@Composable
private fun CardLivroFavorito(
    favorito: FavoritoEstante,
    onRemover: () -> Unit
) {
    val livro = favorito.livros
    val corFallback = coresFallback[livro.id % coresFallback.size]

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.68f)
        ) {
            CapaLivro(
                isbn = livro.isbn ?: "",
                tituloFallback = livro.titulo,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp)),
                corFallback = corFallback
            )

            // Botão Bookmark — validação do spec: coração é reprovação imediata
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.9f))
                    .clickable { onRemover() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = "Remover dos favoritos",
                    tint = AzulPrimario,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(Modifier.height(6.dp))
        Text(
            text = livro.titulo,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1A1A2E),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 16.sp
        )
        Text(
            text = livro.autor,
            fontSize = 11.sp,
            color = CinzaTexto,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ── Estado vazio ─────────────────────────────────────────────────────────────

@Composable
private fun EstanteVazia(mensagem: String, submensagem: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MenuBook,
                contentDescription = null,
                tint = Color(0xFFBDBDBD),
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = mensagem,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF424242)
            )
            Text(
                text = submensagem,
                fontSize = 13.sp,
                color = CinzaTexto
            )
        }
    }
}

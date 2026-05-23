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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import com.br.unifor.for_library.core.data.LivrosSalvosState
import com.br.unifor.for_library.core.data.catalogoGlobal
import com.br.unifor.for_library.core.components.CapaLivro
import com.br.unifor.for_library.core.designsystem.coresFallback

// â”€â”€ Cores â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
private val AzulPrimario  = Color(0xFF1565C0)
private val CinzaTexto    = Color(0xFF616161)
private val FundoTela     = Color(0xFFF5F7FA)

// â”€â”€ Modelos â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
data class LivroLendo(
    val id: Int,
    val titulo: String,
    val autor: String,
    val isbn: String,
    val paginaAtual: Int,
    val totalPaginas: Int,
    val progressoPct: Float = paginaAtual.toFloat() / totalPaginas.toFloat()
)



// â”€â”€ Mock â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
private val mockLendo = listOf(
    LivroLendo(1, "O Design do Dia a Dia",          "Don Norman",       "9780465050659", 248, 390),
    LivroLendo(2, "Sistemas Ãgeis no Enterprise",   "Kent Beck",        "9780321125217",  45, 952),
    LivroLendo(3, "Arquitetura de Software Moderno","Martin Fowler",    "9780134494166", 412, 448),
    LivroLendo(4, "Psicologia da CogniÃ§Ã£o",         "Daniel Kahneman",  "9788535921311", 130, 418),
)



// â”€â”€ Tela principal â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaEstante(
    onSearchClick: () -> Unit = {},
    onHistoricoClick: () -> Unit = {}
) {
    var tabSelecionada by remember { mutableIntStateOf(0) }

    val abas = listOf("Lendo", "Favoritos")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoTela)
    ) {
        // â”€â”€ Header (RF12.1 / RF13.1) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(AzulPrimario),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "Minha Estante",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A2E)
                )
            }

            IconButton(onClick = onHistoricoClick) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Histórico",
                    tint = Color(0xFF424242),
                    modifier = Modifier.size(22.dp)
                )
            }
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Pesquisar",
                    tint = Color(0xFF424242),
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // â”€â”€ Tabs (RF12.2) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
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
            abas.forEachIndexed { index, titulo ->
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

        // â”€â”€ ConteÃºdo das abas â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        when (tabSelecionada) {
            0 -> AbaLendo()
            1 -> AbaFavoritos(
                onToggleFavorito = { isbn ->
                    LivrosSalvosState.toggleSalvo(isbn)
                }
            )
        }
    }
}

// â”€â”€ Aba Lendo (RF12.3) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
@Composable
private fun AbaLendo() {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(mockLendo, key = { it.id }) { livro ->
            CardLivroLendo(livro = livro)
        }
    }
}

@Composable
private fun CardLivroLendo(livro: LivroLendo) {
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
            // Capa
            CapaLivro(
                isbn = livro.isbn,
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

                // Progresso
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "${(livro.progressoPct * 100).toInt()}% concluÃ­do",
                        fontSize = 11.sp,
                        color = AzulPrimario,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { livro.progressoPct },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(50)),
                    color = AzulPrimario,
                    trackColor = Color(0xFFE3EBF6)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${livro.paginaAtual} de ${livro.totalPaginas} pÃ¡ginas",
                    fontSize = 11.sp,
                    color = CinzaTexto
                )
            }
        }
    }
}

// â”€â”€ Aba Favoritos (RF13.2 / RF13.3) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
@Composable
private fun AbaFavoritos(
    onToggleFavorito: (String) -> Unit
) {
    val favoritos = catalogoGlobal.filter { LivrosSalvosState.isSalvo(it.isbn) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(favoritos, key = { it.isbn }) { livro ->
            CardLivroFavorito(
                titulo = livro.titulo,
                autor = livro.autor,
                isbn = livro.isbn,
                onToggleFavorito = { onToggleFavorito(livro.isbn) }
            )
        }
    }
}

@Composable
private fun CardLivroFavorito(
    titulo: String,
    autor: String,
    isbn: String,
    onToggleFavorito: () -> Unit
) {
    val corFallback = coresFallback[isbn.hashCode().and(0x7FFFFFFF) % coresFallback.size]

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
                isbn = isbn,
                tituloFallback = titulo,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp)),
                corFallback = corFallback
            )

            // Botão de Favorito (Coração)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.9f))
                    .clickable { onToggleFavorito() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = "Favoritado",
                    tint = AzulPrimario,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(Modifier.height(6.dp))
        Text(
            text = titulo,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1A1A2E),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 16.sp
        )
        Text(
            text = autor,
            fontSize = 11.sp,
            color = CinzaTexto,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


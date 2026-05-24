package com.br.unifor.for_library.feature.aluno.acervo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FilterList
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
import com.br.unifor.for_library.core.components.FiltroAvancadoBottomSheet
import com.br.unifor.for_library.core.components.FiltroAvancadoState
import com.br.unifor.for_library.core.data.LivrosSalvosState
import com.br.unifor.for_library.core.designsystem.AzulPrimario
import com.br.unifor.for_library.core.designsystem.CinzaTexto
import com.br.unifor.for_library.core.designsystem.coresFallback
import com.br.unifor.for_library.feature.aluno.acervo.viewmodel.AcervoViewModel
import com.br.unifor.for_library.feature.aluno.components.AcervoEmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAcervoDigital(
    viewModel: AcervoViewModel = viewModel(),
    onLivroClick: (Int) -> Unit = {},
    onSinoClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val categorias = listOf("Tudo", "Ficção", "Tecnologia", "História", "Design")
    var mostrarFiltro by remember { mutableStateOf(false) }

    // BottomSheet para o RF07 (Filtros Avançados)
    FiltroAvancadoBottomSheet(
        visivel = mostrarFiltro,
        estadoInicial = state.filtrosAvancados,
        onDismiss = { mostrarFiltro = false },
        onAplicar = { novoFiltro -> 
            viewModel.onAplicarFiltrosAvancados(novoFiltro)
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Cabeçalho (Conforme Requisito RF06)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Foto de perfil (Iniciais)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(AzulPrimario),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.usuario.primeiroNome.ifEmpty { "U" }.first().toString(),
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Acervo Digital",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(onClick = onSinoClick) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notificações",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Search Bar (Conforme Requisito RF06)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = state.query,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                placeholder = { Text("Busque por título ou autor", fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            // Botão de Filtros Avançados
            Surface(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { mostrarFiltro = true },
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Outlined.FilterList, contentDescription = "Filtros")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Filtros Genéricos (Chips)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categorias, key = { it }) { categoria ->
                val isSelected = categoria == state.categoriaSelecionada
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.onCategoriaChange(categoria) },
                    label = { Text(categoria) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AzulPrimario,
                        selectedLabelColor = Color.White
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outline,
                        enabled = true,
                        selected = isSelected
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Grid de Livros (2 colunas conforme RF06)
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AzulPrimario)
            }
        } else if (state.livros.isEmpty()) {
            BuscaVaziaPlaceholder(
                onLimparFiltros = { viewModel.limparFiltros() },
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(state.livros, key = { it.id }) { livro ->
                    val corFallback = coresFallback[livro.id % coresFallback.size]
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLivroClick(livro.id) },
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
                                    .clip(RoundedCornerShape(8.dp)),
                                corFallback = corFallback
                            )

                            // Label "NOVO" (Requisito RF06 - Livros recentes)
                            if (livro.isNovo) {
                                Surface(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(8.dp),
                                    color = Color(0xFF4CAF50),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "NOVO",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }

                            // Botão Salvar (Favoritos)
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.9f))
                                    .clickable { livro.isbn?.let { LivrosSalvosState.toggleSalvo(it) } },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (livro.isbn?.let { LivrosSalvosState.isSalvo(it) } == true)
                                        Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                    contentDescription = "Salvar livro",
                                    tint = if (livro.isbn?.let { LivrosSalvosState.isSalvo(it) } == true)
                                        AzulPrimario else Color(0xFF757575),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = livro.titulo,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = livro.autor,
                            style = MaterialTheme.typography.bodySmall,
                            color = CinzaTexto,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

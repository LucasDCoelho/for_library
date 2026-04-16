package com.br.unifor.for_library.feature.acervo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.br.unifor.for_library.AzulPrimario
import com.br.unifor.for_library.CapaLivro
import com.br.unifor.for_library.CinzaTexto
import com.br.unifor.for_library.coresFallback

private data class LivroDestaque(
    val id: Int,
    val titulo: String,
    val autor: String,
    val isbn: String,
    val salvo: Boolean = false
)

private val mockDestaques = listOf(
    LivroDestaque(1, "O Design do Dia a Dia", "Don Norman", isbn = "9780465050659", salvo = false),
    LivroDestaque(2, "Sapiens: Uma Breve História", "Yuval Noah Harari", isbn = "9788543102146", salvo = false),
    LivroDestaque(3, "Clean Code", "Robert C. Martin", isbn = "9780132350884", salvo = false),
    LivroDestaque(4, "O Pequeno Príncipe", "Antoine de Saint-Exupéry", isbn = "9788522031412", salvo = false),
    LivroDestaque(5, "Fundamentos da Gestão", "Peter Drucker", isbn = "9788522102716", salvo = false),
    LivroDestaque(6, "Atomic Habits", "James Clear", isbn = "9780735211292", salvo = false)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAcervoDigital(
    onLivroClick: (Int) -> Unit = {}
) {
    // Estados do Grid
    var destaques by remember { mutableStateOf(mockDestaques) }

    // Estados do Header
    var searchQuery by remember { mutableStateOf("") }
    val categorias = listOf("Tudo", "Ficção", "Tecnologia", "História", "Design")
    var categoriaSelecionada by remember { mutableStateOf("Tudo") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // ==========================================
        // HEADER
        // ==========================================

        // 1. Top Bar (Perfil, Título e Notificação)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2C3E50)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Acervo Digital",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            IconButton(onClick = { /* Ação Notificação */ }) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notificações",
                    tint = Color.DarkGray
                )
            }
        }

        // 2. Barra de Pesquisa e Botão de Filtro
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                placeholder = {
                    Text(
                        "Busque por título ou autor",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Ícone de busca",
                        tint = Color.Gray
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(4.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = AzulPrimario
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFF5F5F5))
                    .clickable { /* Filtros */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.FilterList,
                    contentDescription = "Filtros",
                    tint = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 3. Categorias (Chips)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categorias) { categoria ->
                val isSelected = categoria == categoriaSelecionada
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isSelected) AzulPrimario else Color(0xFFE0E0E0))
                        .clickable { categoriaSelecionada = categoria }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = categoria,
                        color = if (isSelected) Color.White else Color.DarkGray,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ==========================================
        // GRID DE LIVROS
        // ==========================================

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            // O weight(1f) garante que o grid ocupe o restante da tela abaixo do header
            modifier = Modifier.weight(1f)
        ) {
            items(destaques) { livro ->
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
                            isbn = livro.isbn,
                            tituloFallback = livro.titulo,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp)),
                            corFallback = corFallback
                        )

                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.9f))
                                .clickable {
                                    destaques = destaques.map {
                                        if (it.id == livro.id) it.copy(salvo = !it.salvo) else it
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (livro.salvo) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                contentDescription = "Salvar livro",
                                tint = if (livro.salvo) AzulPrimario else Color(0xFF757575),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = livro.titulo,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 18.sp
                    )

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = livro.autor,
                        fontSize = 12.sp,
                        color = CinzaTexto,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
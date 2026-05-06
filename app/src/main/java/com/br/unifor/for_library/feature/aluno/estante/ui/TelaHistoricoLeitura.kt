package com.br.unifor.for_library.feature.aluno.estante.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AzulPrimario = Color(0xFF1565C0)

private data class LivroLido(
    val id: Int,
    val titulo: String,
    val autor: String,
    val dataConclusao: String,
    val corCapa: Color
)

private val mockLivrosLidos = listOf(
    LivroLido(1, "Dom Casmurro", "Machado de Assis", "Concluído em 14 de Out, 2023", Color(0xFF4E342E)),
    LivroLido(2, "O Algoritmo da Vitória", "José Roberto Guimarães", "Concluído em 02 de Set, 2023", Color(0xFF1A237E)),
    LivroLido(3, "1984", "George Orwell", "Concluído em 15 de Ago, 2023", Color(0xFFB71C1C)),
    LivroLido(4, "Sapiens", "Yuval Noah Harari", "Concluído em 22 de Jul, 2023", Color(0xFF1B5E20)),
    LivroLido(5, "A Revolução dos Bichos", "George Orwell", "Concluído em 05 de Jun, 2023", Color(0xFF4A148C)),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaHistoricoLeitura(
    onVoltar: () -> Unit = {},
    onLivroClick: (Int) -> Unit = {}
) {
    var busca by remember { mutableStateOf("") }

    val livrosFiltrados = remember(busca) {
        if (busca.isBlank()) mockLivrosLidos
        else mockLivrosLidos.filter {
            it.titulo.contains(busca, ignoreCase = true) ||
                    it.autor.contains(busca, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Histórico de Leitura", fontWeight = FontWeight.SemiBold, fontSize = 17.sp) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                Text(
                    text = "Livros Concluídos",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
                )
            }

            itemsIndexed(livrosFiltrados) { index, livro ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLivroClick(livro.id) }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Capa simulada
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .height(68.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(livro.corCapa),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = livro.titulo.take(2),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(livro.titulo, fontSize = 14.sp,
                            fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                        Text(livro.autor, fontSize = 12.sp, color = Color(0xFF757575),
                            modifier = Modifier.padding(top = 2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(livro.dataConclusao, fontSize = 11.sp, color = Color(0xFF757575))
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFFBDBDBD),
                        modifier = Modifier.size(20.dp)
                    )
                }

                if (index < livrosFiltrados.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color(0xFFEEEEEE),
                        thickness = 0.5.dp
                    )
                }
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}


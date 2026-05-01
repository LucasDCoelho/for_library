package com.br.unifor.for_library.feature.adm.acervo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
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
import com.br.unifor.for_library.core.components.CapaLivro
import com.br.unifor.for_library.core.designsystem.AzulPrimario
import com.br.unifor.for_library.core.designsystem.CinzaTexto

data class LivroAdminMock(
    val id: String,
    val titulo: String,
    val autor: String,
    val isbn: String
)

private val mockLivrosAdmin = listOf(
    LivroAdminMock("1", "Design System Essentials", "IBM Design Team",     "9780132350884"),
    LivroAdminMock("2", "The Grid System",          "Josef Müller-Brockmann","9783721201451"),
    LivroAdminMock("3", "Accessibility in UI",      "Sara Soueidan",        "9781492053217"),
    LivroAdminMock("4", "TypeScript Patterns",      "Dan Vanderkam",        "9781491904008"),
    LivroAdminMock("5", "Enterprise Architecture",  "Martin Fowler",        "9780321127426"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaGestaoAcervo(
    onVoltar: () -> Unit,
    onAdicionarLivro: () -> Unit,
    onEditarLivro: (String) -> Unit,
    onExcluirLivro: (String) -> Unit
) {
    var busca by remember { mutableStateOf("") }
    val livros = mockLivrosAdmin

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestão de Acervo", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAdicionarLivro,
                containerColor = AzulPrimario,
                contentColor = Color.White,
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar livro")
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // ── Search Bar (RF27.2) ────────────────────────────────────────────
            OutlinedTextField(
                value = busca,
                onValueChange = { busca = it },
                placeholder = { Text("Busque por título ou autor", fontSize = 13.sp, color = Color.LightGray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                singleLine = true,
                shape = RoundedCornerShape(6.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = AzulPrimario
                )
            )

            // Quantidade de livros (RF27.2)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "${livros.size} livros",
                    fontSize = 12.sp,
                    color = CinzaTexto
                )
            }

            // Lista de livros (RF27.3)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(livros) { livro ->
                    ItemLivroAdmin(
                        livro = livro,
                        onEditar = { onEditarLivro(livro.id) },
                        onExcluir = { onExcluirLivro(livro.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ItemLivroAdmin(
    livro: LivroAdminMock,
    onEditar: () -> Unit,
    onExcluir: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CapaLivro(
                isbn = livro.isbn,
                tituloFallback = livro.titulo,
                modifier = Modifier
                    .width(48.dp)
                    .height(64.dp)
                    .clip(RoundedCornerShape(4.dp)),
                corFallback = AzulPrimario
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    livro.titulo,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = Color(0xFF212121),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    livro.autor,
                    fontSize = 11.sp,
                    color = CinzaTexto,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Editar (RF27.4)
            IconButton(onClick = onEditar) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = AzulPrimario,
                    modifier = Modifier.size(18.dp)
                )
            }
            // Excluir (RF27.3)
            IconButton(onClick = onExcluir) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Excluir",
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

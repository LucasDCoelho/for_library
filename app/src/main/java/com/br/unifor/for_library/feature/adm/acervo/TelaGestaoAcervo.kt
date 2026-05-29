package com.br.unifor.for_library.feature.adm.acervo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.br.unifor.for_library.core.components.CapaLivro
import com.br.unifor.for_library.core.designsystem.AzulPrimario
import com.br.unifor.for_library.core.designsystem.CinzaTexto
import com.br.unifor.for_library.feature.PopupExclusaoObra

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaGestaoAcervo(
    onAdicionarLivro: () -> Unit,
    onEditarLivro: (String) -> Unit,
    viewModel: GestaoAcervoViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var livroParaExcluir by remember { mutableStateOf<LivroAdmin?>(null) }

    LaunchedEffect(state.mensagemSucesso) {
        state.mensagemSucesso?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.limparMensagem()
        }
    }

    LaunchedEffect(state.erro) {
        state.erro?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.limparMensagem()
        }
    }

    livroParaExcluir?.let { livro ->
        PopupExclusaoObra(
            mensagem = "A obra \"${livro.titulo}\" será removida permanentemente do acervo e das estantes de todos os alunos. Deseja continuar?",
            onDismiss = { livroParaExcluir = null },
            onConfirm = {
                viewModel.deletarLivro(livro.id)
                livroParaExcluir = null
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestão de Acervo", fontWeight = FontWeight.Bold, fontSize = 20.sp) }
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = state.query,
                onValueChange = { viewModel.onQueryChange(it) },
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = AzulPrimario
                    )
                } else {
                    Text(
                        text = "${state.livros.size} livro${if (state.livros.size != 1) "s" else ""}",
                        fontSize = 12.sp,
                        color = CinzaTexto
                    )
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(state.livros, key = { it.id }) { livro ->
                    ItemLivroAdmin(
                        livro = livro,
                        onEditar = { onEditarLivro(livro.id.toString()) },
                        onExcluir = { livroParaExcluir = livro }
                    )
                }
            }
        }
    }
}

@Composable
private fun ItemLivroAdmin(
    livro: LivroAdmin,
    onEditar: () -> Unit,
    onExcluir: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!livro.capa_url.isNullOrBlank()) {
                AsyncImage(
                    model = livro.capa_url,
                    contentDescription = livro.titulo,
                    modifier = Modifier
                        .width(48.dp)
                        .height(64.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                CapaLivro(
                    isbn = livro.isbn,
                    tituloFallback = livro.titulo,
                    modifier = Modifier
                        .width(48.dp)
                        .height(64.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    corFallback = AzulPrimario
                )
            }
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
            IconButton(onClick = onEditar) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = AzulPrimario,
                    modifier = Modifier.size(18.dp)
                )
            }
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

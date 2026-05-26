package com.br.unifor.for_library.feature.aluno.presentation.historico

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val AzulPrimario = Color(0xFF1565C0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaHistoricoLeitura(
    onVoltar: () -> Unit = {},
    onLivroClick: (Int) -> Unit = {},
    viewModel: HistoricoLeituraViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val historicoFiltrado by viewModel.historicoFiltrado.collectAsStateWithLifecycle()

    var isSearching by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSearching) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { viewModel.updateSearchQuery(it) },
                            placeholder = { Text("Pesquisar obra...", fontSize = 16.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
                        )
                        LaunchedEffect(Unit) {
                            focusRequester.requestFocus()
                        }
                    } else {
                        Text("Histórico de Leitura", fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = if (isSearching) { { isSearching = false; viewModel.updateSearchQuery("") } } else onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    if (isSearching) {
                        IconButton(onClick = { 
                            if (searchQuery.isNotEmpty()) viewModel.updateSearchQuery("") 
                            else isSearching = false 
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Fechar busca")
                        }
                    } else {
                        IconButton(onClick = { isSearching = true }) {
                            Icon(Icons.Default.Search, contentDescription = "Buscar")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
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
            ) {
                item {
                    Text(
                        text = if (isSearching) "Resultados da busca" else "Livros Concluídos",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
                    )
                }

                if (historicoFiltrado.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(top = 64.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                if (isSearching) "Nenhuma obra encontrada" else "Você ainda não concluiu nenhum livro",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    itemsIndexed(historicoFiltrado) { index, item ->
                        ItemHistorico(
                            progresso = item,
                            onClick = { item.livros?.id?.let { onLivroClick(it) } }
                        )

                        if (index < historicoFiltrado.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = Color(0xFFEEEEEE),
                                thickness = 0.5.dp
                            )
                        }
                    }
                }

                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
private fun ItemHistorico(
    progresso: ProgressoLeitura,
    onClick: () -> Unit
) {
    val livro = progresso.livros ?: return
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Capa do Livro (Simulada com a primeira letra e cor baseada no ID)
        val coresCapa = listOf(Color(0xFF4E342E), Color(0xFF1A237E), Color(0xFFB71C1C), Color(0xFF1B5E20), Color(0xFF4A148C))
        val corCapa = coresCapa[livro.id % coresCapa.size]

        Box(
            modifier = Modifier
                .width(48.dp)
                .height(68.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(corCapa),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = livro.titulo.take(1).uppercase(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = livro.titulo,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )
            Text(
                text = livro.autor,
                fontSize = 12.sp,
                color = Color(0xFF757575),
                modifier = Modifier.padding(top = 2.dp)
            )
            
            progresso.data_conclusao?.let { dataRaw ->
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
                    Text(
                        text = "Concluído em ${formatarData(dataRaw)}",
                        fontSize = 11.sp,
                        color = Color(0xFF757575)
                    )
                }
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color(0xFFBDBDBD),
            modifier = Modifier.size(20.dp)
        )
    }
}

private fun formatarData(dataIso: String): String {
    return try {
        val data = ZonedDateTime.parse(dataIso)
        val formatter = DateTimeFormatter.ofPattern("dd 'de' MMM, yyyy", Locale("pt", "BR"))
        data.format(formatter)
    } catch (e: Exception) {
        dataIso
    }
}

package com.br.unifor.for_library.feature.adm.acervo

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.br.unifor.for_library.core.components.CapaLivro
import com.br.unifor.for_library.core.designsystem.AzulPrimario

private val listaGeneros = listOf(
    "Ficção", "Acadêmico", "Tecnologia", "Biografia", "História", "Design"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaEditarObra(
    livroId: String,
    onVoltar: () -> Unit,
    onAtualizar: () -> Unit = {},
    onCancelar: () -> Unit = {},
    viewModel: EdicaoLivroViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> viewModel.onCapaSelecionada(uri) }

    val pdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> viewModel.onArquivoSelecionado(uri) }

    LaunchedEffect(livroId) {
        viewModel.carregarLivro(livroId)
    }

    LaunchedEffect(state.sucesso) {
        if (state.sucesso) {
            snackbarHostState.showSnackbar("Obra atualizada com sucesso")
            viewModel.limparSucesso()
            onVoltar()
        }
    }

    LaunchedEffect(state.erro) {
        state.erro?.let { snackbarHostState.showSnackbar(it) }
    }

    var menuExpandido by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Obra", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White
    ) { paddingValues ->

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AzulPrimario)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // ── Capa do Livro ──────────────────────────────────────────────────
            Text("Capa do Livro", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val capaModel: Any? = state.novaCapaUri ?: state.capaUrl?.takeIf { it.isNotBlank() }
                    if (capaModel != null) {
                        AsyncImage(
                            model = capaModel,
                            contentDescription = state.titulo,
                            modifier = Modifier
                                .width(60.dp)
                                .height(84.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        CapaLivro(
                            isbn = state.isbn.ifBlank { null },
                            tituloFallback = state.titulo,
                            modifier = Modifier
                                .width(60.dp)
                                .height(84.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            corFallback = AzulPrimario
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            state.titulo.ifBlank { "Sem título" },
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = Color(0xFF212121),
                            maxLines = 2
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = { imageLauncher.launch("image/*") },
                            modifier = Modifier.height(32.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario),
                            shape = RoundedCornerShape(4.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp)
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Trocar Capa", color = Color.White, fontSize = 11.sp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Upload do PDF ──────────────────────────────────────────────────
            Text("Arquivo do Livro (PDF)", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(6.dp))

            val temArquivo = state.novoArquivoUri != null || state.arquivoUrl != null
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                border = BorderStroke(1.dp, if (temArquivo) AzulPrimario else Color(0xFFE0E0E0)),
                elevation = CardDefaults.cardElevation(0.dp),
                onClick = { pdfLauncher.launch("application/pdf") }
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PictureAsPdf,
                        contentDescription = null,
                        tint = if (temArquivo) AzulPrimario else Color.LightGray,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = when {
                            state.novoArquivoUri != null -> "Novo PDF selecionado — clique para trocar"
                            state.arquivoUrl != null -> "PDF já anexado — clique para substituir"
                            else -> "Nenhum PDF — clique para anexar"
                        },
                        color = if (temArquivo) AzulPrimario else Color.Gray,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Campos do formulário ──────────────────────────────────────────
            FormCampo(label = "Título *", value = state.titulo, onValueChange = { viewModel.onTituloChange(it) })
            FormCampo(label = "Autor *", value = state.autor, onValueChange = { viewModel.onAutorChange(it) })
            FormCampo(label = "ISBN", value = state.isbn, onValueChange = { viewModel.onIsbnChange(it) })

            // ── Linha: Total de Páginas + Ano de Publicação ───────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    FormCampo(
                        label = "Total de Páginas",
                        value = state.totalPaginas,
                        onValueChange = { viewModel.onTotalPaginasChange(it) },
                        keyboardType = KeyboardType.Number
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    FormCampo(
                        label = "Ano de Publicação",
                        value = state.anoPublicacao,
                        onValueChange = { viewModel.onAnoPublicacaoChange(it) },
                        keyboardType = KeyboardType.Number
                    )
                }
            }

            // ── Gênero ────────────────────────────────────────────────────────
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Text("Gênero", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(6.dp))
                ExposedDropdownMenuBox(
                    expanded = menuExpandido,
                    onExpandedChange = { menuExpandido = it }
                ) {
                    OutlinedTextField(
                        value = state.genero.ifBlank { "Selecione" },
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpandido) },
                        shape = RoundedCornerShape(4.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedBorderColor = AzulPrimario
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = menuExpandido,
                        onDismissRequest = { menuExpandido = false }
                    ) {
                        listaGeneros.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    viewModel.onGeneroChange(item)
                                    menuExpandido = false
                                }
                            )
                        }
                    }
                }
            }

            // ── Sinopse ───────────────────────────────────────────────────────
            FormCampo(
                label = "Sinopse",
                value = state.sinopse,
                onValueChange = { viewModel.onSinopseChange(it) },
                height = 120.dp,
                singleLine = false
            )

            Spacer(Modifier.height(20.dp))

            // ── Botões ────────────────────────────────────────────────────────
            Button(
                onClick = { viewModel.salvar(context) },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario),
                shape = RoundedCornerShape(4.dp),
                enabled = !state.isSaving
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text("Atualizar Obra", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(Modifier.height(10.dp))

            OutlinedButton(
                onClick = onVoltar,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Icon(Icons.Default.Close, contentDescription = null, tint = Color(0xFF424242), modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text("Cancelar", color = Color(0xFF424242))
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FormCampo(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    height: Dp = 56.dp,
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().height(height),
            singleLine = singleLine,
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = AzulPrimario
            )
        )
    }
}

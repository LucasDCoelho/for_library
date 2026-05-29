package com.br.unifor.for_library.feature.adm.acervo

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.br.unifor.for_library.core.designsystem.AzulPrimario

private val listaGeneros = listOf(
    "Ficção", "Acadêmico", "Tecnologia", "Biografia", "História", "Design"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAdicionarObra(
    onVoltar: () -> Unit,
    onSucesso: () -> Unit = {},
    viewModel: CadastroLivroViewModel = viewModel()
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

    LaunchedEffect(state.sucesso) {
        if (state.sucesso) {
            snackbarHostState.showSnackbar("Livro cadastrado com sucesso")
            viewModel.limparSucesso()
            onSucesso()
        }
    }

    LaunchedEffect(state.erro) {
        state.erro?.let { snackbarHostState.showSnackbar(it) }
    }

    var menuExpandido by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Obra", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // ── Upload da Capa ─────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color(0xFFF9F9F9), RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                    .clickable { imageLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (state.capaUri != null) {
                    AsyncImage(
                        model = state.capaUri,
                        contentDescription = "Capa selecionada",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Fazer upload da Capa (JPG/PNG)", color = Color.Gray, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Upload do PDF ──────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .background(Color(0xFFF9F9F9), RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                    .clickable { pdfLauncher.launch("application/pdf") },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PictureAsPdf,
                        contentDescription = null,
                        tint = if (state.arquivoUri != null) AzulPrimario else Color.LightGray,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = if (state.arquivoUri != null) "PDF selecionado" else "Fazer upload do PDF (opcional)",
                        color = if (state.arquivoUri != null) AzulPrimario else Color.Gray,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Campos obrigatórios ────────────────────────────────────────────
            FormInput(
                label = "Título *",
                placeholder = "Ex: O Alquimista",
                value = state.titulo,
                onValueChange = { viewModel.onTituloChange(it) },
                isError = "titulo" in state.erros,
                errorMessage = state.erros["titulo"]
            )
            FormInput(
                label = "Autor *",
                placeholder = "Nome do autor",
                value = state.autor,
                onValueChange = { viewModel.onAutorChange(it) },
                isError = "autor" in state.erros,
                errorMessage = state.erros["autor"]
            )

            // ── Linha: Total de Páginas + Ano de Publicação ───────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    FormInput(
                        label = "Total de Páginas *",
                        placeholder = "Ex: 320",
                        value = state.totalPaginas,
                        onValueChange = { viewModel.onTotalPaginasChange(it) },
                        keyboardType = KeyboardType.Number,
                        isError = "totalPaginas" in state.erros,
                        errorMessage = state.erros["totalPaginas"]
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    FormInput(
                        label = "Ano de Publicação",
                        placeholder = "Ex: 1988",
                        value = state.anoPublicacao,
                        onValueChange = { viewModel.onAnoPublicacaoChange(it) },
                        keyboardType = KeyboardType.Number,
                        isError = "anoPublicacao" in state.erros,
                        errorMessage = state.erros["anoPublicacao"]
                    )
                }
            }

            // ── Categoria ─────────────────────────────────────────────────────
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Text("Categoria *", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(6.dp))
                ExposedDropdownMenuBox(
                    expanded = menuExpandido,
                    onExpandedChange = { menuExpandido = it }
                ) {
                    OutlinedTextField(
                        value = state.genero.ifBlank { "Selecione uma categoria" },
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpandido) },
                        shape = RoundedCornerShape(4.dp),
                        isError = "genero" in state.erros,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedBorderColor = AzulPrimario,
                            errorBorderColor = Color(0xFFD32F2F)
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
                state.erros["genero"]?.let {
                    Text(
                        text = it,
                        color = Color(0xFFD32F2F),
                        fontSize = 11.sp,
                        modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                    )
                }
            }

            // ── Sinopse ───────────────────────────────────────────────────────
            FormInput(
                label = "Sinopse",
                placeholder = "Breve descrição do livro (opcional)",
                value = state.sinopse,
                onValueChange = { viewModel.onSinopseChange(it) },
                height = 120.dp,
                singleLine = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Botões ────────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onVoltar,
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("Cancelar", color = Color.Black)
                }
                Button(
                    onClick = { viewModel.salvar(context) },
                    modifier = Modifier.weight(1f).height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario),
                    shape = RoundedCornerShape(4.dp),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                    } else {
                        Text("Cadastrar")
                    }
                }
            }
        }
    }
}

@Composable
fun FormInput(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    height: Dp = 56.dp,
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = 14.sp, color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth().height(height),
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            isError = isError,
            shape = RoundedCornerShape(4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = AzulPrimario,
                errorBorderColor = Color(0xFFD32F2F)
            )
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color(0xFFD32F2F),
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }
    }
}

package com.br.unifor.for_library.feature.aluno.perfil.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.br.unifor.for_library.feature.aluno.perfil.viewmodel.EdicaoPerfilViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaEditarPerfil(
    onVoltar: () -> Unit,
    viewModel: EdicaoPerfilViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.onFotoSelected(uri)
    }

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            scope.launch {
                snackbarHostState.showSnackbar("ALTERAÇÕES SALVAS!")
                viewModel.resetSuccess()
            }
        }
    }

    // Cores baseadas no design
    val azulPrimario = Color(0xFF1E88E5)
    val verdeSucesso = Color(0xFF4CAF50)

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    containerColor = verdeSucesso,
                    contentColor = Color.White,
                    shape = MaterialTheme.shapes.small,
                    dismissAction = {
                        IconButton(onClick = { data.dismiss() }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Fechar", tint = Color.White)
                        }
                    }
                ) {
                    Text(text = data.visuals.message, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Foto de Perfil
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier.size(100.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color(0xFFF5F5F5)),
                        contentAlignment = Alignment.Center
                    ) {
                        val model = state.novaFotoUri ?: state.fotoUrl
                        if (model != null) {
                            AsyncImage(
                                model = model,
                                contentDescription = "Foto de Perfil",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.LightGray,
                                modifier = Modifier.size(55.dp)
                            )
                        }
                    }
                    
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(azulPrimario)
                            .clickable { photoPickerLauncher.launch("image/*") }
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Mudar foto",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "CONFIGURAÇÃO DE PERFIL",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Campos de Entrada
                InputPerfil(
                    label = "Nome de Exibição",
                    value = state.nome,
                    onValueChange = { viewModel.onNomeChange(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                InputPerfil(
                    label = "Biografia Curta",
                    value = state.biografia,
                    onValueChange = { viewModel.onBiografiaChange(it) },
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(24.dp))

                InputPerfil(
                    label = "Matrícula",
                    value = state.matricula,
                    onValueChange = { },
                    trailingIcon = Icons.Default.Lock,
                    readOnly = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                InputPerfil(
                    label = "Email Institucional",
                    value = state.email,
                    onValueChange = { },
                    trailingIcon = Icons.Default.Lock,
                    readOnly = true
                )

                Spacer(modifier = Modifier.height(40.dp))

                if (state.error != null) {
                    Text(
                        text = state.error ?: "",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Button(
                    onClick = { viewModel.salvarAlteracoes(context) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = azulPrimario),
                    shape = RoundedCornerShape(6.dp),
                    enabled = !state.isSaving
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Salvar Alterações", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(imageVector = Icons.Default.Verified, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// â”€â”€ Componente ReutilizÃ¡vel de Input baseado no design â”€â”€
@Composable
private fun InputPerfil(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    maxLines: Int = 1,
    readOnly: Boolean = false,
    trailingIcon: ImageVector? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 12.sp, color = Color(0xFF424242), fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                fontSize = 14.sp,
                color = if (readOnly) Color.Gray else Color.Black
            ),
            singleLine = maxLines == 1,
            maxLines = maxLines,
            readOnly = readOnly,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFEEEEEE), // Borda muito sutil
                unfocusedBorderColor = Color(0xFFEEEEEE),
                focusedContainerColor = Color(0xFFF9F9F9), // Fundo quase branco
                unfocusedContainerColor = Color(0xFFF9F9F9),
                cursorColor = Color(0xFF1E88E5)
            ),
            shape = RoundedCornerShape(4.dp),
            trailingIcon = if(trailingIcon != null) {
                { Icon(imageVector = trailingIcon, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(18.dp)) }
            } else null
        )
    }
}

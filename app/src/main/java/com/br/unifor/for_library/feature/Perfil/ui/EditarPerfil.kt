package com.br.unifor.for_library.feature.perfil.ui

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaEditarPerfil(
    onVoltar: () -> Unit // Callback para retornar à tela de Perfil
) {
    // ── Estados dos Campos de Texto ──
    var nomeExibicao by remember { mutableStateOf("Ricardo Ferreira") }
    var biografia by remember { mutableStateOf("Bibliotecário sênior apaixonado por digitalização de acervos históricos e gestão de dados acadêmicos.") }
    var matricula by remember { mutableStateOf("2510453-9") }
    var emailInstitucional by remember { mutableStateOf("ricardo.ferreira@eduuu.unifor.br") }

    // ── Estados para o Pop-up de Sucesso ──
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Cores baseadas no design
    val azulPrimario = Color(0xFF1E88E5)
    val verdeSucesso = Color(0xFF4CAF50) // Verde para o pop-up

    Scaffold(
        // ── Configuração do Pop-up (Snackbar) customizado ──
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    containerColor = verdeSucesso,
                    contentColor = Color.White,
                    shape = MaterialTheme.shapes.small,
                    // Botão 'X' para fechar o pop-up
                    dismissAction = {
                        IconButton(onClick = { data.dismiss() }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Fechar", tint = Color.White)
                        }
                    }
                ) {
                    // Texto do pop-up
                    Text(text = data.visuals.message, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        },
        // ── Barra Superior com Botão Voltar ──
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()), // Permite rolagem se necessário
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // ── Foto de Perfil com Botão de Câmera ──
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.size(100.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color(0xFF212121)), // Fundo escuro igual imagem
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(55.dp)
                    )
                }
                // Botão Câmera simulado (X na imagem, troquei por ícone de câmera para UX melhor)
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(azulPrimario)
                        .clickable { /*TODO*/ }
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "Mudar foto", tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Label da Seção
            Text(
                text = "CONFIGURAÇÃO DE PERFIL",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Caixas de Texto (Inputs) ──

            // 1. Nome de Exibição
            InputPerfil(
                label = "Nome de Exibição",
                value = nomeExibicao,
                onValueChange = { nomeExibicao = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Biografia Curta (Multilinha)
            InputPerfil(
                label = "Biografia Curta",
                value = biografia,
                onValueChange = { biografia = it },
                maxLines = 4 // Permite mais linhas
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Matrícula (Simulando bloqueada com ícone de cadeado)
            InputPerfil(
                label = "Matrícula",
                value = matricula,
                onValueChange = { /* Bloqueado */ },
                trailingIcon = Icons.Default.Lock,
                readOnly = true // Não permite edição visualmente
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Email Institucional (Simulando bloqueada com ícone de cadeado)
            InputPerfil(
                label = "Email Institucional",
                value = emailInstitucional,
                onValueChange = { /* Bloqueado */ },
                trailingIcon = Icons.Default.Lock,
                readOnly = true // Não permite edição visualmente
            )

            Spacer(modifier = Modifier.height(40.dp))

            // ── Botão Salvar Alterações ──
            Button(
                onClick = {
                    // Dispara o pop-up de sucesso usando uma corrotina
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "ALTERAÇÕES SALVAS!", // Texto do pop-up
                            duration = SnackbarDuration.Short
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = azulPrimario),
                shape = RoundedCornerShape(6.dp) // Shape quadrado como na imagem
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Salvar Alterações", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(imageVector = Icons.Default.Verified, contentDescription = null, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ── Componente Reutilizável de Input baseado no design ──
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
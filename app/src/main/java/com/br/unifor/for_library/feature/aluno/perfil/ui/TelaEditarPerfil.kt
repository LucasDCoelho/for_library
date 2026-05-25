package com.br.unifor.for_library.feature.aluno.perfil.ui

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
    onVoltar: () -> Unit // Callback para retornar Ã  tela de Perfil
) {
    // â”€â”€ Estados dos Campos de Texto â”€â”€
    var nomeExibicao by remember { mutableStateOf("Ricardo Ferreira") }
    var biografia by remember { mutableStateOf("BibliotecÃ¡rio sÃªnior apaixonado por digitalizaÃ§Ã£o de acervos histÃ³ricos e gestÃ£o de dados acadÃªmicos.") }
    var matricula by remember { mutableStateOf("2510453-9") }
    var emailInstitucional by remember { mutableStateOf("ricardo.ferreira@eduuu.unifor.br") }

    // â”€â”€ Estados para o Pop-up de Sucesso â”€â”€
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Cores baseadas no design
    val azulPrimario = Color(0xFF1E88E5)
    val verdeSucesso = Color(0xFF4CAF50) // Verde para o pop-up

    Scaffold(
        // â”€â”€ ConfiguraÃ§Ã£o do Pop-up (Snackbar) customizado â”€â”€
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    containerColor = verdeSucesso,
                    contentColor = Color.White,
                    shape = MaterialTheme.shapes.small,
                    // BotÃ£o 'X' para fechar o pop-up
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
        // â”€â”€ Barra Superior com BotÃ£o Voltar â”€â”€
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
                .verticalScroll(rememberScrollState()), // Permite rolagem se necessÃ¡rio
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // â”€â”€ Foto de Perfil com BotÃ£o de CÃ¢mera â”€â”€
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
                // BotÃ£o CÃ¢mera simulado (X na imagem, troquei por Ã­cone de cÃ¢mera para UX melhor)
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

            // Label da SeÃ§Ã£o
            Text(
                text = "CONFIGURAÇÃO DE PERFIL",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // â”€â”€ Caixas de Texto (Inputs) â”€â”€

            // 1. Nome de ExibiÃ§Ã£o
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

            // 3. MatrÃ­cula (Simulando bloqueada com Ã­cone de cadeado)
            InputPerfil(
                label = "Matrícula",
                value = matricula,
                onValueChange = { /* Bloqueado */ },
                trailingIcon = Icons.Default.Lock,
                readOnly = true // NÃ£o permite ediÃ§Ã£o visualmente
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Email Institucional (Simulando bloqueada com Ã­cone de cadeado)
            InputPerfil(
                label = "Email Institucional",
                value = emailInstitucional,
                onValueChange = { /* Bloqueado */ },
                trailingIcon = Icons.Default.Lock,
                readOnly = true // NÃ£o permite ediÃ§Ã£o visualmente
            )

            Spacer(modifier = Modifier.height(40.dp))

            // â”€â”€ BotÃ£o Salvar AlteraÃ§Ãµes â”€â”€
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

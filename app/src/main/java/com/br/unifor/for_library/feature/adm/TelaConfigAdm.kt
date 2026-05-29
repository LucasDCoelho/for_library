package com.br.unifor.for_library.feature.adm

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.br.unifor.for_library.core.components.PopupLogout
import com.br.unifor.for_library.core.designsystem.AzulPrimario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaConfiguracoesSistema(
    onVoltar: () -> Unit,
    onSairConfirm: () -> Unit,
    viewModel: ConfiguracoesSistemaViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    var mostrarPopupSair by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val corVermelha = Color(0xFFD32F2F)
    val fundoVermelhoClaro = Color(0xFFFFF5F5)

    LaunchedEffect(state.salvouComSucesso) {
        if (state.salvouComSucesso) {
            snackbarHostState.showSnackbar("Configurações salvas com sucesso!")
            viewModel.consumirSucesso()
        }
    }

    LaunchedEffect(state.erro) {
        state.erro?.let { erro ->
            snackbarHostState.showSnackbar(erro)
            viewModel.consumirErro()
        }
    }

    if (mostrarPopupSair) {
        PopupLogout(
            onDismiss = { mostrarPopupSair = false },
            onConfirm = {
                mostrarPopupSair = false
                onSairConfirm()
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Configurações do Sistema",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White
    ) { paddingValues ->

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
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
            Spacer(modifier = Modifier.height(24.dp))

            // ── SEÇÃO: GAMIFICAÇÃO ──
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = AzulPrimario,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "GAMIFICAÇÃO",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = Color(0xFF424242),
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Pontos por Livro Lido",
                fontSize = 12.sp,
                color = Color(0xFF616161),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = state.pontosPorLivro,
                onValueChange = { viewModel.atualizarPontosPorLivro(it) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                placeholder = { Text("Ex: 50", color = Color(0xFFBDBDBD)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF9F9F9),
                    focusedContainerColor = Color(0xFFF9F9F9),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = AzulPrimario
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Pontos por Resenha Aprovada",
                fontSize = 12.sp,
                color = Color(0xFF616161),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = state.pontosPorResenha,
                onValueChange = { viewModel.atualizarPontosPorResenha(it) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                placeholder = { Text("Ex: 15", color = Color(0xFFBDBDBD)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF9F9F9),
                    focusedContainerColor = Color(0xFFF9F9F9),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = AzulPrimario
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { viewModel.salvarConfiguracoes() },
                enabled = !state.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario)
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Salvar Alterações", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ── SEÇÃO: SESSÃO ──
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.PowerSettingsNew,
                    contentDescription = null,
                    tint = corVermelha,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "SESSÃO",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = Color(0xFF424242),
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(fundoVermelhoClaro, RoundedCornerShape(4.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Ao sair do sistema, todas as sessões ativas neste dispositivo serão encerradas. Certifique-se de salvar quaisquer alterações pendentes na configuração.",
                        color = corVermelha,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedButton(
                        onClick = { mostrarPopupSair = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(1.dp, corVermelha),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = corVermelha)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Sair", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

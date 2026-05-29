package com.br.unifor.for_library.feature.adm.moderacao.modresenha

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import com.br.unifor.for_library.core.designsystem.AzulPrimario
import com.br.unifor.for_library.core.designsystem.VerdeSucesso
import com.br.unifor.for_library.core.designsystem.VermelhoErro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAnaliseResenha(
    resenhaId: String,
    onVoltar: () -> Unit,
    onAprovar: () -> Unit,
    onRejeitar: (motivo: String) -> Unit
) {
    val idInt = resenhaId.toIntOrNull() ?: 0
    val viewModel: AnaliseResenhaViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
                AnaliseResenhaViewModel(idInt) as T
        }
    )

    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var motivoRejeicao by remember { mutableStateOf("") }

    LaunchedEffect(state.erro) {
        state.erro?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.consumirErro()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analisar Resenha", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── RF35.2: Card da Resenha ──────────────────────────────────────
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7)),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // Autor
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFFE3EEF9), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = AzulPrimario)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(state.autorNome, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            if (state.livroTitulo.isNotBlank()) {
                                Text(
                                    state.livroTitulo,
                                    fontSize = 12.sp,
                                    color = Color(0xFF757575)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Nota em estrelas
                    Row {
                        repeat(5) { i ->
                            Icon(
                                imageVector = if (i < state.nota) Icons.Default.Star else Icons.Outlined.Star,
                                contentDescription = null,
                                tint = if (i < state.nota) AzulPrimario else Color(0xFFBDBDBD),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "${state.nota}/5",
                            fontSize = 12.sp,
                            color = Color(0xFF757575)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Corpo da resenha
                    Label("TEXTO DA RESENHA")
                    Text(
                        text = state.texto.ifBlank { "(sem texto)" },
                        fontSize = 14.sp,
                        color = Color(0xFF424242),
                        lineHeight = 22.sp
                    )

                    if (state.dataEnvio.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Enviado em ${state.dataEnvio}",
                            fontSize = 11.sp,
                            color = Color(0xFF9E9E9E)
                        )
                    }
                }
            }

            // ── RF35.6: Motivo da Rejeição ───────────────────────────────────
            Label("MOTIVO DA REJEIÇÃO (OPCIONAL)")
            OutlinedTextField(
                value = motivoRejeicao,
                onValueChange = { motivoRejeicao = it },
                placeholder = {
                    Text(
                        "Descreva o motivo caso a resenha não cumpra as diretrizes...",
                        fontSize = 13.sp,
                        color = Color(0xFFBDBDBD)
                    )
                },
                modifier = Modifier.fillMaxWidth().height(110.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF7F7F7),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = AzulPrimario
                )
            )

            // ── RF35.3: Aviso de diretrizes (azul) ───────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8F0FE), RoundedCornerShape(8.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = AzulPrimario,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Certifique-se de que a resenha segue as diretrizes da comunidade antes de tomar uma decisão final.",
                    fontSize = 13.sp,
                    color = AzulPrimario,
                    lineHeight = 19.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // ── RF35.4/RF35.5: Botões de ação ───────────────────────────────
            Button(
                onClick = {
                    viewModel.aprovar(onSucesso = onAprovar)
                },
                enabled = !state.processando,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeSucesso)
            ) {
                if (state.processando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Aprovar Resenha", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            OutlinedButton(
                onClick = {
                    viewModel.rejeitar(motivoRejeicao, onSucesso = { onRejeitar(motivoRejeicao) })
                },
                enabled = !state.processando,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = VermelhoErro),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, VermelhoErro)
            ) {
                Icon(Icons.Default.Cancel, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Rejeitar Resenha", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun Label(text: String) {
    Text(
        text = text,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF9E9E9E),
        letterSpacing = 0.5.sp
    )
    Spacer(Modifier.height(4.dp))
}

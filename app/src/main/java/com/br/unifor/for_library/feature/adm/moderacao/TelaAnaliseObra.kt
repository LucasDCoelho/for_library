package com.br.unifor.for_library.feature.adm.moderacao

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.br.unifor.for_library.core.designsystem.AzulPrimario
import com.br.unifor.for_library.core.designsystem.VerdeSucesso
import com.br.unifor.for_library.core.designsystem.VermelhoErro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAnaliseObra(
    obraId: String,
    onVoltar: () -> Unit = {},
    onAprovar: () -> Unit = {},
    onRejeitar: (String) -> Unit = {}
) {
    val idInt = obraId.toIntOrNull() ?: 0
    val viewModel: AnaliseObraViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
                AnaliseObraViewModel(idInt) as T
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
                title = { Text("Analisar Obra", fontWeight = FontWeight.SemiBold, fontSize = 18.sp) },
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // ── Capa placeholder ──────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF2C2C2C)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📖", fontSize = 36.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = state.titulo,
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // ── Metadados da obra ─────────────────────────────────────────────
            Label("TÍTULO")
            Text(state.titulo, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Label("GÊNERO")
                    Text(state.genero, fontSize = 14.sp, color = Color(0xFF424242))
                }
                Column(modifier = Modifier.weight(1f)) {
                    Label("AUTOR")
                    Text(state.nomeAutor, fontSize = 14.sp, color = Color(0xFF424242))
                }
            }

            if (state.matriculaAutor.isNotBlank() && state.matriculaAutor != "—") {
                Label("MATRÍCULA")
                Text(state.matriculaAutor, fontSize = 14.sp, color = Color(0xFF424242))
            }

            if (state.dataEnvio.isNotBlank()) {
                Label("DATA DE ENVIO")
                Text(state.dataEnvio, fontSize = 14.sp, color = Color(0xFF424242))
            }

            HorizontalDivider(color = Color(0xFFE0E0E0))

            // ── Sinopse ───────────────────────────────────────────────────────
            if (state.sinopse.isNotBlank()) {
                Label("SINOPSE")
                Text(
                    text = state.sinopse,
                    fontSize = 13.sp,
                    color = CinzaTextoLocal,
                    lineHeight = 20.sp
                )
            }

            // ── Botão baixar PDF ──────────────────────────────────────────────
            if (state.pdfUrl.isNotBlank()) {
                OutlinedButton(
                    onClick = { /* TODO: abrir PDF via Intent */ },
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AzulPrimario),
                    border = BorderStroke(1.dp, AzulPrimario)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Baixar PDF para Análise", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            // ── Motivo da rejeição ────────────────────────────────────────────
            Label("MOTIVO DA REJEIÇÃO (OPCIONAL)")
            OutlinedTextField(
                value = motivoRejeicao,
                onValueChange = { motivoRejeicao = it },
                placeholder = {
                    Text(
                        "Descreva o motivo caso a obra não cumpra as diretrizes...",
                        fontSize = 13.sp,
                        color = Color(0xFFBDBDBD)
                    )
                },
                modifier = Modifier.fillMaxWidth().height(110.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = AzulPrimario
                )
            )

            // ── Aviso de diretrizes ───────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8F4FD), RoundedCornerShape(8.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = AzulPrimario,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = buildAnnotatedString {
                        append("Certifique-se de que a obra está de acordo com as ")
                        withStyle(
                            SpanStyle(
                                color = AzulPrimario,
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) { append("Diretrizes Editoriais") }
                        append(" antes de prosseguir com a aprovação.")
                    },
                    fontSize = 12.sp,
                    color = Color(0xFF424242),
                    lineHeight = 18.sp
                )
            }

            // ── Botão Aprovar ─────────────────────────────────────────────────
            Button(
                onClick = { viewModel.aprovar(onSucesso = onAprovar) },
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
                    Spacer(Modifier.width(8.dp))
                    Text("Aprovar Obra", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            // ── Botão Rejeitar ────────────────────────────────────────────────
            OutlinedButton(
                onClick = { viewModel.rejeitar(motivoRejeicao) { onRejeitar(motivoRejeicao) } },
                enabled = !state.processando,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = VermelhoErro),
                border = BorderStroke(1.5.dp, VermelhoErro)
            ) {
                Icon(Icons.Default.Cancel, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Rejeitar Obra", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
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
    Spacer(Modifier.height(2.dp))
}

private val CinzaTextoLocal = Color(0xFF616161)

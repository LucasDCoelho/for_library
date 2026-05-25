package com.br.unifor.for_library.feature.aluno.notificacao.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.br.unifor.for_library.feature.aluno.notificacao.viewmodel.NotificacaoUi
import com.br.unifor.for_library.feature.aluno.notificacao.viewmodel.NotificacoesViewModel

private val AzulPrimario   = Color(0xFF1565C0)
private val FundoNaoLida   = Color(0xFFE3F2FD)
private val CinzaTexto     = Color(0xFF757575)
private val CinzaClaro     = Color(0xFF9E9E9E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaNotificacoes(
    onVoltar: () -> Unit,
    viewModel: NotificacoesViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text("Notificações", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.marcarTodasComoLidas() }) {
                        Text(
                            text = "Marcar todas como lidas",
                            color = AzulPrimario,
                            fontSize = 13.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        when {
            state.isLoading -> {
                Box(
                    Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AzulPrimario)
                }
            }

            state.error != null -> {
                Box(
                    Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Não foi possível carregar as notificações.",
                            color = CinzaTexto,
                            fontSize = 14.sp
                        )
                        Spacer(Modifier.height(12.dp))
                        TextButton(onClick = { viewModel.carregarNotificacoes() }) {
                            Text("Tentar novamente", color = AzulPrimario)
                        }
                    }
                }
            }

            state.grupos.isEmpty() -> {
                Box(
                    Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nenhuma notificação por aqui.",
                        color = CinzaTexto,
                        fontSize = 14.sp
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    state.grupos.forEach { (grupo, lista) ->
                        item(key = "header_$grupo") {
                            Text(
                                text = grupo.uppercase(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CinzaTexto,
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(
                                    start = 16.dp, top = 24.dp, bottom = 8.dp
                                )
                            )
                        }

                        lista.forEach { notif ->
                            item(key = notif.id) {
                                ItemNotificacao(
                                    notif = notif,
                                    onClick = { viewModel.marcarComoLida(notif.id) }
                                )
                                HorizontalDivider(
                                    color = Color(0xFFEEEEEE),
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemNotificacao(
    notif: NotificacaoUi,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (notif.lido) Color.White else FundoNaoLida)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Indicador de não lida
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(if (notif.lido) Color.Transparent else AzulPrimario)
        )

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = notif.titulo,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = notif.mensagem,
                fontSize = 13.sp,
                color = CinzaTexto,
                lineHeight = 18.sp
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = notif.horarioFormatado,
                fontSize = 11.sp,
                color = CinzaClaro
            )
        }
    }
}

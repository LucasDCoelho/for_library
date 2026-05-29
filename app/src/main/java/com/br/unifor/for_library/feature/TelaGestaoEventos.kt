package com.br.unifor.for_library.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.br.unifor.for_library.core.designsystem.AzulPrimario
import com.br.unifor.for_library.feature.aluno.eventos.viewmodel.EventoDb

@Composable
fun TelaGestaoEventos(
    onEditarEventoClick: (String) -> Unit = {},
    onNovoEventoClick: () -> Unit = {},
    viewModel: GestaoEventosViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var eventoParaExcluir by remember { mutableStateOf<EventoDb?>(null) }

    LaunchedEffect(state.erro) {
        state.erro?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.consumirErro()
        }
    }

    if (eventoParaExcluir != null) {
        PopupExclusaoObra(
            mensagem = "Atenção: A exclusão removerá o evento para todos os alunos. Deseja continuar?",
            onDismiss = { eventoParaExcluir = null },
            onConfirm = {
                val id = eventoParaExcluir!!.id
                eventoParaExcluir = null
                viewModel.deletarEvento(id) {}
            }
        )
    }

    Scaffold(
        containerColor = Color(0xFFF9F9F9),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNovoEventoClick,
                containerColor = AzulPrimario,
                shape = RoundedCornerShape(4.dp),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Criar novo evento")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header RF31.1
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Gestão de Eventos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF222222)
                )

                Box(
                    modifier = Modifier
                        .background(Color(0xFFEEEEEE), RoundedCornerShape(4.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "TOTAL:\n${state.eventos.size}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = AzulPrimario)
                    }
                }

                state.eventos.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Nenhum evento cadastrado.",
                            color = Color(0xFF9E9E9E),
                            fontSize = 14.sp
                        )
                    }
                }

                else -> {
                    // Listagem RF31.2
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.eventos, key = { it.id }) { evento ->
                            ItemEventoAdmin(
                                evento = evento,
                                onEditarClick = { onEditarEventoClick(evento.id.toString()) },
                                onExcluirClick = { eventoParaExcluir = evento }
                            )
                            HorizontalDivider(color = Color(0xFFEBEBEB), thickness = 1.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemEventoAdmin(
    evento: EventoDb,
    onEditarClick: () -> Unit,
    onExcluirClick: () -> Unit
) {
    val (dataFormatada, horario) = remember(evento.data_inicio) {
        parseDataHorario(evento.data_inicio)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {

            // Tipo do evento
            if (evento.tipo.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFE3EEF9), RoundedCornerShape(2.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = evento.tipo.uppercase(),
                        color = AzulPrimario,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = evento.titulo,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color(0xFF757575),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (horario.isNotBlank()) "$dataFormatada • $horario"
                           else dataFormatada,
                    fontSize = 12.sp,
                    color = Color(0xFF757575)
                )
            }
        }

        Row {
            IconButton(onClick = onEditarClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar evento",
                    tint = Color(0xFF616161),
                    modifier = Modifier.size(22.dp)
                )
            }
            IconButton(onClick = onExcluirClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Excluir evento",
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

private fun parseDataHorario(dataInicio: String?): Pair<String, String> {
    if (dataInicio == null) return Pair("Sem data", "")
    return try {
        val partes = dataInicio.split("T")
        val dateParts = partes[0].split("-")
        val meses = listOf("Jan","Fev","Mar","Abr","Mai","Jun","Jul","Ago","Set","Out","Nov","Dez")
        val dia = dateParts[2].trimStart('0').ifEmpty { "0" }
        val mes = meses[dateParts[1].toInt() - 1]
        val ano = dateParts[0]
        val data = "$dia $mes $ano"

        val horario = if (partes.size > 1) {
            val timeParts = partes[1].split(":")
            "${timeParts[0]}:${timeParts[1]}"
        } else ""

        Pair(data, horario)
    } catch (e: Exception) {
        Pair(dataInicio, "")
    }
}

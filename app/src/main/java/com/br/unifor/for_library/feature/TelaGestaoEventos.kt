// ── TelaGestaoEventos.kt (corrigido) ─────────────────────────────────────────
//TO-DO -> COLOCAR A BOTTOM BAR DO ADM
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
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class StatusEvento { EM_BREVE, ANTES }

data class EventoAdmin(
    val id: String,
    val titulo: String,
    val data: String,
    val horario: String,
    val status: StatusEvento
)

@Composable
fun TelaGestaoEventos(
    onEditarEventoClick: (String) -> Unit = {},
    onNovoEventoClick: () -> Unit = {}
) {
    val listaEventos = remember {
        listOf(
            EventoAdmin("1", "Advanced Archival Techniques Workshop", "Oct 24, 2024", "10:00 AM", StatusEvento.EM_BREVE),
            EventoAdmin("2", "Children's Storytelling Hour", "Oct 28, 2024", "02:30 PM", StatusEvento.EM_BREVE),
            EventoAdmin("3", "Digital Preservation Summit", "Sept 12, 2024", "09:00 AM", StatusEvento.ANTES),
            EventoAdmin("4", "Summer Book Fair", "Aug 30, 2024", "11:00 AM", StatusEvento.ANTES),
            EventoAdmin("5", "Local Author Reading: Elena Vance", "July 15, 2024", "06:00 PM", StatusEvento.ANTES)
        )
    }

    // ✅ Fix 1: separado para futura substituição por viewModel.totalEventos
    val totalEventos = listaEventos.size // TODO: viewModel.totalEventos

    var mostrarPopupExclusao by remember { mutableStateOf(false) }
    var eventoSelecionadoParaExcluir by remember { mutableStateOf<EventoAdmin?>(null) }

    // ✅ Fix 2: reutiliza PopupExclusaoObra com mensagem de eventos
    if (mostrarPopupExclusao) {
        PopupExclusaoObra(
            mensagem = "Atenção: A exclusão removerá o evento para todos " +
                    "os alunos. Deseja continuar?",
            onDismiss = {
                mostrarPopupExclusao = false
                eventoSelecionadoParaExcluir = null
            },
            onConfirm = {
                // TODO: apagar eventoSelecionadoParaExcluir do banco de dados
                mostrarPopupExclusao = false
                eventoSelecionadoParaExcluir = null
            }
        )
    }

    Scaffold(
        containerColor = Color(0xFFF9F9F9),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNovoEventoClick() },
                containerColor = Color(0xFF1E54FA),
                shape = RoundedCornerShape(4.dp),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Criar novo evento")
            }
        }
        // ✅ Fix 3: bottomBar = { BottomBarAdmin(...) } — TODO: implementar
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ── RF31.1: Cabeçalho ─────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    // ✅ Fix 5: protege contra sobreposição da status bar
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
                    // ✅ Fix 1: variável separada, pronta para ViewModel
                    Text(
                        text = "TOTAL:\n$totalEventos",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)

            // ── RF31.2: Lista ─────────────────────────────────────────────────
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(listaEventos) { evento ->
                    ItemEventoAdmin(
                        evento = evento,
                        onEditarClick = { onEditarEventoClick(evento.id) },
                        onExcluirClick = {
                            eventoSelecionadoParaExcluir = evento
                            mostrarPopupExclusao = true
                        }
                    )
                    HorizontalDivider(color = Color(0xFFEBEBEB), thickness = 1.dp)
                }
            }
        }
    }
}

@Composable
private fun ItemEventoAdmin(
    evento: EventoAdmin,
    onEditarClick: () -> Unit,
    onExcluirClick: () -> Unit
) {
    val isEmBreve = evento.status == StatusEvento.EM_BREVE
    val corTextoPrincipal = if (isEmBreve) Color.Black else Color(0xFF888888)
    val corTextoSecundario = if (isEmBreve) Color.DarkGray else Color(0xFFAAAAAA)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {

            if (isEmBreve) {
                Text(
                    text = "EM BREVE",
                    color = Color(0xFF1E54FA),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            } else {
                Box(
                    modifier = Modifier
                        .background(Color(0xFF999999), RoundedCornerShape(2.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "ANTES",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = evento.titulo,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = corTextoPrincipal
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isEmBreve) Icons.Default.DateRange else Icons.Default.History,
                    contentDescription = null,
                    tint = corTextoSecundario,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${evento.data} • ${evento.horario}",
                    fontSize = 12.sp,
                    color = corTextoSecundario
                )
            }
        }

        // ✅ Fix 4: IconButton garante área mínima de toque de 48dp
        Row {
            IconButton(onClick = onEditarClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar evento",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(onClick = onExcluirClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Excluir evento",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun TelaGestaoEventosPreview() {
    TelaGestaoEventos()
}
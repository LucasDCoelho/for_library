package com.br.unifor.for_library.feature

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.br.unifor.for_library.core.designsystem.AzulPrimario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAdicionarEvento(
    eventoId: Int? = null,
    onVoltar: () -> Unit = {},
    onPublicar: () -> Unit = {},
    viewModel: EventosFormViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var mostrarDatePicker by remember { mutableStateOf(false) }
    var mostrarDropdownTipo by remember { mutableStateOf(false) }

    LaunchedEffect(state.sucesso) {
        if (state.sucesso) {
            snackbarHostState.showSnackbar(
                if (eventoId == null) "Evento criado com sucesso!" else "Evento atualizado!"
            )
            viewModel.consumirSucesso()
            onPublicar()
        }
    }

    LaunchedEffect(state.erro) {
        state.erro?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.consumirErro()
        }
    }

    // DatePickerDialog
    if (mostrarDatePicker) {
        val datePickerState = rememberDatePickerState(
            selectableDates = if (eventoId == null) {
                object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        val hoje = java.time.LocalDate.now()
                            .atStartOfDay(java.time.ZoneOffset.UTC)
                            .toInstant().toEpochMilli()
                        return utcTimeMillis >= hoje
                    }
                }
            } else object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long) = true
                override fun isSelectableYear(year: Int) = true
            }
        )
        DatePickerDialog(
            onDismissRequest = { mostrarDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { viewModel.onDataSelecionada(it) }
                    mostrarDatePicker = false
                }) { Text("OK", color = AzulPrimario) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDatePicker = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    val tituloTela = if (eventoId == null) "Criar Evento" else "Editar Evento"
    val labelBotao = if (eventoId == null) "Publicar Evento" else "Salvar Alterações"

    val camposPreenchidos = state.titulo.isNotBlank() &&
            state.descricao.isNotBlank() &&
            state.dataIso.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tituloTela, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White,
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onVoltar,
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(1.dp, AzulPrimario),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AzulPrimario)
                    ) {
                        Text("Cancelar", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { viewModel.salvar(onPublicar) },
                        enabled = camposPreenchidos && !state.isLoading,
                        modifier = Modifier.weight(2f).height(48.dp),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario)
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(labelBotao, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título
            CampoFormEvento(label = "Título do Evento *") {
                OutlinedTextField(
                    value = state.titulo,
                    onValueChange = viewModel::onTituloChange,
                    placeholder = { Text("Ex: Workshop de Escrita Criativa", fontSize = 14.sp) },
                    isError = state.erroTitulo != null,
                    supportingText = state.erroTitulo?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = campoColors()
                )
            }

            // Descrição
            CampoFormEvento(label = "Descrição *") {
                OutlinedTextField(
                    value = state.descricao,
                    onValueChange = viewModel::onDescricaoChange,
                    placeholder = { Text("Detalhes sobre o evento...", fontSize = 14.sp) },
                    isError = state.erroDescricao != null,
                    supportingText = state.erroDescricao?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = campoColors()
                )
            }

            // Tipo (Dropdown)
            CampoFormEvento(label = "Tipo de Evento") {
                ExposedDropdownMenuBox(
                    expanded = mostrarDropdownTipo,
                    onExpandedChange = { mostrarDropdownTipo = it }
                ) {
                    OutlinedTextField(
                        value = state.tipo.replaceFirstChar { it.uppercase() },
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Selecione o tipo", fontSize = 14.sp) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = mostrarDropdownTipo) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(4.dp),
                        colors = campoColors()
                    )
                    ExposedDropdownMenu(
                        expanded = mostrarDropdownTipo,
                        onDismissRequest = { mostrarDropdownTipo = false }
                    ) {
                        tiposEvento.forEach { tipo ->
                            DropdownMenuItem(
                                text = { Text(tipo) },
                                onClick = {
                                    viewModel.onTipoChange(tipo)
                                    mostrarDropdownTipo = false
                                }
                            )
                        }
                    }
                }
            }

            // Data e Horário
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Data
                Column(modifier = Modifier.weight(1f)) {
                    LabelCampoEvento("Data *")
                    OutlinedTextField(
                        value = state.dataExibicao,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Selecionar", fontSize = 14.sp) },
                        trailingIcon = {
                            IconButton(onClick = { mostrarDatePicker = true }) {
                                Icon(
                                    Icons.Default.CalendarToday,
                                    contentDescription = "Abrir calendário",
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        },
                        isError = state.erroData != null,
                        supportingText = state.erroData?.let { { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 11.sp) } },
                        shape = RoundedCornerShape(4.dp),
                        colors = campoColors()
                    )
                }

                // Horário
                Column(modifier = Modifier.weight(1f)) {
                    LabelCampoEvento("Horário")
                    OutlinedTextField(
                        value = state.horario,
                        onValueChange = viewModel::onHorarioChange,
                        placeholder = { Text("HH:mm", fontSize = 14.sp) },
                        trailingIcon = {
                            Icon(
                                Icons.Default.AccessTime,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        isError = state.erroHorario != null,
                        supportingText = state.erroHorario?.let { { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 11.sp) } },
                        shape = RoundedCornerShape(4.dp),
                        colors = campoColors()
                    )
                }
            }

            // Local / Link
            CampoFormEvento(label = "Local (físico) ou Link (online)") {
                OutlinedTextField(
                    value = state.endereco,
                    onValueChange = viewModel::onEnderecoChange,
                    placeholder = { Text("Sala 302 ou meet.google.com/...", fontSize = 14.sp) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = campoColors()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun CampoFormEvento(label: String, content: @Composable () -> Unit) {
    Column {
        LabelCampoEvento(label)
        content()
    }
}

@Composable
private fun LabelCampoEvento(texto: String) {
    Text(
        text = texto,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF424242),
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun campoColors() = OutlinedTextFieldDefaults.colors(
    unfocusedContainerColor = Color(0xFFF5F5F5),
    focusedContainerColor = Color(0xFFF5F5F5),
    unfocusedBorderColor = Color(0xFFE0E0E0),
    focusedBorderColor = AzulPrimario
)

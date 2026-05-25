package com.br.unifor.for_library.feature.aluno.eventos.ui

import android.content.Intent
import android.provider.CalendarContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.br.unifor.for_library.feature.aluno.eventos.viewmodel.DetalhesEventoViewModel
import com.br.unifor.for_library.feature.aluno.eventos.viewmodel.EventoDetalhesUi
import kotlinx.coroutines.launch

private val AzulPrimario = Color(0xFF1565C0)

@Composable
fun TelaDetalhesEvento(
    eventoId: Int = 1,
    onVoltar: () -> Unit = {},
    viewModel: DetalhesEventoViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(eventoId) {
        viewModel.carregarEvento(eventoId)
    }

    val calendarLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        scope.launch {
            snackbarHostState.showSnackbar("Evento adicionado ao calendário!")
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = Color(0xFF2E7D32),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AzulPrimario)
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            text = "Não foi possível carregar o evento.",
                            color = Color(0xFF616161),
                            fontSize = 14.sp
                        )
                        Button(
                            onClick = { viewModel.carregarEvento(eventoId) },
                            colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Tentar novamente")
                        }
                    }
                }
            }

            state.evento != null -> {
                val evento = state.evento!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    BannerEvento(evento = evento, onVoltar = onVoltar)
                    ConteudoEvento(
                        evento = evento,
                        onAdicionarCalendario = {
                            val intent = Intent(
                                Intent.ACTION_INSERT,
                                CalendarContract.Events.CONTENT_URI
                            ).apply {
                                putExtra(CalendarContract.Events.TITLE, evento.titulo)
                                putExtra(CalendarContract.Events.DESCRIPTION, evento.sobreEvento)
                                val localizacao = listOf(evento.local, evento.complemento)
                                    .filter { it.isNotEmpty() }
                                    .joinToString(" — ")
                                putExtra(CalendarContract.Events.EVENT_LOCATION, localizacao)
                                if (evento.dtStartMillis > 0L)
                                    putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, evento.dtStartMillis)
                                if (evento.dtEndMillis > 0L)
                                    putExtra(CalendarContract.EXTRA_EVENT_END_TIME, evento.dtEndMillis)
                            }
                            calendarLauncher.launch(intent)
                        }
                    )
                }
            }
        }
    }
}

// ── Banner com overlay, botão voltar e badge de tipo ─────────────────────────

@Composable
private fun BannerEvento(evento: EventoDetalhesUi, onVoltar: () -> Unit) {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxWidth().height(240.dp)) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .data(evento.bannerUrl)
                .crossfade(true)
                .build(),
            contentDescription = evento.titulo,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            loading = {
                Box(Modifier.fillMaxSize().background(Color(0xFF1A237E)))
            },
            error = {
                Box(
                    Modifier.fillMaxSize().background(
                        Brush.linearGradient(listOf(Color(0xFF0D47A1), Color(0xFF1565C0)))
                    )
                )
            }
        )

        // Gradiente inferior para legibilidade do título
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .align(Alignment.BottomStart)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f))
                    )
                )
        )

        // Botão voltar (canto superior esquerdo)
        IconButton(
            onClick = onVoltar,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(50))
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = Color.White
            )
        }

        // Badge de tipo + título (canto inferior esquerdo)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            if (evento.tipo.isNotEmpty()) {
                Surface(shape = RoundedCornerShape(4.dp), color = AzulPrimario) {
                    Text(
                        evento.tipo,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Spacer(Modifier.height(6.dp))
            }
            Text(
                evento.titulo,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                lineHeight = 24.sp
            )
        }
    }
}

// ── Conteúdo abaixo do banner ─────────────────────────────────────────────────

@Composable
private fun ConteudoEvento(
    evento: EventoDetalhesUi,
    onAdicionarCalendario: () -> Unit
) {
    Column(modifier = Modifier.padding(20.dp)) {
        HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 0.5.dp)
        Spacer(Modifier.height(20.dp))

        // Data e Hora (RF17.2)
        Row(verticalAlignment = Alignment.Top) {
            Icon(
                Icons.Default.CalendarToday,
                contentDescription = null,
                tint = AzulPrimario,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "DATA E HORA",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF9E9E9E),
                    letterSpacing = 0.8.sp
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    evento.dataFormatada.ifEmpty { "A definir" },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF212121)
                )
                if (evento.horarioInicio.isNotEmpty()) {
                    val horario = buildString {
                        append(evento.horarioInicio)
                        if (evento.horarioFim.isNotEmpty()) append(" – ${evento.horarioFim}")
                        append(" (${evento.fusoHorario})")
                    }
                    Text(horario, fontSize = 13.sp, color = Color(0xFF616161))
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Localização (RF17.3)
        Row(verticalAlignment = Alignment.Top) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = AzulPrimario,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "LOCALIZAÇÃO",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF9E9E9E),
                    letterSpacing = 0.8.sp
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    evento.local.ifEmpty { "A definir" },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF212121)
                )
                if (evento.complemento.isNotEmpty()) {
                    Text(evento.complemento, fontSize = 13.sp, color = Color(0xFF616161))
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 0.5.dp)
        Spacer(Modifier.height(20.dp))

        // Sobre o Evento (RF17.4)
        Text(
            "Sobre o Evento",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121)
        )
        Spacer(Modifier.height(10.dp))
        Text(
            evento.sobreEvento.ifEmpty { "Informações em breve." },
            fontSize = 14.sp,
            color = Color(0xFF616161),
            lineHeight = 22.sp
        )

        Spacer(Modifier.height(28.dp))

        // Botão Calendário (RF17.5)
        Button(
            onClick = onAdicionarCalendario,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario)
        ) {
            Icon(
                Icons.Default.Event,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Adicionar ao Meu Calendário",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}

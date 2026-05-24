package com.br.unifor.for_library.feature.aluno.livro.ui

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.br.unifor.for_library.feature.aluno.livro.viewmodel.LeitorViewModel
import kotlinx.coroutines.launch

private val AzulPrimario      = Color(0xFF1565C0)
private val AzulPrimarioClaro = Color(0xFFE3EEF9)

@Composable
fun TelaLeitorDigital(
    livroId: String,
    tituloLivro: String,
    onVoltar: () -> Unit,
    onIrAvaliarLivro: () -> Unit,
    viewModel: LeitorViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val livroIdInt = remember(livroId) { livroId.toIntOrNull() ?: 0 }

    LaunchedEffect(livroIdInt) {
        if (livroIdInt > 0) viewModel.carregarLivro(livroIdInt)
    }

    when {
        state.isLoading -> LoadingLeitor()
        state.error != null -> ErroLeitor(onVoltar)
        state.pdfPronto -> LeitorContent(
            tituloLivro = tituloLivro,
            totalPaginas = state.totalPaginas,
            paginaInicial = state.paginaInicial,
            pontosGanhos = state.pontosGanhos,
            onVoltar = onVoltar,
            onIrAvaliarLivro = onIrAvaliarLivro,
            renderizarPagina = { idx, w -> viewModel.renderizarPagina(idx, w) },
            salvarProgresso = { pagina -> viewModel.salvarProgresso(pagina) },
            concluirLeitura = { viewModel.concluirLeitura() }
        )
    }
}

@Composable
private fun LoadingLeitor() {
    Box(
        Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(color = AzulPrimario)
            Text("Carregando livro...", color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
private fun ErroLeitor(onVoltar: () -> Unit) {
    Box(
        Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                "Não foi possível carregar o livro.",
                color = Color(0xFF616161),
                fontSize = 14.sp
            )
            Button(
                onClick = onVoltar,
                colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            ) { Text("Voltar") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LeitorContent(
    tituloLivro: String,
    totalPaginas: Int,
    paginaInicial: Int,
    pontosGanhos: Int,
    onVoltar: () -> Unit,
    onIrAvaliarLivro: () -> Unit,
    renderizarPagina: suspend (Int, Int) -> Bitmap?,
    salvarProgresso: (Int) -> Unit,
    concluirLeitura: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = paginaInicial) { totalPaginas }
    val coroutineScope = rememberCoroutineScope()
    var mostrarPopupFim by remember { mutableStateOf(false) }
    var showControls by remember { mutableStateOf(true) }

    LaunchedEffect(pagerState.currentPage) {
        if (totalPaginas > 0 && pagerState.currentPage == totalPaginas - 1) {
            salvarProgresso(pagerState.currentPage)
            concluirLeitura()
            mostrarPopupFim = true
        }
    }

    DisposableEffect(Unit) {
        onDispose { salvarProgresso(pagerState.currentPage) }
    }

    Box(Modifier.fillMaxSize().background(Color(0xFF1A1A1A))) {

        // ── RF10.2 + RF10.3: Pager com renderização PDF real ─────────────────
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { pageIndex ->
            var bitmap by remember(pageIndex) { mutableStateOf<Bitmap?>(null) }

            BoxWithConstraints(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                val widthPx = with(LocalDensity.current) { maxWidth.roundToPx() }

                LaunchedEffect(pageIndex, widthPx) {
                    bitmap = renderizarPagina(pageIndex, widthPx)
                }

                if (bitmap != null) {
                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = "Página ${pageIndex + 1}",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    CircularProgressIndicator(color = AzulPrimario)
                }
            }
        }

        // ── Overlay para capturar tap e alternar controles ────────────────────
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { showControls = !showControls })
                }
        )

        // ── RF10.1: Barra superior ────────────────────────────────────────────
        AnimatedVisibility(
            visible = showControls,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit  = slideOutVertically(targetOffsetY  = { -it }) + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Surface(color = Color.White.copy(alpha = 0.95f), shadowElevation = 4.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .height(64.dp)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                    Text(
                        text = tituloLivro,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // ── RF10.4: Barra inferior com slider e setas ─────────────────────────
        AnimatedVisibility(
            visible = showControls,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit  = slideOutVertically(targetOffsetY  = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Surface(color = Color.White.copy(alpha = 0.95f), shadowElevation = 8.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Slider(
                        value = pagerState.currentPage.toFloat(),
                        onValueChange = { v ->
                            coroutineScope.launch { pagerState.scrollToPage(v.toInt()) }
                        },
                        valueRange = 0f..(totalPaginas - 1).coerceAtLeast(1).toFloat(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = SliderDefaults.colors(
                            activeTrackColor   = AzulPrimario,
                            inactiveTrackColor = AzulPrimarioClaro
                        ),
                        thumb = {
                            Box(Modifier.size(20.dp).background(AzulPrimario, CircleShape))
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = {
                            coroutineScope.launch {
                                if (pagerState.currentPage > 0)
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }) {
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Página anterior",
                                tint = Color.Gray
                            )
                            Text("Anterior", color = Color.Gray, fontSize = 12.sp)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.AutoMirrored.Outlined.MenuBook,
                                contentDescription = null,
                                tint = AzulPrimario,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                "Página ${pagerState.currentPage + 1} de $totalPaginas",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulPrimario
                            )
                        }

                        TextButton(onClick = {
                            coroutineScope.launch {
                                if (pagerState.currentPage < totalPaginas - 1)
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }) {
                            Text("Próximo", color = Color.Gray, fontSize = 12.sp)
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Próxima página",
                                tint = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }

    // ── RF11: Popup de fim de leitura ─────────────────────────────────────────
    if (mostrarPopupFim) {
        PopupFimLeitura(
            pontosGanhos = pontosGanhos,
            onAvaliarLivro = { mostrarPopupFim = false; onIrAvaliarLivro() },
            onFechar       = { mostrarPopupFim = false; onVoltar() }
        )
    }
}

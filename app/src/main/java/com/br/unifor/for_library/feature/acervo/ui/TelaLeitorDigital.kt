package com.br.unifor.for_library.feature.acervo.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// ---------------------------------------------------------------------------
// Cor centralizada — futuramente migrar para MaterialTheme.colorScheme
// ---------------------------------------------------------------------------
private val AzulPrimario     = Color(0xFF1565C0)
private val AzulPrimarioClaro = Color(0xFFE3EEF9)

@Composable
fun TelaLeitorDigital(
    tituloLivro: String,
    onVoltar: () -> Unit
) {
    val totalPages = 340
    val pagerState = rememberPagerState(pageCount = { totalPages })
    val coroutineScope = rememberCoroutineScope()

    var mostrarPopupFim by remember { mutableStateOf(false) }
    var showControls    by remember { mutableStateOf(true) }

    // RF11.1: ao chegar na última página, exibe o popup de fim de leitura
    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage == totalPages - 1) {
            mostrarPopupFim = true
        }
    }

    // RF10.5: salva o progresso ao sair da tela
    // TODO: substituir o println pela chamada real ao repositório:
    //       repositorio.salvarProgresso(livroId, paginaSalva)
    DisposableEffect(Unit) {
        onDispose {
            val paginaSalva = pagerState.currentPage + 1
            println("Log: Salvando no banco de dados a página $paginaSalva do livro '$tituloLivro'")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // ── RF10.2 e RF10.3: Conteúdo em tela cheia com swipe horizontal ─────
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            // TODO: substituir por renderizador de PDF/ePub real
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ele virou a página. O papel era firme, uma textura que uma tela nunca poderia substituir...\n\n(Página simulada ${page + 1})",
                    fontSize = 18.sp,
                    lineHeight = 28.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Justify
                )
            }
        }

        // ── RF10.4: Overlay transparente dedicado para capturar taps ─────────
        // CORREÇÃO: o pointerInput foi movido para uma camada separada acima do
        // HorizontalPager. Antes estava no Box pai, causando conflito entre o
        // detectTapGestures e o gesture de swipe do Pager — a barra podia sumir
        // no meio de uma troca de página.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { showControls = !showControls }
                    )
                }
        )

        // ── RF10.1: Barra Superior (Animada) ─────────────────────────────────
        AnimatedVisibility(
            visible = showControls,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit  = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Surface(
                color = Color.White.copy(alpha = 0.95f),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .height(64.dp)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                    // CORREÇÃO: adicionado TextOverflow.Ellipsis para títulos longos
                    // não serem cortados abruptamente.
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

        // ── RF10.4: Barra Inferior com Slider (Animada) ──────────────────────
        AnimatedVisibility(
            visible = showControls,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit  = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Surface(
                color = Color.White.copy(alpha = 0.95f),
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    @OptIn(ExperimentalMaterial3Api::class)
                    Slider(
                        value = pagerState.currentPage.toFloat(),
                        onValueChange = { newValue ->
                            coroutineScope.launch {
                                pagerState.scrollToPage(newValue.toInt())
                            }
                        },
                        valueRange = 0f..(totalPages - 1).toFloat(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = SliderDefaults.colors(
                            activeTrackColor   = AzulPrimario,
                            inactiveTrackColor = AzulPrimarioClaro
                        ),
                        thumb = {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(AzulPrimario, shape = CircleShape)
                            )
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Botão: página anterior
                        TextButton(
                            onClick = {
                                coroutineScope.launch {
                                    if (pagerState.currentPage > 0) {
                                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Página anterior",
                                tint = Color.Gray
                            )
                            Text("Anterior", color = Color.Gray, fontSize = 12.sp)
                        }

                        // Centro: ícone + número da página atual
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.MenuBook,
                                contentDescription = null,
                                tint = AzulPrimario,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Página ${pagerState.currentPage + 1} de $totalPages",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulPrimario
                            )
                        }

                        // Botão: próxima página
                        TextButton(
                            onClick = {
                                coroutineScope.launch {
                                    if (pagerState.currentPage < totalPages - 1) {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                }
                            }
                        ) {
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

    // ── RF11: Popup de fim de leitura ────────────────────────────────────────
    if (mostrarPopupFim) {
        PopupFimLeitura(
            pontosGanhos = 50,
            onAvaliarLivro = {
                mostrarPopupFim = false
                // TODO: navegar para a Tela de Resenha (RF14)
            },
            onFechar = {
                mostrarPopupFim = false
                onVoltar()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TelaLeitorDigitalPreview() {
    TelaLeitorDigital(
        tituloLivro = "O Horizonte de Eventos",
        onVoltar = {}
    )
}
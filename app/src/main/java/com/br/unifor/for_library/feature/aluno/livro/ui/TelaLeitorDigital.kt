package com.br.unifor.for_library.feature.aluno.livro.ui

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
// Cor centralizada â€” futuramente migrar para MaterialTheme.colorScheme
// ---------------------------------------------------------------------------
private val AzulPrimario     = Color(0xFF1565C0)
private val AzulPrimarioClaro = Color(0xFFE3EEF9)

@Composable
fun TelaLeitorDigital(
    tituloLivro: String,
    onVoltar: () -> Unit,
    onIrAvaliarLivro: () -> Unit
) {
    val totalPages = 340
    val pagerState = rememberPagerState(pageCount = { totalPages })
    val coroutineScope = rememberCoroutineScope()

    var mostrarPopupFim by remember { mutableStateOf(false) }
    var showControls    by remember { mutableStateOf(true) }

    // RF11.1: ao chegar na Ãºltima pÃ¡gina, exibe o popup de fim de leitura
    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage == totalPages - 1) {
            mostrarPopupFim = true
        }
    }

    // RF10.5: salva o progresso ao sair da tela
    // TODO: substituir o println pela chamada real ao repositÃ³rio:
    //       repositorio.salvarProgresso(livroId, paginaSalva)
    DisposableEffect(Unit) {
        onDispose {
            val paginaSalva = pagerState.currentPage + 1
            println("Log: Salvando no banco de dados a pÃ¡gina $paginaSalva do livro '$tituloLivro'")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // â”€â”€ RF10.2 e RF10.3: ConteÃºdo em tela cheia com swipe horizontal â”€â”€â”€â”€â”€
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
                    text = "Ele virou a pÃ¡gina. O papel era firme, uma textura que uma tela nunca poderia substituir...\n\n(PÃ¡gina simulada ${page + 1})",
                    fontSize = 18.sp,
                    lineHeight = 28.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Justify
                )
            }
        }

        // â”€â”€ RF10.4: Overlay transparente dedicado para capturar taps â”€â”€â”€â”€â”€â”€â”€â”€â”€
        // CORREÃ‡ÃƒO: o pointerInput foi movido para uma camada separada acima do
        // HorizontalPager. Antes estava no Box pai, causando conflito entre o
        // detectTapGestures e o gesture de swipe do Pager â€” a barra podia sumir
        // no meio de uma troca de pÃ¡gina.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { showControls = !showControls }
                    )
                }
        )

        // â”€â”€ RF10.1: Barra Superior (Animada) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
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
                    // CORREÃ‡ÃƒO: adicionado TextOverflow.Ellipsis para tÃ­tulos longos
                    // nÃ£o serem cortados abruptamente.
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

        // â”€â”€ RF10.4: Barra Inferior com Slider (Animada) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
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
                        // BotÃ£o: pÃ¡gina anterior
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
                                contentDescription = "PÃ¡gina anterior",
                                tint = Color.Gray
                            )
                            Text("Anterior", color = Color.Gray, fontSize = 12.sp)
                        }

                        // Centro: Ã­cone + nÃºmero da pÃ¡gina atual
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.MenuBook,
                                contentDescription = null,
                                tint = AzulPrimario,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "PÃ¡gina ${pagerState.currentPage + 1} de $totalPages",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulPrimario
                            )
                        }

                        // BotÃ£o: prÃ³xima pÃ¡gina
                        TextButton(
                            onClick = {
                                coroutineScope.launch {
                                    if (pagerState.currentPage < totalPages - 1) {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                }
                            }
                        ) {
                            Text("PrÃ³ximo", color = Color.Gray, fontSize = 12.sp)
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "PrÃ³xima pÃ¡gina",
                                tint = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }

    // â”€â”€ RF11: Popup de fim de leitura â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    if (mostrarPopupFim) {
        PopupFimLeitura(
            pontosGanhos = 50,
            onAvaliarLivro = {
                mostrarPopupFim = false
                onIrAvaliarLivro()
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
        onVoltar = {},
        onIrAvaliarLivro = {}
    )
}


package com.br.unifor.for_library.feature.aluno.eventos.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

// â”€â”€ Cores â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
private val AzulPrimario = Color(0xFF1565C0)
private val FundoTela    = Color(0xFFF5F7FA)
private val CinzaTexto   = Color(0xFF616161)

// â”€â”€ Modelos â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
enum class TipoEvento(val label: String) {
    TODOS("Todos"),
    WORKSHOP("Workshops"),
    PALESTRA("Palestras"),
    LANCAMENTO("LanÃ§amentos")
}

data class Evento(
    val id: Int,
    val titulo: String,
    val local: String,
    val descricao: String,
    val mes: String,
    val dia: String,
    val tipo: TipoEvento,
    val bannerUrl: String = ""
)

// â”€â”€ Mock â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
private val mockEventos = listOf(
    Evento(
        id = 1,
        titulo = "Workshop de Escrita Criativa",
        local = "AuditÃ³rio Central",
        descricao = "Desenvolva suas habilidades narrativas com tÃ©cnicas prÃ¡ticas de estruturaÃ§Ã£o de personagens.",
        mes = "MAI",
        dia = "15",
        tipo = TipoEvento.WORKSHOP,
        bannerUrl = "https://images.unsplash.com/photo-1455390582262-044cdead277a?w=600"
    ),
    Evento(
        id = 2,
        titulo = "Noite de Poesia ContemporÃ¢nea",
        local = "Sala de Leitura 04",
        descricao = "Um encontro dedicado a explorar as novas vozes da poesia nacional com leitura aberta ao pÃºblico.",
        mes = "MAI",
        dia = "22",
        tipo = TipoEvento.PALESTRA,
        bannerUrl = "https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=600"
    ),
    Evento(
        id = 3,
        titulo = "LanÃ§amento: O Eco do SilÃªncio",
        local = "Foyer Principal",
        descricao = "SessÃ£o de autÃ³grafos e bate-papo com a autora premiada Marina Silva sobre seu novo romance.",
        mes = "JUN",
        dia = "05",
        tipo = TipoEvento.LANCAMENTO,
        bannerUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=600"
    ),
    Evento(
        id = 4,
        titulo = "Palestra: O Futuro da Literatura Digital",
        local = "AuditÃ³rio B",
        descricao = "Como as novas tecnologias estÃ£o transformando a forma de escrever e consumir literatura.",
        mes = "JUN",
        dia = "18",
        tipo = TipoEvento.PALESTRA,
        bannerUrl = "https://images.unsplash.com/photo-1519389950473-47ba0277781c?w=600"
    ),
    Evento(
        id = 5,
        titulo = "Workshop: RevisÃ£o e EdiÃ§Ã£o de Textos",
        local = "Sala de Leitura 02",
        descricao = "Aprenda tÃ©cnicas profissionais de revisÃ£o para aprimorar seus textos antes da publicaÃ§Ã£o.",
        mes = "JUL",
        dia = "03",
        tipo = TipoEvento.WORKSHOP,
        bannerUrl = "https://images.unsplash.com/photo-1554415707-6e8cfc93fe23?w=600"
    )
)

// â”€â”€ Tela principal â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
@Composable
fun TelaEventos(
    onSinoClick: () -> Unit = {},
    onEventoClick: (Int) -> Unit = {}
) {
    var filtroSelecionado by remember { mutableStateOf(TipoEvento.TODOS) }

    val eventosFiltrados = remember(filtroSelecionado) {
        if (filtroSelecionado == TipoEvento.TODOS) mockEventos
        else mockEventos.filter { it.tipo == filtroSelecionado }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoTela)
    ) {
        // â”€â”€ Header (RF16.1) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(AzulPrimario),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = "Eventos LiterÃ¡rios",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A2E)
            )

            // Sino (RF16.1)
            IconButton(
                onClick = onSinoClick,
                modifier = Modifier.size(38.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "NotificaÃ§Ãµes",
                    tint = Color(0xFF424242),
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // â”€â”€ Filtros (RF16.2) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(TipoEvento.entries) { tipo ->
                val selecionado = tipo == filtroSelecionado
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (selecionado) AzulPrimario else Color(0xFFEEEEEE))
                        .clickable { filtroSelecionado = tipo }
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = tipo.label.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (selecionado) Color.White else Color(0xFF616161),
                        letterSpacing = 0.4.sp
                    )
                }
            }
        }

        HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFE0E0E0))

        Spacer(modifier = Modifier.height(12.dp))

        // â”€â”€ Lista de cards (RF16.3) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(eventosFiltrados, key = { it.id }) { evento ->
                CardEvento(
                    evento = evento,
                    onClick = { onEventoClick(evento.id) }
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

// â”€â”€ Card de Evento (RF16.4 + RF16.5) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
@Composable
private fun CardEvento(
    evento: Evento,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Banner com badge de data sobreposta
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp)
            ) {
                // Imagem do banner
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(evento.bannerUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = evento.titulo,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFFCFD8DC))
                        )
                    },
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(Color(0xFF1A237E), Color(0xFF1565C0))
                                    )
                                )
                        )
                    }
                )

                // Gradiente sutil na base da imagem para legibilidade
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .align(Alignment.BottomStart)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.25f))
                            )
                        )
                )

                // Badge de data (RF16.4)
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape(8.dp))
                        .background(AzulPrimario)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = evento.mes,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = evento.dia,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 22.sp
                    )
                }
            }

            // ConteÃºdo do card
            Column(
                modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 10.dp)
            ) {
                // TÃ­tulo
                Text(
                    text = evento.titulo,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A2E),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(5.dp))

                // Local
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = CinzaTexto,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = evento.local,
                        fontSize = 12.sp,
                        color = CinzaTexto
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // DescriÃ§Ã£o
                Text(
                    text = evento.descricao,
                    fontSize = 13.sp,
                    color = Color(0xFF757575),
                    lineHeight = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                // BotÃ£o seta (RF16.5)
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(AzulPrimario)
                            .clickable { onClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Ver detalhes",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}


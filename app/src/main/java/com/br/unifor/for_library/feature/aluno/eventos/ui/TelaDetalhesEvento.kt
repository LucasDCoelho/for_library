package com.br.unifor.for_library.feature.aluno.eventos.ui

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

private val AzulPrimario = Color(0xFF1565C0)

private data class DetalhesEvento(
    val id: Int, val tipo: String, val titulo: String,
    val dataHora: String, val horario: String,
    val local: String, val endereco: String,
    val sobreEvento: String, val bannerUrl: String
)

private val mockDetalhes = listOf(
    DetalhesEvento(1, "WORKSHOP", "Introdução à Caligrafia Moderna",
        "24 de Outubro, 2024", "14:30 – 17:00 (GMT-3)",
        "Sala de Estudos B, 2º Andar", "Biblioteca Central Municipal",
        "Junte-se a nós para uma tarde imersiva dedicada à arte da escrita. Neste workshop prático, exploraremos as técnicas fundamentais da caligrafia moderna, desde a postura correta e manuseio da pena até a formação de alfabetos estilizados.\n\nIdeal para iniciantes e entusiastas do design, o curso fornecerá todo o material necessário (papel de alta gramatura, tintas nanquim e cabos). Não é necessária experiência prévia com artes visuais.",
        "https://images.unsplash.com/photo-1455390582262-044cdead277a?w=600"),
    DetalhesEvento(2, "PALESTRA", "Noite de Poesia Contemporânea",
        "22 de Maio, 2026", "19:00 – 21:00 (GMT-3)",
        "Sala de Leitura 04", "Biblioteca Central Municipal",
        "Um encontro dedicado a explorar as novas vozes da poesia nacional com leitura aberta ao público.",
        "https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=600"),
    DetalhesEvento(3, "LANÇAMENTO", "Lançamento: O Eco do Silêncio",
        "05 de Junho, 2026", "18:00 – 20:00 (GMT-3)",
        "Foyer Principal", "Biblioteca Central Municipal",
        "Sessão de autógrafos e bate-papo com a autora premiada Marina Silva sobre seu novo romance.",
        "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=600"),
    DetalhesEvento(4, "PALESTRA", "O Futuro da Literatura Digital",
        "18 de Junho, 2026", "15:00 – 17:00 (GMT-3)",
        "Auditório B", "Biblioteca Central Municipal",
        "Como as novas tecnologias estão transformando a forma de escrever e consumir literatura.",
        "https://images.unsplash.com/photo-1519389950473-47ba0277781c?w=600"),
    DetalhesEvento(5, "WORKSHOP", "Revisão e Edição de Textos",
        "03 de Julho, 2026", "09:00 – 12:00 (GMT-3)",
        "Sala de Leitura 02", "Biblioteca Central Municipal",
        "Aprenda técnicas profissionais de revisão para aprimorar seus textos antes da publicação.",
        "https://images.unsplash.com/photo-1554415707-6e8cfc93fe23?w=600")
)

@Composable
fun TelaDetalhesEvento(
    eventoId: Int = 1,
    onVoltar: () -> Unit = {},
    onAdicionarCalendario: () -> Unit = {}
) {
    val evento = mockDetalhes.find { it.id == eventoId } ?: mockDetalhes.first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Banner com overlay
        Box(modifier = Modifier.fillMaxWidth().height(240.dp)) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(evento.bannerUrl).crossfade(true).build(),
                contentDescription = evento.titulo,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = { Box(Modifier.fillMaxSize().background(Color(0xFF1A237E))) },
                error = {
                    Box(Modifier.fillMaxSize().background(
                        Brush.linearGradient(listOf(Color(0xFF0D47A1), Color(0xFF1565C0)))
                    ))
                }
            )
            Box(
                modifier = Modifier.fillMaxWidth().height(120.dp).align(Alignment.BottomStart)
                    .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))))
            )
            IconButton(
                onClick = onVoltar,
                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
                    .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(50))
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
            }
            Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                Surface(shape = RoundedCornerShape(4.dp), color = AzulPrimario) {
                    Text(evento.tipo, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                        color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                }
                Spacer(Modifier.height(6.dp))
                Text(evento.titulo, fontSize = 20.sp, fontWeight = FontWeight.Bold,
                    color = Color.White, lineHeight = 24.sp)
            }
        }

        Column(modifier = Modifier.padding(20.dp)) {
            HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 0.5.dp)
            Spacer(Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.Top) {
                Icon(Icons.Default.CalendarToday, contentDescription = null,
                    tint = AzulPrimario, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("DATA E HORA", fontSize = 11.sp, fontWeight = FontWeight.Bold,
                        color = Color(0xFF9E9E9E), letterSpacing = 0.8.sp)
                    Spacer(Modifier.height(3.dp))
                    Text(evento.dataHora, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF212121))
                    Text(evento.horario, fontSize = 13.sp, color = Color(0xFF616161))
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.Top) {
                Icon(Icons.Default.LocationOn, contentDescription = null,
                    tint = AzulPrimario, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("LOCALIZAÇÃO", fontSize = 11.sp, fontWeight = FontWeight.Bold,
                        color = Color(0xFF9E9E9E), letterSpacing = 0.8.sp)
                    Spacer(Modifier.height(3.dp))
                    Text(evento.local, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF212121))
                    Text(evento.endereco, fontSize = 13.sp, color = Color(0xFF616161))
                }
            }

            Spacer(Modifier.height(24.dp))
            HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 0.5.dp)
            Spacer(Modifier.height(20.dp))

            Text("Sobre o Evento", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
            Spacer(Modifier.height(10.dp))
            Text(evento.sobreEvento, fontSize = 14.sp, color = Color(0xFF616161), lineHeight = 22.sp)

            Spacer(Modifier.height(28.dp))

            Button(
                onClick = onAdicionarCalendario,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario)
            ) {
                Icon(Icons.Default.Event, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Adicionar ao Meu Calendário", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}


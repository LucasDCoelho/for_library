package com.br.unifor.for_library.feature.`notificaçao`.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Modelos de Dados
data class Notificacao(
    val id: Int,
    val titulo: String,
    val mensagem: String,
    val horario: String,
    val icone: ImageVector,
    val lida: Boolean
)

data class SecaoNotificacao(
    val tituloSecao: String,
    val notificacoes: List<Notificacao>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaNotificacoes(
    onVoltar: () -> Unit
) {
    val azulPrimario = Color(0xFF1E88E5)
    val cinzaTexto = Color(0xFF757575)
    val fundoNaoLida = Color(0xFFF8F9FA)
    val fundoLida = Color.White

    // Exemplosudos
    val secoes = listOf(
        SecaoNotificacao(
            tituloSecao = "HOJE",
            notificacoes = listOf(
                Notificacao(1, "Sua resenha foi aprovada!", "Parabéns! Sua análise de \"Dom Casmurro\" agora está visível para a comunidade.", "10:45", Icons.Default.ChatBubbleOutline, false),
                Notificacao(2, "Novo evento literário disponível!", "O Clube de Leitura DarkRomanceLovers acaba de abrir vagas para o debate de Sábado.", "08:20", Icons.Default.CalendarMonth, false)
            )
        ),
        SecaoNotificacao(
            tituloSecao = "ONTEM",
            notificacoes = listOf(
                Notificacao(3, "Livro salvo atualizado", "\"A Metamorfose\" recebeu uma nova edição digital na biblioteca.", "Ontem, 16:30", Icons.Default.BookmarkBorder, true)
            )
        ),
        SecaoNotificacao(
            tituloSecao = "ANTERIORES",
            notificacoes = listOf(
                Notificacao(4, "Conta verificada", "Seu perfil de Bibliotecário foi validado com sucesso.", "12 Out, 09:00", Icons.Default.VerifiedUser, true)
            )
        )
    )

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text("Notificações", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar para Home")
                    }
                },
                actions = {
                    TextButton(onClick = { /* Lógica para marcar todas como lidas */ }) {
                        Text("Marcar todas como lidas", color = azulPrimario, fontSize = 13.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            secoes.forEach { secao ->
                // Cabeçalho da Seção (HOJE, ONTEM, ANTERIORES)
                item {
                    Text(
                        text = secao.tituloSecao,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = cinzaTexto,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
                    )
                }

                // Itens da Seção
                items(secao.notificacoes) { notif ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (notif.lida) fundoLida else fundoNaoLida)
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Bolinha de Não Lida + Ícone
                        Box(
                            modifier = Modifier.width(32.dp),
                            contentAlignment = Alignment.TopStart
                        ) {
                            if (!notif.lida) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 6.dp)
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(azulPrimario)
                                        .align(Alignment.CenterStart)
                                )
                            }
                            Icon(
                                imageVector = notif.icone,
                                contentDescription = null,
                                tint = if (notif.lida) cinzaTexto else azulPrimario,
                                modifier = Modifier
                                    .size(20.dp)
                                    .align(Alignment.TopEnd)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Textos (Título, Mensagem, Horário)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = notif.titulo,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF212121)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = notif.mensagem,
                                fontSize = 13.sp,
                                color = cinzaTexto,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = notif.horario,
                                fontSize = 11.sp,
                                color = Color(0xFF9E9E9E)
                            )
                        }
                    }


                    HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                }
            }
        }
    }
}
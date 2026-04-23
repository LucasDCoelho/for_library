package com.br.unifor.for_library.feature.perfil.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ── Modelo de Dados ──
data class Duvida(
    val pergunta: String,
    val resposta: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDuvidas(
    onVoltar: () -> Unit
) {

    val listaDuvidas = listOf(
        Duvida(
            pergunta = "Como renovar um livro?",
            resposta = "Para renovar uma obra, acesse a aba \"Empréstimos\", selecione o livro desejado e clique no botão \"Renovar\". Certifique-se de que não há reservas pendentes para este título."
        ),
        // As demais com texto vazio conforme solicitado
        Duvida("Como funciona o sistema de pontos?", "tem rep ainda naum kk 1"),
        Duvida("Posso sugerir um novo livro?", "tem rep ainda naum kk 2"),
        Duvida("Como reportar um erro na obra?", "tem rep ainda naum kk 3")
    )

    // Controla qual item está expandido (começa com o índice 0 expandido)
    var expandedIndex by remember { mutableStateOf<Int?>(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Dúvidas Frequentes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF212121)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar para Perfil",
                            tint = Color(0xFF212121)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ── Cabeçalho da Tela ──
            item {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Como podemos ajudar?",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF212121)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Encontre respostas para as dúvidas mais comuns sobre o uso da ForLibrary.",
                        fontSize = 14.sp,
                        color = Color(0xFF757575),
                        lineHeight = 20.sp
                    )
                }
                HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
            }

            // ── Lista de Perguntas (Accordion) ──
            itemsIndexed(listaDuvidas) { index, duvida ->
                val isExpanded = expandedIndex == index

                // Animação de rotação da setinha
                val rotationAngle by animateFloatAsState(
                    targetValue = if (isExpanded) 180f else 0f,
                    label = "SetaAnimacao"
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    // Linha clicável da pergunta
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // Se clicar no que já está aberto, ele fecha. Se não, abre o clicado.
                                expandedIndex = if (isExpanded) null else index
                            }
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = duvida.pergunta,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF424242),
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expandir",
                            tint = Color(0xFF9E9E9E),
                            modifier = Modifier.rotate(rotationAngle)
                        )
                    }

                    // Conteúdo expansível (Resposta)
                    AnimatedVisibility(visible = isExpanded) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 8.dp)
                                .padding(bottom = 12.dp)
                        ) {
                            Text(
                                text = duvida.resposta,
                                fontSize = 13.sp,
                                color = Color(0xFF757575),
                                lineHeight = 20.sp
                            )
                        }
                    }

                    HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                }
            }
        }
    }
}
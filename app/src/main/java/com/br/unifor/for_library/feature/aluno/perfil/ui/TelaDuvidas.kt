package com.br.unifor.for_library.feature.aluno.perfil.ui

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

// RF23.3 - Modelo de Dados FAQ
data class FAQItem(
    val pergunta: String,
    val resposta: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDuvidas(
    onVoltar: () -> Unit
) {
    // RF23.3 - Conteúdo dinâmico e real
    val listaDuvidas = remember {
        listOf(
            FAQItem(
                pergunta = "Como renovar um livro?",
                resposta = "Para renovar uma obra, acesse a aba \"Empréstimos\", selecione o livro desejado e clique no botão \"Renovar\". Certifique-se de que não há reservas pendentes para este título."
            ),
            FAQItem(
                pergunta = "Como funciona o sistema de pontos?",
                resposta = "Você ganha pontos ao concluir a leitura de livros (50 pts) e ao ter suas resenhas aprovadas (15 pts). Seus pontos definem seu nível na plataforma, variando de 1 a 5."
            ),
            FAQItem(
                pergunta = "Posso sugerir um novo livro?",
                resposta = "Sim! No seu perfil, utilize a opção 'Adicionar Obra' para enviar os dados de um livro que você gostaria de ver em nosso acervo. Nossa equipe analisará a sugestão."
            ),
            FAQItem(
                pergunta = "Como reportar um erro na obra?",
                resposta = "Caso encontre algum erro de formatação ou conteúdo, você pode utilizar o botão de suporte no menu de configurações ou enviar um e-mail para suporte@forlibrary.com."
            )
        )
    }

    // Controla qual item está expandido
    var expandedIndex by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Dúvidas Frequentes", // RF23.1 - Correção do título
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
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
            // RF23.2 - Cabeçalho da Tela
            item {
                Column(modifier = Modifier.padding(24.dp)) {
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
                        lineHeight = 22.sp
                    )
                }
                HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
            }

            // RF23.3 - Lista de Perguntas (Accordion)
            itemsIndexed(listaDuvidas) { index, duvida ->
                val isExpanded = expandedIndex == index

                // Animação de rotação da seta
                val rotationAngle by animateFloatAsState(
                    targetValue = if (isExpanded) 180f else 0f,
                    label = "SetaAnimacao"
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    // Linha da pergunta (Clickable)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                expandedIndex = if (isExpanded) null else index
                            }
                            .padding(horizontal = 24.dp, vertical = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = duvida.pergunta,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF424242),
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = if (isExpanded) "Recolher" else "Expandir",
                            tint = Color(0xFF9E9E9E),
                            modifier = Modifier.rotate(rotationAngle)
                        )
                    }

                    // Conteúdo expansível (Resposta)
                    AnimatedVisibility(visible = isExpanded) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                                .padding(bottom = 20.dp)
                        ) {
                            Text(
                                text = duvida.resposta,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF616161),
                                lineHeight = 22.sp
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        color = Color(0xFFF5F5F5), 
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}

package com.br.unifor.for_library.feature.aluno.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.br.unifor.for_library.core.components.CapaLivro
import com.br.unifor.for_library.core.data.LivrosSalvosState
import com.br.unifor.for_library.core.designsystem.AzulPrimario
import com.br.unifor.for_library.core.designsystem.CinzaTexto
import com.br.unifor.for_library.core.designsystem.coresFallback

// â”€â”€ Cores locais da Home â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
private val AzulChip  = Color(0xFF1E88E5)
private val FundoTela = Color(0xFFF2F4F8)

// â”€â”€ Modelos â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
private data class LivroEmLeitura(
    val titulo: String,
    val autor: String,
    val capitulo: String,
    val progressoPct: Float
)

private data class LivroDestaque(
    val id: Int,
    val titulo: String,
    val autor: String,
    val isbn: String,
    val salvo: Boolean = false
)

// â”€â”€ Mock â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
private val mockLivroEmLeitura = LivroEmLeitura(
    titulo       = "Cem Anos de SolidÃ£o",
    autor        = "Gabriel GarcÃ­a MÃ¡rquez",
    capitulo     = "CapÃ­tulo 12",
    progressoPct = 0.64f
)

private const val isbnLivroEmLeitura = "9780060883287"

private val mockDestaques = listOf(
    LivroDestaque(1, "O Alquimista",   "Paulo Coelho",     isbn = "9780062315007"),
    LivroDestaque(2, "Dom Casmurro",   "Machado de Assis", isbn = "9788535902778", salvo = true),
    LivroDestaque(3, "1984",           "George Orwell",    isbn = "9780451524935"),
    LivroDestaque(4, "Clean Code",     "Robert C. Martin", isbn = "9780132350884", salvo = true),
    LivroDestaque(5, "Atomic Habits",  "James Clear",      isbn = "9780735211292"),
)

// â”€â”€ Tela principal â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
@Composable
fun TelaHomeAluno(
    nomeAluno: String = "Ricardo",
    pontos: Int = 1250,
    livrosLidos: Int = 24,
    tempoTotal: String = "128h",
    onPontosClick: () -> Unit = {},
    onSinoClick: () -> Unit = {},
    onContinueLendoClick: () -> Unit = {},
    onVerTodosClick: () -> Unit = {},
    onLivroClick: (Int) -> Unit = {},
) {
    val destaques = mockDestaques

    Box(modifier = Modifier.fillMaxSize()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoTela)
            .verticalScroll(rememberScrollState())
    ) {
        // â”€â”€ Header â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(AzulPrimario),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = nomeAluno.first().toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text("Bem-vindo", fontSize = 11.sp, color = CinzaTexto)
                Text("OlÃ¡, $nomeAluno!", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
            }

            Surface(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraLarge)
                    .clickable { onPontosClick() },
                color = AzulChip,
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("â­", fontSize = 13.sp)
                    Spacer(Modifier.width(4.dp))
                    Text("%,d pts".format(pontos), color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }
            }

            Spacer(Modifier.width(8.dp))

            IconButton(onClick = onSinoClick) {
                Icon(Icons.Default.Notifications, contentDescription = "NotificaÃ§Ãµes", tint = Color(0xFF424242))
            }
        }

        // â”€â”€ Continue Lendo â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        Text(
            "Continue Lendo",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color(0xFF212121),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { onContinueLendoClick() },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CapaLivro(
                    isbn = isbnLivroEmLeitura,
                    tituloFallback = mockLivroEmLeitura.titulo,
                    modifier = Modifier
                        .width(62.dp)
                        .height(88.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    corFallback = AzulPrimario
                )

                Spacer(Modifier.width(14.dp))

                Column(Modifier.weight(1f)) {
                    Text(mockLivroEmLeitura.titulo, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Spacer(Modifier.height(2.dp))
                    Text(mockLivroEmLeitura.autor, fontSize = 12.sp, color = CinzaTexto)
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("ðŸ“–", fontSize = 11.sp)
                        Spacer(Modifier.width(4.dp))
                        Text(mockLivroEmLeitura.capitulo, fontSize = 11.sp, color = CinzaTexto)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("PROGRESSO", fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = CinzaTexto, letterSpacing = 0.8.sp)
                    Spacer(Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { mockLivroEmLeitura.progressoPct },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(RoundedCornerShape(50)),
                        color = AzulPrimario,
                        trackColor = Color(0xFFE3EBF6)
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        "${(mockLivroEmLeitura.progressoPct * 100).toInt()}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AzulPrimario
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // â”€â”€ Destaques do Acervo â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Destaques do Acervo", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF212121))
            Text(
                "ver todos â†’",
                fontSize = 13.sp,
                color = AzulPrimario,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onVerTodosClick() }
            )
        }

        Spacer(Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(destaques) { livro ->
                val corFallback = coresFallback[livro.id % coresFallback.size]
                Column(
                    modifier = Modifier
                        .width(110.dp)
                        .clickable { onLivroClick(livro.id) },
                    horizontalAlignment = Alignment.Start
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    ) {
                        CapaLivro(
                            isbn = livro.isbn,
                            tituloFallback = livro.titulo,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp)),
                            corFallback = corFallback
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(6.dp)
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.88f))
                                .clickable { LivrosSalvosState.toggleSalvo(livro.isbn) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (LivrosSalvosState.isSalvo(livro.isbn)) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                contentDescription = null,
                                tint = if (LivrosSalvosState.isSalvo(livro.isbn)) AzulPrimario else Color(0xFF757575),
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(6.dp))
                    Text(livro.titulo, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 16.sp)
                    Text(livro.autor, fontSize = 11.sp, color = CinzaTexto, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // â”€â”€ EstatÃ­sticas â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf(
                Triple("ðŸ“š", "LIVROS LIDOS", livrosLidos.toString()),
                Triple("â±ï¸", "TEMPO TOTAL", tempoTotal)
            ).forEach { (icone, titulo, valor) ->
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(icone, fontSize = 24.sp)
                        Spacer(Modifier.height(4.dp))
                        Text(titulo, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = CinzaTexto, letterSpacing = 0.6.sp)
                        Spacer(Modifier.height(2.dp))
                        Text(valor, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }

    ChatBotAluno(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(bottom = 16.dp, end = 16.dp)
    )
    } // Box
}



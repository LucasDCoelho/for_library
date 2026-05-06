package com.br.unifor.for_library.feature.aluno.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.br.unifor.for_library.core.components.CapaLivro
import com.br.unifor.for_library.core.data.LivrosSalvosState
import com.br.unifor.for_library.core.designsystem.AzulPrimario
import com.br.unifor.for_library.core.designsystem.coresFallback

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
private val mockLivroEmLeitura = LivroEmLeitura(
    titulo       = "Cem Anos de Solidao",
    autor        = "Gabriel Garcia Marquez",
    capitulo     = "Capitulo 12",
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
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
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text  = nomeAluno.first().toString(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    "Bem-vindo",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "Ola, $nomeAluno!",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Surface(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraLarge)
                    .clickable(
                        onClickLabel = "Ver meus pontos",
                        role         = Role.Button,
                        onClick      = onPontosClick
                    ),
                color = MaterialTheme.colorScheme.secondary,
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "%,d pts".format(pontos),
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            IconButton(
                onClick  = onSinoClick,
                modifier = Modifier.semantics { contentDescription = "Notificacoes" }
            ) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text(
            "Continue Lendo",
            style    = MaterialTheme.typography.titleMedium,
            color    = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Card(
            modifier  = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable(
                    onClickLabel = "Continuar lendo",
                    role         = Role.Button,
                    onClick      = onContinueLendoClick
                ),
            shape     = MaterialTheme.shapes.medium,
            colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier          = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CapaLivro(
                    isbn           = isbnLivroEmLeitura,
                    tituloFallback = mockLivroEmLeitura.titulo,
                    modifier       = Modifier
                        .width(62.dp)
                        .height(88.dp)
                        .clip(MaterialTheme.shapes.small),
                    corFallback    = AzulPrimario
                )
                Spacer(Modifier.width(14.dp))
                Column(
                    modifier            = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        mockLivroEmLeitura.titulo,
                        style    = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        mockLivroEmLeitura.autor,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(Modifier.width(4.dp))
                        Text(
                            mockLivroEmLeitura.capitulo,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "PROGRESSO",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    LinearProgressIndicator(
                        progress   = { mockLivroEmLeitura.progressoPct },
                        modifier   = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(MaterialTheme.shapes.extraLarge),
                        color      = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Text(
                        "${(mockLivroEmLeitura.progressoPct * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(
                "Destaques do Acervo",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "ver todos",
                style    = MaterialTheme.typography.labelLarge,
                color    = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(
                    onClickLabel = "Ver todos os destaques",
                    role         = Role.Button,
                    onClick      = onVerTodosClick
                )
            )
        }
        Spacer(Modifier.height(12.dp))
        LazyRow(
            contentPadding        = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(destaques, key = { it.id }) { livro ->
                val corFallback = coresFallback[livro.id % coresFallback.size]
                Column(
                    modifier = Modifier
                        .width(110.dp)
                        .clickable(
                            onClickLabel = "Abrir ${livro.titulo}",
                            role         = Role.Button,
                            onClick      = { onLivroClick(livro.id) }
                        ),
                    horizontalAlignment = Alignment.Start
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    ) {
                        CapaLivro(
                            isbn           = livro.isbn,
                            tituloFallback = livro.titulo,
                            modifier       = Modifier
                                .fillMaxSize()
                                .clip(MaterialTheme.shapes.small),
                            corFallback    = corFallback
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(6.dp)
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.88f))
                                .clickable(
                                    onClickLabel = if (LivrosSalvosState.isSalvo(livro.isbn))
                                        "Remover dos salvos" else "Salvar livro",
                                    role    = Role.Checkbox,
                                    onClick = { LivrosSalvosState.toggleSalvo(livro.isbn) }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector        = if (LivrosSalvosState.isSalvo(livro.isbn))
                                    Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                contentDescription = null,
                                tint               = if (LivrosSalvosState.isSalvo(livro.isbn))
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        livro.titulo,
                        style    = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        livro.autor,
                        style    = MaterialTheme.typography.labelSmall,
                        color    = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf(
                Triple("", "LIVROS LIDOS", livrosLidos.toString()),
                Triple("", "TEMPO TOTAL",  tempoTotal)
            ).forEach { (icone, titulo, valor) ->
                Card(
                    modifier  = Modifier.weight(1f),
                    shape     = MaterialTheme.shapes.medium,
                    colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier            = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(icone, style = MaterialTheme.typography.headlineSmall)
                        Text(
                            titulo,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            valor,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}
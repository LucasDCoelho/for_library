package com.br.unifor.for_library.feature.aluno.acervo.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AzulPrimario = Color(0xFF1565C0)

// â”€â”€ IlustraÃ§Ã£o de documento dormindo â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
@Composable
private fun IlustracaoDocumentoDormindo() {
    val cinzaFundo   = Color(0xFFF0F2F5)
    val cinzaLinha   = Color(0xFFCDD1D8)
    val azulMarcador = Color(0xFF1565C0)

    Canvas(
        modifier = Modifier
            .size(width = 130.dp, height = 160.dp)
    ) {
        val w = size.width
        val h = size.height

        // Sombra do documento
        drawRoundRect(
            color = Color(0xFFDDE1E8),
            topLeft = Offset(w * 0.08f, h * 0.06f),
            size = Size(w * 0.84f, h * 0.78f),
            cornerRadius = CornerRadius(16f)
        )

        // Corpo do documento
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(w * 0.04f, h * 0.02f),
            size = Size(w * 0.84f, h * 0.78f),
            cornerRadius = CornerRadius(16f)
        )

        // Marcador azul no canto superior direito
        drawRoundRect(
            color = azulMarcador,
            topLeft = Offset(w * 0.72f, h * 0.0f),
            size = Size(w * 0.1f, h * 0.16f),
            cornerRadius = CornerRadius(4f)
        )

        // Linhas de texto simuladas
        val linhaX = w * 0.16f
        val linhaAltura = h * 0.022f
        val linhaRadius = CornerRadius(4f)

        listOf(0.38f, 0.47f, 0.56f).forEach { yRatio ->
            drawRoundRect(
                color = cinzaLinha,
                topLeft = Offset(linhaX, h * yRatio),
                size = Size(w * 0.62f, linhaAltura),
                cornerRadius = linhaRadius
            )
        }
        // Linha curta no final
        drawRoundRect(
            color = cinzaLinha,
            topLeft = Offset(linhaX, h * 0.65f),
            size = Size(w * 0.35f, linhaAltura),
            cornerRadius = linhaRadius
        )
    }
}

// â”€â”€ Placeholder principal (RF08.3 + RF08.4 + RF08.5) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
@Composable
fun BuscaVaziaPlaceholder(
    onLimparFiltros: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // IlustraÃ§Ã£o (RF08.3)
        Box(contentAlignment = Alignment.TopEnd) {
            IlustracaoDocumentoDormindo()
            // ZZZ
            Text(
                text = "z z z",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AzulPrimario,
                modifier = Modifier.offset(x = 16.dp, y = 8.dp)
            )
        }

        Spacer(Modifier.height(28.dp))

        // TÃ­tulo (RF08.4)
        Text(
            text = "Ops! Silêncio na biblioteca",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A2E),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        // SubtÃ­tulo (RF08.4)
        Text(
            text = "Nenhum livro encontrado para esta pesquisa.",
            fontSize = 13.sp,
            color = Color(0xFF757575),
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )

        Spacer(Modifier.height(6.dp))

        // Dica (RF08.5)
        Text(
            text = "Tente usar palavras-chave diferentes ou verifique a ortografia.",
            fontSize = 12.sp,
            color = Color(0xFF9E9E9E),
            textAlign = TextAlign.Center,
            lineHeight = 17.sp
        )

        Spacer(Modifier.height(28.dp))

        // BotÃ£o Limpar filtros (RF08.5)
        Button(
            onClick = onLimparFiltros,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario)
        ) {
            Text(
                text = "Limpar filtros  Ã—",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


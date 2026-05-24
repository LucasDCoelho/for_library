package com.br.unifor.for_library.feature.aluno.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.br.unifor.for_library.core.designsystem.AzulPrimario
import com.br.unifor.for_library.core.designsystem.CinzaTexto

@Composable
fun AcervoEmptyState(
    onLimparFiltrosClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // RF08.3: Imagem amigável de livro (MenuBook para representar o acervo vazio)
        Icon(
            imageVector = Icons.Default.MenuBook,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = AzulPrimario.copy(alpha = 0.3f)
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        // RF08.4: Texto Principal
        Text(
            text = "Ops! Silêncio na biblioteca. Nenhum livro encontrado para esta pesquisa",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                lineHeight = 24.sp
            ),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // RF08.5: Texto Explicativo
        Text(
            text = "Tente usar palavras-chave diferentes ou verifique a ortografia.",
            style = MaterialTheme.typography.bodyMedium,
            color = CinzaTexto,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // RF08.5/RF08.6: Botão Limpar Filtros
        Button(
            onClick = onLimparFiltrosClick,
            colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                text = "Limpar Filtros",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

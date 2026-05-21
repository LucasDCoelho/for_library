package com.br.unifor.for_library.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ForLibraryColorScheme = lightColorScheme(
    primary          = AzulPrimario,
    secondary        = AzulChip,
    onSurfaceVariant = CinzaTexto,
    background       = FundoTela,
    surface          = androidx.compose.ui.graphics.Color.White
)

@Composable
fun ForLibraryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ForLibraryColorScheme,
        content     = content
    )
}


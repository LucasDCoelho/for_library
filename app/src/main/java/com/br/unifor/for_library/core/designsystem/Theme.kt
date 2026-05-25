package com.br.unifor.for_library.core.designsystem

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.br.unifor.for_library.feature.aluno.presentation.configuracoes.ConfigViewModel
import com.br.unifor.for_library.feature.aluno.presentation.configuracoes.ConfigViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel

private val LightColorScheme = lightColorScheme(
    primary          = AzulPrimario,
    secondary        = AzulChip,
    onSurfaceVariant = CinzaTexto,
    background       = FundoTela,
    surface          = androidx.compose.ui.graphics.Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary          = AzulPrimario,
    secondary        = AzulChip,
    onSurfaceVariant = CinzaTexto,
    // Background escuro padrão ou personalizado
)

@Composable
fun ForLibraryTheme(
    context: Context = LocalContext.current,
    content: @Composable () -> Unit
) {
    val configViewModel: ConfigViewModel = viewModel(factory = ConfigViewModelFactory(context))
    val temaEscuroPref by configViewModel.temaEscuro.collectAsState(initial = isSystemInDarkTheme())

    val colorScheme = if (temaEscuroPref) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content     = content
    )
}

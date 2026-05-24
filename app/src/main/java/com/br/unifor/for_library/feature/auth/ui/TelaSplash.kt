package com.br.unifor.for_library.feature.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.br.unifor.for_library.R
import com.br.unifor.for_library.core.designsystem.AzulChip
import com.br.unifor.for_library.feature.auth.buscarTipoUsuarioAtual
import com.br.unifor.for_library.feature.auth.isAdmin
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.delay

private const val SPLASH_DELAY_MS = 3000L

@Composable
fun TelaSplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHomeAluno: () -> Unit,
    onNavigateToHomeAdmin: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(SPLASH_DELAY_MS)
        val user = supabase.auth.currentUserOrNull()
        when {
            user == null -> onNavigateToLogin()
            else -> {
                val tipoUsuario = buscarTipoUsuarioAtual()
                if (isAdmin(tipoUsuario)) {
                    onNavigateToHomeAdmin()
                } else {
                    onNavigateToHomeAluno()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulChip),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_for_library),
            contentDescription = "Logo ForLibrary",
            modifier = Modifier.size(120.dp)
        )
    }
}
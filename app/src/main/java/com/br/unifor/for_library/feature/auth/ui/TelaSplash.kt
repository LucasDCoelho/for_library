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
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import android.util.Log

private const val SPLASH_DELAY_MS = 3000L

@Composable
fun TelaSplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHomeAluno: () -> Unit,
    onNavigateToHomeAdmin: () -> Unit
) {
    LaunchedEffect(Unit) {
        val splashStart = System.currentTimeMillis()

        // 1. Aguarda o Supabase carregar a sessão do armazenamento (ou falhar)
        // O status muda de NotAuthenticated (inicial) para Authenticated ou NotAuthenticated (após carregar cache)
        Log.d("Splash", "Iniciando verificação de sessão...")

        // Espera até que o status não seja mais 'LoadingFromStorage' (se disponível na versão do SDK)
        // Ou simplesmente tenta pegar a sessão atual após um pequeno fôlego para o SDK
        delay(500) // Pequeno delay inicial para o SDK respirar

        val status = supabase.auth.sessionStatus.value
        Log.d("Splash", "Status da sessão inicial: $status")

        // 2. Garante que o splash dure pelo menos SPLASH_DELAY_MS
        val elapsed = System.currentTimeMillis() - splashStart
        val remaining = SPLASH_DELAY_MS - elapsed
        if (remaining > 0) delay(remaining)

        try {
            val user = supabase.auth.currentUserOrNull()
            Log.d("Splash", "Usuário após delay: ${user?.email}")

            if (user != null) {
                val tipoUsuario = buscarTipoUsuarioAtual()
                Log.d("Splash", "Tipo de usuário: $tipoUsuario")
                if (isAdmin(tipoUsuario)) {
                    onNavigateToHomeAdmin()
                } else {
                    onNavigateToHomeAluno()
                }
            } else {
                Log.d("Splash", "Nenhum usuário encontrado, indo para login.")
                onNavigateToLogin()
            }
        } catch (e: Exception) {
            Log.e("Splash", "Erro no splash", e)
            onNavigateToLogin()
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
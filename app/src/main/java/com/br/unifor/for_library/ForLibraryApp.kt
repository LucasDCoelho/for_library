package com.br.unifor.for_library;

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.br.unifor.for_library.core.navigation.ForLibraryBottomBar
import com.br.unifor.for_library.core.navigation.Rota
import com.br.unifor.for_library.feature.auth.ui.TelaLoginPlaceholder
import com.br.unifor.for_library.feature.splash.ui.TelaSplashScreen
import com.br.unifor.for_library.feature.auth.ui.TelaRecuperarSenha

@Composable
fun ForLibraryApp() {
    // Esse é o controlador mestre. Ele só é instanciado UMA vez aqui.
    val navController = rememberNavController()

    // O Scaffold gerencia o layout da tela, incluindo a BottomBar
    Scaffold(
        bottomBar = { ForLibraryBottomBar(navController = navController) }
    ) { paddingValues ->

        // O NavHost é onde as rotas são ligadas às telas
        NavHost(
            navController = navController,
            startDestination = Rota.Splash.path,
            modifier = Modifier.padding(paddingValues)
        ) {

            // --- ÁREA DE AUTENTICAÇÃO ---
            composable(route = Rota.Splash.path) {
                TelaSplashScreen(
                    onSplashFinished = {
                        // Navega para o Login e remove a Splash do histórico
                        navController.navigate(Rota.Login.path) {
                            popUpTo(Rota.Splash.path) { inclusive = true }
                        }
                    }
                )
            }

            composable(route = Rota.Login.path) {
                TelaLoginPlaceholder(
                    onLoginSucesso = {
                        navController.navigate(route = Rota.HomeAluno.path) {
                            popUpTo(route = Rota.Login.path) { inclusive = true }
                        }
                    },
                    onIrParaCadastro = {
                        navController.navigate(route = Rota.Cadastro.path)
                    },
                    onIrParaEsqueciSenha = {
                        navController.navigate(route = Rota.RecuperarSenha.path)
                    }
                )
            }
            composable(route = Rota.RecuperarSenha.path) {
                TelaRecuperarSenha(
                    onVoltar = {
                        // O popBackStack destrói essa tela e volta automaticamente para a anterior (Login)
                        navController.popBackStack()
                    }
                )
            }
            composable(route = Rota.Cadastro.path) {
                Text(text = "Tela de cadastro (em construção)")
            }

        }
    }
}

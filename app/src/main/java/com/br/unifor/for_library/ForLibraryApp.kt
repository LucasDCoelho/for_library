package com.br.unifor.for_library;

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.br.unifor.for_library.core.navigation.ForLibraryBottomBar
import com.br.unifor.for_library.core.navigation.Rota
import com.br.unifor.for_library.feature.acervo.ui.TelaAcervoDigital
import com.br.unifor.for_library.feature.auth.ui.TelaLoginPlaceholder
import com.br.unifor.for_library.feature.auth.ui.TelaRecuperarSenha
import com.br.unifor.for_library.feature.auth.ui.TelaSplashScreen

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

            // ── Autenticação ──────────────────────────────────────────────────
            composable(Rota.Login.path) {
                TelaLoginPlaceholder(
                    onLoginSucesso = {
                        navController.navigate(Rota.HomeAluno.path) {
                            popUpTo(Rota.Login.path) { inclusive = true }
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

            composable(Rota.Cadastro.path) {
                TelaPlaceholder("Cadastro")
            }

            // ── Home ──────────────────────────────────────────────────────────
            composable(Rota.HomeAluno.path) {
                TelaHomeAluno(
                    onPontosClick        = { /* TODO: Rota.MeusPontos */ },
                    onSinoClick          = { /* TODO: Rota.Notificacoes */ },
                    onContinueLendoClick = { /* TODO: Rota.Leitor */ },
                    onVerTodosClick      = { navController.navigate(Rota.Acervo.path) },
                    onLivroClick         = { /* TODO: Rota.DetalhesLivro */ }
                )
            }

            // ── Bottom Nav ────────────────────────────────────────────────────
            composable(Rota.Acervo.path) {
                TelaAcervoDigital()
            }

            composable(Rota.Estante.path) {
                TelaPlaceholder("Estante")
            }

            composable(Rota.Eventos.path) {
                TelaPlaceholder("Eventos")
            }
            composable(route = Rota.Cadastro.path) {
                Text(text = "Tela de cadastro (em construção)")
            }

            composable(Rota.Perfil.path) {
                TelaPlaceholder("Perfil")
            }
        }
    }
}

// Tela temporária para rotas ainda não implementadas
@Composable
private fun TelaPlaceholder(nome: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Tela $nome — em breve")
    }
}
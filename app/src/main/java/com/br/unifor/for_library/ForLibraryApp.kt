package com.br.unifor.for_library

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.br.unifor.for_library.core.navigation.ForLibraryBottomBar
import com.br.unifor.for_library.core.navigation.Rota
import com.br.unifor.for_library.feature.acervo.ui.TelaAcervoDigital
import com.br.unifor.for_library.feature.eventos.ui.TelaEventos
import com.br.unifor.for_library.feature.estante.ui.TelaEstante
import com.br.unifor.for_library.feature.auth.ui.TelaLoginPlaceholder
import com.br.unifor.for_library.feature.auth.ui.TelaRecuperarSenha
import com.br.unifor.for_library.feature.auth.ui.TelaSplashScreen
import com.br.unifor.for_library.feature.home.ui.TelaHomeAluno

private val rotasComPadding = setOf(
    Rota.HomeAluno.path,
    Rota.Acervo.path,
    Rota.Estante.path,
    Rota.Eventos.path,
    Rota.Perfil.path
)

@Composable
fun ForLibraryApp() {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val rotaAtual = backStack?.destination?.route

    Scaffold(
        bottomBar = { ForLibraryBottomBar(navController = navController) }
    ) { paddingValues ->

        // Só aplica padding nas telas com bottom bar — null e rotas de auth ficam sem padding
        val modifier = if (rotaAtual in rotasComPadding) {
            Modifier.padding(paddingValues)
        } else {
            Modifier.fillMaxSize()
        }

        NavHost(
            navController = navController,
            startDestination = Rota.Splash.path,
            modifier = modifier
        ) {

            // ── Splash ────────────────────────────────────────────────────────
            composable(Rota.Splash.path) {
                TelaSplashScreen(
                    onSplashFinished = {
                        navController.navigate(Rota.Login.path) {
                            popUpTo(Rota.Splash.path) { inclusive = true }
                        }
                    }
                )
            }

            // ── Autenticação ──────────────────────────────────────────────────
            composable(Rota.Login.path) {
                TelaLoginPlaceholder(
                    onLoginSucesso       = {
                        navController.navigate(Rota.HomeAluno.path) {
                            popUpTo(Rota.Login.path) { inclusive = true }
                        }
                    },
                    onIrParaCadastro     = { navController.navigate(Rota.Cadastro.path) },
                    onIrParaEsqueciSenha = { navController.navigate(Rota.RecuperarSenha.path) }
                )
            }

            composable(Rota.RecuperarSenha.path) {
                TelaRecuperarSenha(onVoltar = { navController.popBackStack() })
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
                TelaAcervoDigital(
                    onLivroClick = { livroId ->
                        navController.navigate(Rota.DetalhesLivro.criarRota(livroId.toString()))
                    }
                )
            }

            composable(Rota.Estante.path) {
                TelaEstante(
                    onSearchClick = { /* TODO: busca */ }
                )
            }

            composable(Rota.Eventos.path) {
                TelaEventos(
                    onSinoClick   = { /* TODO: Rota.Notificacoes */ },
                    onEventoClick = { /* TODO: Rota.DetalhesEvento */ }
                )
            }

            composable(Rota.Perfil.path) { TelaPlaceholder("Perfil") }
        }
    }
}

@Composable
private fun TelaPlaceholder(nome: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Tela $nome — em breve")
    }
}
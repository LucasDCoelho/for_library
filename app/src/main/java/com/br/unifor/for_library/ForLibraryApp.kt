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
import com.br.unifor.for_library.feature.perfil.ui.TelaPerfil
import com.br.unifor.for_library.feature.acervo.ui.TelaAcervoDigital
import com.br.unifor.for_library.feature.auth.ui.TelaLoginPlaceholder
import com.br.unifor.for_library.feature.auth.ui.TelaRecuperarSenha
import com.br.unifor.for_library.feature.auth.ui.TelaSplashScreen
import com.br.unifor.for_library.feature.notificaçao.ui.TelaNotificacoes
import com.br.unifor.for_library.feature.perfil.ui.TelaEditarPerfil
import com.br.unifor.for_library.feature.perfil.ui.TelaDuvidas

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
            //Login
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
            //Recuperar senha
            composable(route = Rota.RecuperarSenha.path) {
                TelaRecuperarSenha(
                    onVoltar = {
                        navController.popBackStack()
                    }
                )
            }

            // HOME
            composable(Rota.HomeAluno.path) {
                TelaHomeAluno(
                    onPontosClick        = { /* TODO: Rota.MeusPontos */ },
                    onSinoClick          = { navController.navigate(Rota.Notificacoes.path) },
                    onContinueLendoClick = { /* TODO: Rota.Leitor */ },
                    onVerTodosClick      = { navController.navigate(Rota.Acervo.path) },
                    onLivroClick         = { /* TODO: Rota.DetalhesLivro */ }
                )
            }
            //Notificações
            composable(Rota.Notificacoes.path) {
                TelaNotificacoes(
                    onVoltar = { navController.popBackStack() }
                )
            }
            //Editar perfil
            composable(Rota.EditarPerfil.path) {
                TelaEditarPerfil(
                    onVoltar = { navController.popBackStack() }
                )
            }
            //F.A.Q
            composable(Rota.Duvida.path) {
                TelaDuvidas(
                    onVoltar = { navController.popBackStack() }
                )
            }
            // ── Bottom Nav ────────────────────────────────────────────────────
            composable(Rota.Acervo.path) {
                TelaAcervoDigital(
                    onSinoClick = {
                        navController.navigate(Rota.Notificacoes.path)
                    }
                )
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
                TelaPerfil(
                    onEditarPerfilClick = { navController.navigate(Rota.EditarPerfil.path) },
                    onEnvioObraClick = { /*TODO*/ },
                    onDuvidasClick = { navController.navigate(Rota.Duvida.path) },
                    onConfiguracoesClick = { /*TODO*/ },
                    onSairClick = {
                        navController.navigate(Rota.Login.path) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
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
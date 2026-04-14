package com.br.unifor.for_library

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
import com.br.unifor.for_library.feature.auth.ui.TelaLoginPlaceholder

@Composable
fun ForLibraryApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { ForLibraryBottomBar(navController = navController) }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Rota.Login.path,
            modifier = Modifier.padding(paddingValues)
        ) {

            // ── Autenticação ──────────────────────────────────────────────────
            composable(Rota.Login.path) {
                TelaLoginPlaceholder(
                    onLoginSucesso = {
                        navController.navigate(Rota.HomeAluno.path) {
                            popUpTo(Rota.Login.path) { inclusive = true }
                        }
                    },
                    onIrParaCadastro = { navController.navigate(Rota.Cadastro.path) }
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
                TelaPlaceholder("Acervo")
            }

            composable(Rota.Estante.path) {
                TelaPlaceholder("Estante")
            }

            composable(Rota.Eventos.path) {
                TelaPlaceholder("Eventos")
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
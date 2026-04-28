package com.br.unifor.for_library;

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.br.unifor.for_library.feature.aluno.acervo.ui.TelaAcervoDigital
import com.br.unifor.for_library.feature.aluno.eventos.ui.TelaEventos
import com.br.unifor.for_library.feature.aluno.estante.ui.TelaEstante
import com.br.unifor.for_library.feature.auth.ui.TelaLoginPlaceholder
import com.br.unifor.for_library.feature.auth.ui.TelaRecuperarSenha
import com.br.unifor.for_library.feature.auth.ui.TelaSplashScreen
import com.br.unifor.for_library.feature.aluno.livro.ui.TelaDetalhesLivro
import com.br.unifor.for_library.feature.aluno.livro.ui.TelaLeitorDigital
import com.br.unifor.for_library.feature.aluno.livro.ui.TelaAvaliacaoResenha
import com.br.unifor.for_library.feature.aluno.home.ui.TelaHomeAluno
import com.br.unifor.for_library.feature.aluno.perfil.ui.TelaPerfil
import com.br.unifor.for_library.feature.aluno.notificacao.ui.TelaNotificacoes
import com.br.unifor.for_library.feature.aluno.perfil.ui.TelaConfiguracoes
import com.br.unifor.for_library.feature.aluno.perfil.ui.TelaEditarPerfil
import com.br.unifor.for_library.feature.aluno.perfil.ui.TelaDuvidas
import com.br.unifor.for_library.feature.aluno.perfil.ui.PopupLogout
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
private val rotasComPadding = setOf(
    Rota.HomeAluno.path,
    Rota.Acervo.path,
    Rota.Estante.path,
    Rota.Eventos.path,
    Rota.Perfil.path
)

@Composable
fun ForLibraryApp() {
    // Esse é o controlador mestre. Ele só é instanciado UMA vez aqui.
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val rotaAtual = backStack?.destination?.route

    var mostrarPopupSair by remember { mutableStateOf(false) }

    if (mostrarPopupSair) {
        PopupLogout(
            onDismiss = { mostrarPopupSair = false }, // RF25.3: Fecha ao cancelar
            onConfirm = {
                mostrarPopupSair = false
                // RF25.3: Vai para o Login limpando o histórico
                navController.navigate(Rota.Login.path) {
                    popUpTo(Rota.Splash.path) { inclusive = true }
                }
            }
        )
    }
    // O Scaffold gerencia o layout da tela, incluindo a BottomBar
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
                Text(text = "Tela de cadastro (em construção)")
            }

            // ── Home ──────────────────────────────────────────────────────────
            composable(Rota.HomeAluno.path) {
                TelaHomeAluno(
                    onPontosClick        = { /* TODO: Rota.MeusPontos */ },
                    onSinoClick          = { navController.navigate(Rota.Notificacoes.path) },
                    onContinueLendoClick = { /* TODO: Rota.Leitor */ },
                    onVerTodosClick      = { navController.navigate(Rota.Acervo.path) },
                    onLivroClick = { navController.navigate(Rota.DetalhesLivro.criarRota("livro_id_exemplo")) }                )
            }

            composable(Rota.DetalhesLivro.path) { backStackEntry ->
                val livroId = backStackEntry.arguments?.getString("livroId") ?: ""

                TelaDetalhesLivro(
                    livroId = livroId,
                    onVoltar = { navController.popBackStack() },
                    onNotificacoes = { /* TODO */ },
                    onLerAgora = {
                        // Clicou no botão, navega para o Leitor passando o título!
                        navController.navigate(Rota.LeitorDigital.criarRota(livroId, "O Horizonte de Eventos"))
                    }
                )
            }

            composable(route = Rota.LeitorDigital.path) { backStackEntry ->
                val titulo = backStackEntry.arguments?.getString("titulo") ?: "Livro Desconhecido"

                TelaLeitorDigital(
                    tituloLivro = titulo,
                    onVoltar = { navController.popBackStack() },
                    onIrAvaliarLivro = { navController.navigate(Rota.AvalicaoLivro.path) }
                )
            }

            composable(Rota.AvalicaoLivro.path) {
                TelaAvaliacaoResenha(
                    onClose = { navController.popBackStack() },
                    onCancelar = { navController.popBackStack() },
                )
            }

            // ── Notificações ──────────────────────────────────────────────────
            composable(Rota.Notificacoes.path) {
                TelaNotificacoes(
                    onVoltar = { navController.popBackStack() }
                )
            }

            // ── Editar Perfil ─────────────────────────────────────────────────
            composable(Rota.EditarPerfil.path) {
                TelaEditarPerfil(
                    onVoltar = { navController.popBackStack() }
                )
            }

            // ── F.A.Q ─────────────────────────────────────────────────────────
            composable(Rota.Duvida.path) {
                TelaDuvidas(
                    onVoltar = { navController.popBackStack() }
                )
            }

            // ── Bottom Nav ────────────────────────────────────────────────────
            composable(Rota.Acervo.path) {
                TelaAcervoDigital(
                    onLivroClick = { livroId ->
                        navController.navigate(Rota.DetalhesLivro.criarRota(livroId.toString()))
                    },
                    onSinoClick = {
                        navController.navigate(Rota.Notificacoes.path)
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
                    onSinoClick   = { navController.navigate(Rota.Notificacoes.path) },
                    onEventoClick = { /* TODO: Rota.DetalhesEvento */ }
                )
            }

            composable(Rota.Perfil.path) {
                TelaPerfil(
                    onEditarPerfilClick = { navController.navigate(Rota.EditarPerfil.path) },
                    onEnvioObraClick = { /*TODO*/ },
                    onDuvidasClick = { navController.navigate(Rota.Duvida.path) },
                    onConfiguracoesClick = { navController.navigate(Rota.Configuracoes.path) },

                    // ── MUDANÇA AQUI: Aciona o popup em vez de navegar direto ──
                    onSairClick = { mostrarPopupSair = true }
                )
            }

            composable(route = Rota.Configuracoes.path) {
                TelaConfiguracoes(
                    onVoltar = { navController.popBackStack() },

                    // ── MUDANÇA AQUI: Aciona o popup em vez de navegar direto ──
                    onSairClick = { mostrarPopupSair = true }
                )
            }
        }
    }
}

@Composable
private fun TelaPlaceholder(nome: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Tela $nome — em breve")
    }
}

package com.br.unifor.for_library

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
import com.br.unifor.for_library.core.navigation.AdminBottomBar
import com.br.unifor.for_library.core.navigation.ForLibraryBottomBar
import com.br.unifor.for_library.core.navigation.Rota
import com.br.unifor.for_library.feature.adm.TelaConfiguracoesSistema
import com.br.unifor.for_library.feature.adm.acervo.TelaAdicionarObra
import com.br.unifor.for_library.feature.adm.acervo.TelaEditarObra
import com.br.unifor.for_library.feature.adm.acervo.TelaGestaoAcervo
import com.br.unifor.for_library.feature.adm.dashboard.TelaDashboardAdmin
import com.br.unifor.for_library.feature.adm.moderacao.TelaGestaoUsuarios
import com.br.unifor.for_library.feature.adm.moderacao.TelaListaModeracaoAdmin
import com.br.unifor.for_library.feature.adm.moderacao.TelaModeracaoObras
import com.br.unifor.for_library.feature.adm.moderacao.TelaAnaliseObra
import com.br.unifor.for_library.feature.adm.moderacao.modresenha.TelaAnaliseResenha
import com.br.unifor.for_library.feature.adm.moderacao.modresenha.TelaModeracaoResenhas
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
import com.br.unifor.for_library.feature.TelaGestaoEventos
import com.br.unifor.for_library.feature.auth.ui.TelaCadastro
import android.net.Uri
import com.br.unifor.for_library.feature.aluno.perfil.ui.TelaEnvioObra
import com.br.unifor.for_library.feature.aluno.perfil.ui.TelaMeusPontos
import com.br.unifor.for_library.feature.aluno.estante.ui.TelaHistoricoLeitura
import com.br.unifor.for_library.feature.aluno.eventos.ui.TelaDetalhesEvento


private val rotasComPadding = setOf(
    Rota.HomeAluno.path,
    Rota.Acervo.path,
    Rota.Estante.path,
    Rota.Eventos.path,
    Rota.Perfil.path,
    // Rotas admin com bottom bar
    Rota.DashboardAdmin.path,
    Rota.AcervoAdmin.path,
    Rota.AdicionarLivro.path,
    Rota.EditarObra.path,
    Rota.ListaModeracao.path,
    Rota.GestaoUsuarios.path,
    Rota.ModeracaoObras.path,
    Rota.AnaliseObra.path,
    Rota.ModeracaoResenhas.path,
    Rota.AnaliseResenha.path,
    Rota.EventosAdmin.path,
    Rota.NotificacoesAdmin.path
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
    // ── Bug 1 corrigido: popUpTo(0) garante que toda a back stack é limpa ──
            onConfirm = {
                mostrarPopupSair = false
                navController.navigate(Rota.Login.path) {
                    popUpTo(0) { inclusive = true }
                }
            }
        )
    }
    // Rotas que usam a bottom bar do Admin
    val rotasAdmin = setOf(
        Rota.DashboardAdmin.path,
        Rota.AcervoAdmin.path,
        Rota.AdicionarLivro.path,
        Rota.EditarObra.path,
        Rota.ListaModeracao.path,
        Rota.GestaoUsuarios.path,
        Rota.ModeracaoObras.path,
        Rota.AnaliseObra.path,
        Rota.ModeracaoResenhas.path,
        Rota.AnaliseResenha.path,
        Rota.EventosAdmin.path,
        Rota.NotificacoesAdmin.path  // Bug 3: notificações no contexto admin
    )

    // O Scaffold gerencia o layout da tela, incluindo a BottomBar
    Scaffold(
        bottomBar = {
            if (rotaAtual in rotasAdmin) {
                AdminBottomBar(navController = navController)
            } else {
                ForLibraryBottomBar(navController = navController)
            }
        }
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
                    onIrParaEsqueciSenha = { navController.navigate(Rota.RecuperarSenha.path) },
                    onAdm                = {
                        navController.navigate(Rota.DashboardAdmin.path) {
                            popUpTo(Rota.Login.path) { inclusive = true }
                        }
                    }
                )
            }

            composable(Rota.RecuperarSenha.path) {
                TelaRecuperarSenha(onVoltar = { navController.popBackStack() })
            }

            composable(Rota.Cadastro.path) {
                TelaCadastro(
                    onVoltar = { navController.popBackStack() },
                    onCadastrarSucesso = {
                        navController.navigate(Rota.HomeAluno.path) {
                            popUpTo(Rota.Cadastro.path) { inclusive = true }
                        }
                    },
                    onIrParaLogin = { navController.popBackStack() }
                )
            }

            // ── Home ──────────────────────────────────────────────────────────
            composable(Rota.HomeAluno.path) {
                TelaHomeAluno(
                    onPontosClick        = { navController.navigate(Rota.MeusPontos.path) },
                    onSinoClick          = { navController.navigate(Rota.Notificacoes.path) },
                    onContinueLendoClick = { /* TODO: Rota.Leitor */ },
                    onVerTodosClick      = { navController.navigate(Rota.Acervo.path) },
                    onLivroClick = { navController.navigate(Rota.DetalhesLivro.criarRota("livro_id_exemplo")) }
                )
            }

            composable(Rota.DetalhesLivro.path) { backStackEntry ->
                val livroId = backStackEntry.arguments?.getString("livroId") ?: ""

                TelaDetalhesLivro(
                    livroId = livroId,
                    onVoltar = { navController.popBackStack() },
                    onNotificacoes = { /* TODO */ },
                    onLerAgora = {
                        // Bug 2: Uri.encode evita crash com títulos com caracteres especiais
                        navController.navigate(Rota.LeitorDigital.criarRota(livroId, Uri.encode("O Horizonte de Eventos")))
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

            // ── Notificações Admin (Bug 3: contexto separado) ─────────────────
            composable(Rota.NotificacoesAdmin.path) {
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
                    onSearchClick = { /* TODO: busca */ },
                    onHistoricoClick = { navController.navigate(Rota.HistoricoLeitura.path) }
                )
            }

            composable(Rota.EventosAdmin.path) {
                TelaGestaoEventos (
                )
            }


            composable(Rota.Eventos.path) {
                TelaEventos(
                    onSinoClick   = { navController.navigate(Rota.Notificacoes.path) },
                    onEventoClick = { eventoId -> navController.navigate(Rota.DetalhesEvento.criarRota(eventoId)) }
                )
            }

            composable(Rota.Perfil.path) {
                TelaPerfil(
                    onEditarPerfilClick = { navController.navigate(Rota.EditarPerfil.path) },
                    onEnvioObraClick = { navController.navigate(Rota.EnvioObra.path) },
                    onDuvidasClick = { navController.navigate(Rota.Duvida.path) },
                    onConfiguracoesClick = { navController.navigate(Rota.Configuracoes.path) },
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
            //NAVEGAÇÃO DE ADM

            //CONFIG ADM

            composable(Rota.ConfiguracoesSistema.path) {
                TelaConfiguracoesSistema(
                    onVoltar = { navController.popBackStack() },

                    onSairClick = {
                        navController.navigate(Rota.Login.path) {
                            popUpTo(0) { inclusive = true } // Limpa todo o histórico
                        }
                    }
                )
            }


            // ── Dashboard Admin (RF26) ───────────────────────────────────────
            composable(Rota.DashboardAdmin.path) {
                TelaDashboardAdmin(
                    onSinoClick = { navController.navigate(Rota.NotificacoesAdmin.path) }, // Bug 3: rota admin
                    onConfiguracoesClick = { navController.navigate(Rota.ConfiguracoesSistema.path) }, // Bug 7
                    onVerTodasAtividades = { /* TODO */ }
                )
            }

            // ── Gestão de Acervo (RF27) ──────────────────────────────────────
            composable(Rota.AcervoAdmin.path) {
                TelaGestaoAcervo(
                    onVoltar = { navController.popBackStack() },
                    onAdicionarLivro = { navController.navigate(Rota.AdicionarLivro.path) },
                    onEditarLivro = { livroId ->
                        navController.navigate(Rota.EditarObra.criarRota(livroId))
                    },
                    onExcluirLivro = { /* TODO */ }
                )
            }

            // ── Editar Obra (RF29) ───────────────────────────────────────────
            composable(Rota.EditarObra.path) { backStackEntry ->
                val livroId = backStackEntry.arguments?.getString("livroId") ?: ""
                TelaEditarObra(
                    livroId = livroId,
                    onVoltar = { navController.popBackStack() },
                    onAtualizar = { navController.popBackStack() },
                    onCancelar = { navController.popBackStack() }
                )
            }

            // ── Lista de Moderação ───────────────────────────────────────────
            composable(Rota.ListaModeracao.path) {
                TelaListaModeracaoAdmin(
                    onModeracaoUsuarios = { navController.navigate(Rota.GestaoUsuarios.path) },
                    onModeracaoObras    = { navController.navigate(Rota.ModeracaoObras.path) },
                    onModeracaoResenhas = { navController.navigate(Rota.ModeracaoResenhas.path) }
                )
            }

            // ── Gestão de Usuários (RF38 mínima → trigger RF39) ──────────────
            composable(Rota.GestaoUsuarios.path) {
                TelaGestaoUsuarios(onVoltar = { navController.popBackStack() })
            }

            // ── Moderação de Obras ────────────────────────────────────────────
            composable(Rota.ModeracaoObras.path) {
                TelaModeracaoObras(
                    onVoltar = { navController.popBackStack() },
                    onRevisarObra = { obraId ->
                        navController.navigate(Rota.AnaliseObra.criarRota(obraId))
                    }
                )
            }

            // ── Análise de Obra ───────────────────────────────────────────────
            composable(Rota.AnaliseObra.path) { backStackEntry ->
                val obraId = backStackEntry.arguments?.getString("obraId") ?: ""
                TelaAnaliseObra(
                    obraId = obraId,
                    onVoltar = { navController.popBackStack() },
                    onAprovar = { navController.popBackStack() },
                    onRejeitar = { _ -> navController.popBackStack() }
                )
            }

            // Moderação de obras

            composable(Rota.AdicionarLivro.path) {
                TelaAdicionarObra(
                    onVoltar = { navController.popBackStack() }
                )
            }


            // MOD RESENHA
            composable(Rota.ModeracaoResenhas.path) {
                TelaModeracaoResenhas(
                    onVoltar = { navController.popBackStack() },
                    onResenhaClick = { idDaResenha ->
                        navController.navigate(Rota.AnaliseResenha.criarRota(idDaResenha))
                    }
                )
            }


            composable(Rota.AnaliseResenha.path) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("resenhaId") ?: ""

                TelaAnaliseResenha(
                    resenhaId = id,
                    onVoltar = { navController.popBackStack() },
                    onAprovar = {
                        /* Lógica de aprovação */
                        navController.popBackStack()
                    },
                    onRejeitar = { motivo ->
                        /* Lógica de rejeição usando o motivo */
                        navController.popBackStack()
                    }
                )
            }

            // ── Novas telas Aluno ─────────────────────────────────────────────

            composable(Rota.EnvioObra.path) {
                TelaEnvioObra(
                    onVoltar = { navController.popBackStack() },
                    onObraEnviada = { navController.popBackStack() }
                )
            }

            composable(Rota.MeusPontos.path) {
                TelaMeusPontos(
                    onVoltar = { navController.popBackStack() }
                )
            }

            composable(Rota.HistoricoLeitura.path) {
                TelaHistoricoLeitura(
                    onVoltar = { navController.popBackStack() },
                    onLivroClick = { livroId ->
                        navController.navigate(Rota.DetalhesLivro.criarRota(livroId.toString()))
                    }
                )
            }

            composable(Rota.DetalhesEvento.path) { backStackEntry ->
                val eventoId = backStackEntry.arguments?.getString("eventoId")?.toIntOrNull() ?: 1
                TelaDetalhesEvento(
                    eventoId = eventoId,
                    onVoltar = { navController.popBackStack() },
                    onAdicionarCalendario = { /* TODO: integrar calendário */ }
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

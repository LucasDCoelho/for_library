package com.br.unifor.for_library.core.navigation;

sealed class Rota(val path: String) {
    // Rotas de Entrada (Sem BottomBar)
    object Splash         : Rota("splash")
    object Login          : Rota("login")
    object Cadastro       : Rota("cadastro")
    object RecuperarSenha : Rota("recuperar_senha")
    object Notificacoes   : Rota("notificacoes")
    object EditarPerfil   : Rota("editar_perfil")
    object Duvida         : Rota("duvida")
    object Configuracoes  : Rota("configuracoes")

    // NOVAS ROTAS ADM
    object DashboardAdmin : Rota("dashboard_admin")
    object AcervoAdmin : Rota("acervo_admin")
    object AdicionarLivro : Rota("adicionar_livro")

    object EditarObra : Rota("editar_obra/{livroId}") {
        fun criarRota(livroId: String) = "editar_obra/$livroId"
    }

    object PainelModeracao : Rota("painel_moderacao")

    object GestaoUsuarios : Rota("gestao_usuarios")

    object ModeracaoResenhas : Rota("moderacao_resenhas")

    object AnaliseResenha : Rota("analise_resenha/{resenhaId}") {
        fun criarRota(resenhaId: String) = "analise_resenha/$resenhaId"
    }

    object ConfiguracoesSistema : Rota("configuracoes_sistema")

    // Rotas da Bottom Navigation (Aluno)
    object HomeAluno : Rota("home_aluno")
    object Acervo    : Rota("acervo")
    object Estante   : Rota("estante")
    object Eventos   : Rota("eventos")
    object Perfil    : Rota("perfil")

    object DetalhesLivro : Rota("detalhes_livro/{livroId}") {
        fun criarRota(livroId: String) = "detalhes_livro/$livroId"
    }

    object LeitorDigital : Rota("leitor/{livroId}/{titulo}") {
        fun criarRota(livroId: String, titulo: String) = "leitor/$livroId/$titulo"
    }

    object AvalicaoLivro : Rota("avaliacao")
}

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
}

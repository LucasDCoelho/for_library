package com.br.unifor.for_library.core.navigation

sealed class Rota(val path: String) {
    object Login      : Rota("login")
    object Cadastro   : Rota("cadastro")
    object HomeAluno  : Rota("home_aluno")
    object Acervo     : Rota("acervo")
    object Estante    : Rota("estante")
    object Eventos    : Rota("eventos")
    object Perfil     : Rota("perfil")
}
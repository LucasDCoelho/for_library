package com.br.unifor.for_library.feature.auth

private val dominiosInstitucionais = setOf("@unifor.br", "@edu.unifor.br")
private const val DOMINIO_ADMIN = "@unifor.br"

fun emailInstitucionalValido(email: String): Boolean {
    val normalizado = email.trim().lowercase()
    return dominiosInstitucionais.any { normalizado.endsWith(it) }
}

fun obterTipoPorEmail(email: String): String {
    val normalizado = email.trim().lowercase()
    return if (normalizado.endsWith(DOMINIO_ADMIN)) {
        TIPO_ADMIN
    } else {
        TIPO_ALUNO
    }
}

class UsuarioBloqueadoException : Exception("Conta bloqueada")

fun traduzirErroAuth(e: Exception): String {
    if (e is UsuarioBloqueadoException) {
        return "Sua conta foi bloqueada. Entre em contato com a administração"
    }
    val msg = e.message?.lowercase() ?: ""
    return when {
        "invalid login credentials" in msg ->
            "E-mail ou senha incorretos"
        "email not confirmed" in msg ->
            "E-mail não confirmado. Verifique sua caixa de entrada"
        "user already registered" in msg || "already registered" in msg ->
            "Este e-mail já está cadastrado. Faça login ou recupere sua senha"
        "password should be at least" in msg ->
            "A senha deve ter pelo menos 6 caracteres"
        "rate limit" in msg || "email rate limit" in msg ->
            "Muitas tentativas. Aguarde alguns minutos e tente novamente"
        "signup disabled" in msg ->
            "Cadastro temporariamente indisponível. Tente novamente mais tarde"
        "permission denied" in msg || "row-level security" in msg ->
            "Você não tem permissão para realizar esta ação"
        "network" in msg || "unable to resolve host" in msg || "failed to connect" in msg ->
            "Sem conexão com a internet. Verifique sua rede e tente novamente"
        "timeout" in msg || "timed out" in msg ->
            "A requisição demorou muito. Verifique sua conexão e tente novamente"
        else -> "Ocorreu um erro inesperado. Tente novamente"
    }
}


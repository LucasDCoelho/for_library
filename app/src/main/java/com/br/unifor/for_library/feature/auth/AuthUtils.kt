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




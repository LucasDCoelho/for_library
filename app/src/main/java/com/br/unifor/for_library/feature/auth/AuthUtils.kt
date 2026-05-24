package com.br.unifor.for_library.feature.auth

private val dominiosInstitucionais = setOf("@unifor.br", "@edu.unifor.br")

fun emailInstitucionalValido(email: String): Boolean {
    val normalizado = email.trim().lowercase()
    return dominiosInstitucionais.any { normalizado.endsWith(it) }
}




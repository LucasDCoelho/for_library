package com.br.unifor.for_library.feature.auth

import com.br.unifor.for_library.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val TABELA_USUARIO = "usuario"
private const val TABELA_USUARIOS = "usuarios"
const val TIPO_ALUNO = "ALUNO"
const val TIPO_ADMIN = "ADMIN"

@Serializable
private data class UsuarioPublicoPayload(
    @SerialName("auth_user_id")
    val authUserId: String,
    val nome: String,
    val matricula: String,
    val email: String,
    val tipo: String
)

@Serializable
private data class UsuarioTipoResponse(
    val tipo: String? = null
)

fun normalizarTipoUsuario(tipo: String?): String {
    return when (tipo?.trim()?.uppercase()) {
        TIPO_ADMIN -> TIPO_ADMIN
        else -> TIPO_ALUNO
    }
}

fun tipoAlunoPadrao(): String = TIPO_ALUNO

fun isAdmin(tipo: String?): Boolean = normalizarTipoUsuario(tipo) == TIPO_ADMIN

suspend fun inserirUsuarioPublico(
    nome: String,
    matricula: String,
    email: String,
    tipo: String = tipoAlunoPadrao()
) {
    val usuarioAutenticado = supabase.auth.currentUserOrNull()
        ?: throw IllegalStateException("Sessao invalida para criar usuario publico")

    val payload = UsuarioPublicoPayload(
        authUserId = usuarioAutenticado.id,
        nome = nome.trim(),
        matricula = matricula.trim(),
        email = email.trim().lowercase(),
        tipo = normalizarTipoUsuario(tipo)
    )

    var ultimoErro: Exception? = null
    for (tabela in listOf(TABELA_USUARIO, TABELA_USUARIOS)) {
        try {
            supabase.from(tabela).insert(payload)
            return
        } catch (e: Exception) {
            ultimoErro = e
        }
    }

    throw ultimoErro ?: IllegalStateException("Nao foi possivel salvar o usuario na tabela publica")
}

suspend fun buscarTipoUsuarioAtual(): String {
    val usuarioAutenticado = supabase.auth.currentUserOrNull() ?: return TIPO_ALUNO

    for (tabela in listOf(TABELA_USUARIO, TABELA_USUARIOS)) {
        try {
            val resultado = supabase
                .from(tabela)
                .select {
                    filter {
                        eq("auth_user_id", usuarioAutenticado.id)
                    }
                    limit(1)
                }
                .decodeList<UsuarioTipoResponse>()

            val tipo = resultado.firstOrNull()?.tipo
            if (tipo != null) {
                return normalizarTipoUsuario(tipo)
            }
        } catch (_: Exception) {
            // tenta a proxima convencao de nome de tabela
        }
    }

    return TIPO_ALUNO
}


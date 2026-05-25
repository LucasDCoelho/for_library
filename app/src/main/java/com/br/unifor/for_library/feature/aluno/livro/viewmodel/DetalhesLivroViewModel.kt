package com.br.unifor.for_library.feature.aluno.livro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Serializable
data class LivroDetalhes(
    val id: Int,
    val titulo: String,
    val autor: String,
    val genero: String,
    val ano_publicacao: Int? = null,
    val sinopse: String? = null,
    val isbn: String? = null,
    val nota_media: Double = 0.0,
    val qtd_avaliacoes: Int = 0
)

@Serializable
private data class UsuarioResenha(val nome: String)

@Serializable
private data class ResenhaDb(
    val id: Int,
    val nota: Int,
    val texto: String? = null,
    val data_publicacao: String? = null,
    val usuarios: UsuarioResenha
)

data class ResenhaUi(
    val id: Int,
    val autorNome: String,
    val nota: Int,
    val texto: String,
    val tempoRelativo: String
)

data class DetalhesLivroState(
    val isLoading: Boolean = true,
    val livro: LivroDetalhes? = null,
    val resenhas: List<ResenhaUi> = emptyList(),
    val favoritado: Boolean = false,
    val error: String? = null
)

class DetalhesLivroViewModel : ViewModel() {
    private val _state = MutableStateFlow(DetalhesLivroState())
    val state: StateFlow<DetalhesLivroState> = _state.asStateFlow()

    private var usuarioId: Int? = null

    fun carregarDetalhes(livroId: Int) {
        if (_state.value.livro?.id == livroId) return
        viewModelScope.launch {
            _state.value = DetalhesLivroState(isLoading = true)
            try {
                val userAuth = supabase.auth.currentUserOrNull()
                    ?: throw Exception("Não autenticado")

                val usuario = supabase.from("usuarios")
                    .select { filter { eq("auth_user_id", userAuth.id) } }
                    .decodeSingle<UsuarioIdModel>()
                usuarioId = usuario.id

                val livro = supabase.from("livros")
                    .select { filter { eq("id", livroId) } }
                    .decodeSingle<LivroDetalhes>()

                val resenhas = supabase.from("resenhas")
                    .select(Columns.raw("id, nota, texto, data_publicacao, usuarios(nome)")) {
                        filter {
                            eq("livro_id", livroId)
                            eq("status", "Aprovado")
                        }
                        order("data_publicacao", Order.DESCENDING)
                    }
                    .decodeList<ResenhaDb>()
                    .map { it.toUi() }

                val favoritoCount = supabase.from("favoritos")
                    .select { filter { eq("usuario_id", usuario.id); eq("livro_id", livroId) } }
                    .decodeList<FavoritoId>()

                _state.value = DetalhesLivroState(
                    isLoading = false,
                    livro = livro,
                    resenhas = resenhas,
                    favoritado = favoritoCount.isNotEmpty()
                )
            } catch (e: Exception) {
                _state.value = DetalhesLivroState(
                    isLoading = false,
                    error = e.message ?: "Erro ao carregar livro"
                )
            }
        }
    }

    fun toggleFavorito(livroId: Int) {
        val uid = usuarioId ?: return
        val eraFavoritado = _state.value.favoritado
        _state.value = _state.value.copy(favoritado = !eraFavoritado)
        viewModelScope.launch {
            try {
                if (eraFavoritado) {
                    supabase.from("favoritos").delete {
                        filter { eq("usuario_id", uid); eq("livro_id", livroId) }
                    }
                } else {
                    supabase.from("favoritos").insert(
                        FavoritoPayload(usuario_id = uid, livro_id = livroId)
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(favoritado = eraFavoritado)
            }
        }
    }

    private fun ResenhaDb.toUi() = ResenhaUi(
        id = id,
        autorNome = usuarios.nome,
        nota = nota,
        texto = texto ?: "",
        tempoRelativo = tempoRelativo(data_publicacao)
    )

    private fun tempoRelativo(dataIso: String?): String {
        if (dataIso == null) return ""
        return try {
            val dt = LocalDateTime.parse(dataIso.take(19))
            val dias = ChronoUnit.DAYS.between(dt, LocalDateTime.now())
            when {
                dias == 0L -> "Hoje"
                dias == 1L -> "Há 1 dia"
                dias < 7  -> "Há $dias dias"
                dias < 14 -> "Há 1 semana"
                dias < 30 -> "Há ${dias / 7} semanas"
                dias < 60 -> "Há 1 mês"
                dias < 365 -> "Há ${dias / 30} meses"
                else -> "Há ${dias / 365} ano(s)"
            }
        } catch (e: Exception) { "" }
    }
}

@Serializable private data class UsuarioIdModel(val id: Int)
@Serializable private data class FavoritoId(val id: Int)
@Serializable private data class FavoritoPayload(val usuario_id: Int, val livro_id: Int)

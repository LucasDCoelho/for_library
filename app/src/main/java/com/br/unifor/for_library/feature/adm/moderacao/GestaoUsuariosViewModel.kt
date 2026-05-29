package com.br.unifor.for_library.feature.adm.moderacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
private data class UsuarioAdminDb(
    val id: Int,
    val nome: String,
    val matricula: String,
    val email: String? = null,
    val status: String = "Ativo",
    val foto_perfil_url: String? = null,
    val resenhas_inadequadas: Int = 0
)

data class UsuarioAdminItem(
    val id: Int,
    val nome: String,
    val matricula: String,
    val email: String,
    val bloqueado: Boolean,
    val fotoPerfil: String?,
    val resenhasInadequadas: Int = 0
)

data class GestaoUsuariosState(
    val isLoading: Boolean = true,
    val todos: List<UsuarioAdminItem> = emptyList(),
    val erro: String? = null
)

class GestaoUsuariosViewModel : ViewModel() {
    private val _state = MutableStateFlow(GestaoUsuariosState())
    val state: StateFlow<GestaoUsuariosState> = _state.asStateFlow()

    private val _busca = MutableStateFlow("")
    val busca: StateFlow<String> = _busca.asStateFlow()

    val usuariosFiltrados: StateFlow<List<UsuarioAdminItem>> =
        combine(_state, _busca) { state, query ->
            if (query.isBlank()) state.todos
            else state.todos.filter {
                it.nome.contains(query, ignoreCase = true) ||
                it.matricula.contains(query, ignoreCase = true)
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        carregarUsuarios()
    }

    fun carregarUsuarios() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, erro = null)
            try {
                val usuarios = supabase.from("usuarios")
                    .select()
                    .decodeList<UsuarioAdminDb>()
                    .map { it.toItem() }

                _state.value = _state.value.copy(isLoading = false, todos = usuarios)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    erro = "Erro ao carregar usuários: ${e.message}"
                )
            }
        }
    }

    fun onBuscaChange(query: String) {
        _busca.value = query
    }

    fun alterarStatus(userId: Int, bloqueado: Boolean) {
        viewModelScope.launch {
            try {
                val novoStatus = if (bloqueado) "Bloqueado" else "Ativo"
                supabase.from("usuarios").update(mapOf("status" to novoStatus)) {
                    filter { eq("id", userId) }
                }
                _state.value = _state.value.copy(
                    todos = _state.value.todos.map { u ->
                        if (u.id == userId) u.copy(bloqueado = bloqueado) else u
                    }
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    erro = "Erro ao alterar status: ${e.message}"
                )
            }
        }
    }

    fun consumirErro() {
        _state.value = _state.value.copy(erro = null)
    }

    private fun UsuarioAdminDb.toItem() = UsuarioAdminItem(
        id = id,
        nome = nome,
        matricula = matricula,
        email = email ?: "",
        bloqueado = status.equals("Bloqueado", ignoreCase = true),
        fotoPerfil = foto_perfil_url,
        resenhasInadequadas = resenhas_inadequadas
    )
}

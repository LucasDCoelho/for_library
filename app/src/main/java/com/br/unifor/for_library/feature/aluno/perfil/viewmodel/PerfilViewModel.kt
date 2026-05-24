package com.br.unifor.for_library.feature.aluno.perfil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
private data class UsuarioPerfilResponse(
    val id: Int,
    val nome: String = "",
    val matricula: String = "",
    val foto_perfil: String? = null,
    val pontos_gamificacao: Int = 0
)

@Serializable
private data class ItemId(val id: Int)

data class PerfilState(
    val isLoading: Boolean = true,
    val nome: String = "",
    val matricula: String = "",
    val fotoPerfil: String? = null,
    val livrosLidos: Int = 0,
    val resenhasAprovadas: Int = 0,
    val pontos: Int = 0,
    val error: String? = null
)

class PerfilViewModel : ViewModel() {
    private val _state = MutableStateFlow(PerfilState())
    val state: StateFlow<PerfilState> = _state.asStateFlow()

    init {
        carregarPerfil()
    }

    fun carregarPerfil() {
        viewModelScope.launch {
            _state.value = PerfilState(isLoading = true)
            try {
                val userAuth = supabase.auth.currentUserOrNull()
                    ?: throw Exception("Usuário não autenticado")

                val usuario = supabase.from("usuarios")
                    .select { filter { eq("auth_user_id", userAuth.id) } }
                    .decodeSingle<UsuarioPerfilResponse>()

                val livrosLidos = supabase.from("progresso_leitura")
                    .select {
                        filter {
                            eq("usuario_id", usuario.id)
                            eq("status", "Concluído")
                        }
                    }
                    .decodeList<ItemId>()
                    .size

                val resenhasAprovadas = supabase.from("resenhas")
                    .select {
                        filter {
                            eq("usuario_id", usuario.id)
                            eq("status", "Aprovado")
                        }
                    }
                    .decodeList<ItemId>()
                    .size

                _state.value = PerfilState(
                    isLoading = false,
                    nome = usuario.nome,
                    matricula = usuario.matricula,
                    fotoPerfil = usuario.foto_perfil,
                    livrosLidos = livrosLidos,
                    resenhasAprovadas = resenhasAprovadas,
                    pontos = usuario.pontos_gamificacao
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Erro ao carregar perfil"
                )
            }
        }
    }
}

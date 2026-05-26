package com.br.unifor.for_library.feature.aluno.perfil.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
private data class UsuarioEdicaoResponse(
    val nome: String = "",
    val biografia: String? = null,
    val matricula: String = "",
    val email: String = "",
    @SerialName("foto_perfil") val fotoPerfil: String? = null
)

@Serializable
private data class UsuarioUpdate(
    val nome: String,
    val biografia: String,
    @SerialName("foto_perfil") val fotoPerfil: String?
)

data class EdicaoPerfilState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val nome: String = "",
    val biografia: String = "",
    val matricula: String = "",
    val email: String = "",
    val fotoUrl: String? = null,
    val novaFotoUri: Uri? = null,
    val saveSuccess: Boolean = false,
    val error: String? = null
)

class EdicaoPerfilViewModel : ViewModel() {
    private val _state = MutableStateFlow(EdicaoPerfilState())
    val state: StateFlow<EdicaoPerfilState> = _state.asStateFlow()

    init {
        carregarDadosAtuais()
    }

    private fun carregarDadosAtuais() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val userAuth = supabase.auth.currentUserOrNull()
                    ?: throw Exception("Usuário não autenticado")

                val usuario = supabase.from("usuarios")
                    .select { filter { eq("auth_user_id", userAuth.id) } }
                    .decodeSingle<UsuarioEdicaoResponse>()

                _state.value = _state.value.copy(
                    isLoading = false,
                    nome = usuario.nome,
                    biografia = usuario.biografia ?: "",
                    matricula = usuario.matricula,
                    email = usuario.email,
                    fotoUrl = usuario.fotoPerfil
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Erro ao carregar dados: ${e.message}"
                )
            }
        }
    }

    fun onNomeChange(novoNome: String) {
        _state.value = _state.value.copy(nome = novoNome)
    }

    fun onBiografiaChange(novaBio: String) {
        _state.value = _state.value.copy(biografia = novaBio)
    }

    fun onFotoSelected(uri: Uri?) {
        _state.value = _state.value.copy(novaFotoUri = uri)
    }

    fun salvarAlteracoes(context: Context) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true, error = null)
            try {
                val userAuth = supabase.auth.currentUserOrNull()
                    ?: throw Exception("Usuário não autenticado")

                var finalFotoUrl = _state.value.fotoUrl

                // 1. Upload da foto se houver uma nova
                _state.value.novaFotoUri?.let { uri ->
                    val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    if (bytes != null) {
                        val fileName = "avatar_${userAuth.id}_${System.currentTimeMillis()}.jpg"
                        val bucket = supabase.storage.from("avatars")
                        bucket.upload(fileName, bytes)
                        finalFotoUrl = bucket.publicUrl(fileName)
                    }
                }

                // 2. Update no banco
                val updateData = UsuarioUpdate(
                    nome = _state.value.nome,
                    biografia = _state.value.biografia,
                    fotoPerfil = finalFotoUrl
                )

                supabase.from("usuarios").update(updateData) {
                    filter { eq("auth_user_id", userAuth.id) }
                }

                _state.value = _state.value.copy(
                    isSaving = false,
                    saveSuccess = true,
                    fotoUrl = finalFotoUrl,
                    novaFotoUri = null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isSaving = false,
                    error = "Erro ao salvar: ${e.message}"
                )
            }
        }
    }

    fun resetSuccess() {
        _state.value = _state.value.copy(saveSuccess = false)
    }
}

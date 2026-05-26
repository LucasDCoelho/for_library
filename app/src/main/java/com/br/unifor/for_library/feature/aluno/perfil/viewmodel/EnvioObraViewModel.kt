package com.br.unifor.for_library.feature.aluno.perfil.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
private data class UsuarioId(val id: Int)

@Serializable
private data class ObraAutorialPayload(
    @SerialName("usuario_id") val usuarioId: Int,
    val titulo: String,
    val genero: String,
    @SerialName("sinopse_curta") val sinopseCurta: String,
    @SerialName("arquivo_pdf_url") val arquivoPdfUrl: String,
    val status: String = "Pendente"
)

sealed class EnvioObraUiState {
    object Idle : EnvioObraUiState()
    object Loading : EnvioObraUiState()
    object Success : EnvioObraUiState()
    data class Error(val message: String) : EnvioObraUiState()
}

class EnvioObraViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow<EnvioObraUiState>(EnvioObraUiState.Idle)
    val uiState: StateFlow<EnvioObraUiState> = _uiState.asStateFlow()

    fun enviarObra(titulo: String, genero: String, sinopse: String, pdfUri: Uri) {
        viewModelScope.launch {
            _uiState.value = EnvioObraUiState.Loading
            try {
                val context = getApplication<Application>()
                val userAuth = supabase.auth.currentUserOrNull()
                    ?: throw Exception("Usuário não autenticado")

                val usuario = supabase.from("usuarios")
                    .select { filter { eq("auth_user_id", userAuth.id) } }
                    .decodeSingle<UsuarioId>()

                val pdfBytes = context.contentResolver.openInputStream(pdfUri)
                    ?.use { it.readBytes() }
                    ?: throw Exception("Não foi possível ler o arquivo PDF")

                val fileName = "${userAuth.id}_${System.currentTimeMillis()}.pdf"
                supabase.storage.from("obras").upload(fileName, pdfBytes)
                val publicUrl = supabase.storage.from("obras").publicUrl(fileName)

                supabase.from("obras_autorais").insert(
                    ObraAutorialPayload(
                        usuarioId = usuario.id,
                        titulo = titulo.trim(),
                        genero = genero,
                        sinopseCurta = sinopse.trim(),
                        arquivoPdfUrl = publicUrl
                    )
                )

                _uiState.value = EnvioObraUiState.Success
            } catch (e: Exception) {
                _uiState.value = EnvioObraUiState.Error(e.message ?: "Erro ao enviar obra")
            }
        }
    }

    fun limparErro() {
        if (_uiState.value is EnvioObraUiState.Error) {
            _uiState.value = EnvioObraUiState.Idle
        }
    }
}

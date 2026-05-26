package com.br.unifor.for_library.feature.aluno.presentation.configuracoes

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Context.dataStore by preferencesDataStore(name = "settings")

class ConfigViewModel(context: Context) : ViewModel() {

    private val dataStore = context.dataStore

    companion object {
        val TEMA_ESCURO = booleanPreferencesKey("tema_escuro")
        val NOTIFICACOES_PUSH = booleanPreferencesKey("notificacoes_push")
    }

    val temaEscuro: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[TEMA_ESCURO] ?: false
    }

    val notificacoesPush: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[NOTIFICACOES_PUSH] ?: true
    }

    fun toggleTemaEscuro(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[TEMA_ESCURO] = enabled
            }
        }
    }

    fun toggleNotificacoesPush(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[NOTIFICACOES_PUSH] = enabled
            }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                supabase.auth.signOut()
                onSuccess()
            } catch (e: Exception) {
                // Mesmo se falhar o signout no servidor, podemos limpar localmente
                onSuccess()
            }
        }
    }
}

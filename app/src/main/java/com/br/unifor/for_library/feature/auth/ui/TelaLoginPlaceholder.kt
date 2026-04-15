package com.br.unifor.for_library.feature.auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TelaLoginPlaceholder(
    onLoginSucesso: () -> Unit,
    onIrParaCadastro: () -> Unit,
    onIrParaEsqueciSenha: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "ForLibrary - Tela de Login", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onLoginSucesso) {
            Text("Simular Login")
        }

        TextButton(onClick = onIrParaCadastro) {
            Text("Primeiro Acesso? Cadastre-se")
        }
        //Botão improvisado
        TextButton(onClick = onIrParaEsqueciSenha) {
            Text("Esqueceu sua senha?")
        }
    }
}
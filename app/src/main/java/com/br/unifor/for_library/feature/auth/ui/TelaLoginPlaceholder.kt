package com.br.unifor.for_library.feature.auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TelaLoginPlaceholder(
    onLoginSucesso: () -> Unit = {},
    onIrParaCadastro: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "FOR Library",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1565C0)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Placeholder — tela de login",
            fontSize = 13.sp,
            color = Color(0xFF9E9E9E)
        )

        Spacer(Modifier.height(40.dp))

        Button(
            onClick = onLoginSucesso,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
        ) {
            Text("Entrar (demo)", color = Color.White)
        }

        Spacer(Modifier.height(12.dp))

        TextButton(onClick = onIrParaCadastro) {
            Text("Criar conta", color = Color(0xFF1565C0))
        }
    }
}
package com.br.unifor.for_library.feature.auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TelaLoginPlaceholder(
    onLoginSucesso: () -> Unit,
    onIrParaCadastro: () -> Unit,
    onIrParaEsqueciSenha: () -> Unit,
    onEsqueceuSenha: () -> Unit = {},
    onAdm: () -> Unit = {},
) {
    var email             by rememberSaveable { mutableStateOf("") }
    var password          by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var showError         by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // RF02.1 e RF02.2: Títulos alinhados à esquerda
        Text(
            text  = "ForLibrary",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text  = "Acesse sua conta institucional para continuar.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(40.dp))

        // RF02.3: Campo Matrícula/Email
        OutlinedTextField(
            value         = email,
            onValueChange = { email = it; showError = false },
            label         = { Text("Matrícula ou Email Institucional") },
            modifier      = Modifier.fillMaxWidth(),
            singleLine    = true,
            isError       = showError
        )
        Spacer(Modifier.height(8.dp))

        // RF02.4: Campo Senha Oculto + Ícone de Olho
        OutlinedTextField(
            value         = password,
            onValueChange = { password = it; showError = false },
            label         = { Text("Senha") },
            modifier      = Modifier.fillMaxWidth(),
            singleLine    = true,
            isError       = showError,
            supportingText = if (showError) {
                { Text(text = "Credenciais inválidas", color = MaterialTheme.colorScheme.error) }
            } else null,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (isPasswordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
                val desc  = if (isPasswordVisible) "Ocultar senha" else "Mostrar senha"
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = desc)
                }
            }
        )
        Spacer(modifier = Modifier.height(24.dp))

        // RF02.5: Botão Entrar (Apenas Navegação)
        Button(
            onClick  = { onLoginSucesso() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C64F2)),
            shape  = MaterialTheme.shapes.small
        ) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text("Entrar", fontSize = 16.sp)
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Seta Entrar")
            }
        }
        Spacer(Modifier.height(8.dp))

        TextButton(
            onClick  = onIrParaEsqueciSenha,
            modifier = Modifier.semantics { role = Role.Button }
        ) {
            Text(
                text  = "Esqueceu sua senha?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Botão ADM
        Button(
            onClick  = { onAdm() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1264c2)),
            shape  = MaterialTheme.shapes.small
        ) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text("Entrar ADM", fontSize = 16.sp)
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Seta Entrar")
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Spacer(Modifier.height(24.dp))

        // RF02.7: Botão Primeiro Acesso (Navegação)
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(text = "Primeiro Acesso? ", color = Color.Gray, fontSize = 14.sp)
            TextButton(onClick = { onIrParaCadastro() }) {
                Text(
                    text       = "Cadastre-se",
                    style      = MaterialTheme.typography.labelLarge,
                    color      = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 14.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun TelaLoginPlaceholderPreview() {
    TelaLoginPlaceholder(
        onLoginSucesso       = {},
        onIrParaCadastro     = {},
        onIrParaEsqueciSenha = {}
    )
}
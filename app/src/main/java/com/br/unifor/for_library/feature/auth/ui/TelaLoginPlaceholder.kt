package com.br.unifor.for_library.feature.auth.ui

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.br.unifor.for_library.R
import com.br.unifor.for_library.feature.auth.buscarTipoUsuarioAtual
import com.br.unifor.for_library.feature.auth.emailInstitucionalValido
import com.br.unifor.for_library.feature.auth.isAdmin
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch

@Composable
fun TelaLoginPlaceholder(
    onLoginSucesso: () -> Unit,
    onLoginAdmin: () -> Unit = onLoginSucesso,
    onIrParaCadastro: () -> Unit,
    onIrParaEsqueciSenha: () -> Unit,
) {
    var email             by rememberSaveable { mutableStateOf("") }
    var password          by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isLoading         by rememberSaveable { mutableStateOf(false) }
    var errorMessage      by rememberSaveable { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Image(
            painter = painterResource(id = R.drawable.logo_for_library),
            contentDescription = "Logo ForLibrary",
            modifier = Modifier.size(64.dp),
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )

        Spacer(modifier = Modifier.height(16.dp))

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
            onValueChange = { email = it; errorMessage = null },
            label         = { Text("Matrícula ou e-mail institucional") },
            placeholder   = { Text("Matrícula ou Email") },
            modifier      = Modifier.fillMaxWidth(),
            singleLine    = true,
            isError       = errorMessage != null
        )
        Spacer(Modifier.height(8.dp))

        // RF02.4: Campo Senha Oculto + Ícone de Olho
        OutlinedTextField(
            value         = password,
            onValueChange = { password = it; errorMessage = null },
            label         = { Text("Senha") },
            modifier      = Modifier.fillMaxWidth(),
            singleLine    = true,
            isError       = errorMessage != null,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (isPasswordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
                val desc  = if (isPasswordVisible) "Ocultar senha" else "Mostrar senha"
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = desc)
                }
            }
        )
        if (errorMessage != null) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = errorMessage ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        // RF02.5 + RF02.8: botão com loading e autenticação real
        Button(
            onClick  = {
                if (!emailInstitucionalValido(email)) {
                    errorMessage = "Utilize um e-mail institucional válido"
                    return@Button
                }
                scope.launch {
                    isLoading = true
                    errorMessage = null
                    try {
                        supabase.auth.signInWith(Email) {
                            this.email = email.trim()
                            this.password = password
                        }
                        val tipoUsuario = buscarTipoUsuarioAtual()
                        if (isAdmin(tipoUsuario)) {
                            onLoginAdmin()
                        } else {
                            onLoginSucesso()
                        }
                    } catch (_: Exception) {
                        errorMessage = "Credenciais inválidas"
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C64F2)),
            shape  = MaterialTheme.shapes.small,
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = Color.White
                )
            } else {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text("Entrar", fontSize = 16.sp)
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Seta Entrar")
                }
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
package com.br.unifor.for_library.feature.auth.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    onEsqueceuSenha: () -> Unit = {} // Adicionado com valor padrão para não quebrar seu Rotas.kt
) {
    // Estados puramente visuais (para permitir digitação e ver a senha)
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    // Deixei true apenas para a tela ficar idêntica à imagem do protótipo que você mandou.
    // No futuro, isso virá de fora (da sua ViewModel).
    var showError by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // RF02.1 e RF02.2: Títulos alinhados à esquerda
        Text(
            text = "ForLibrary",
            fontSize = 32.sp,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Acesse sua conta institucional para continuar.",
            color = Color.DarkGray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        // RF02.3: Campo Matrícula/Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Matrícula ou Email Institucional") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        // RF02.4: Campo Senha Oculto + Ícone de Olho
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (isPasswordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = "Alternar visibilidade da senha")
                }
            }
        )

        // RF02.8: Mensagem de Erro Visual
        if (showError) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Erro",
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Credenciais inválidas",
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // RF02.5: Botão Entrar (Apenas Navegação)
        Button(
            onClick = { onLoginSucesso() }, // Navega direto sem validar nada!
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C64F2)),
            shape = MaterialTheme.shapes.small // Deixa o botão mais retangular, igual ao protótipo
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Entrar", fontSize = 16.sp)
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Seta Entrar")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // RF02.6: Botão Esqueceu a senha? (Navegação)
        Text(
            text = "Esqueceu a senha?",
            color = Color(0xFF1C64F2),
            fontSize = 14.sp,
            modifier = Modifier.clickable { onEsqueceuSenha() }
        )

        // Mola que empurra o resto para baixo
        Spacer(modifier = Modifier.weight(1f))

        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
        Spacer(modifier = Modifier.height(24.dp))

        // RF02.7: Botão Primeiro Acesso (Navegação)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Primeiro Acesso? ", color = Color.Gray, fontSize = 14.sp)
            Text(
                text = "Cadastre-se",
                color = Color(0xFF1C64F2),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onIrParaCadastro() }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}



// A anotação @Preview diz ao Android Studio: "Desenhe isso na tela ao lado!"
// showBackground = true coloca um fundo branco, simulando a tela do celular.
@Preview(showBackground = true)
@Composable
fun TelaLoginPreview() {
    // Aqui nós chamamos a sua tela, passando funções vazias "{}" só para o Preview funcionar
    TelaLoginPlaceholder(
        onLoginSucesso = {},
        onIrParaCadastro = {},
        onEsqueceuSenha = {}
    )
}

package com.br.unifor.for_library.feature.auth.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AzulPrimario = Color(0xFF1565C0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCadastro(
    onVoltar: () -> Unit = {},
    onCadastrarSucesso: () -> Unit = {},
    onIrParaLogin: () -> Unit = {}
) {
    // ✅ rememberSaveable: campos sobrevivem à rotação de tela
    var nomeCompleto by rememberSaveable { mutableStateOf("") }
    var matricula by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var confirmarSenha by rememberSaveable { mutableStateOf("") }
    var senhaVisivel by rememberSaveable { mutableStateOf(false) }
    var confirmarSenhaVisivel by rememberSaveable { mutableStateOf(false) }

    // ✅ derivedStateOf: re-calcula apenas quando as dependências mudam
    val emailJaEmUso by remember {
        derivedStateOf { email.isNotEmpty() && !email.contains("@") }
    }
    val emailInvalido by remember {
        derivedStateOf {
            email.isNotEmpty() && email.contains("@") && !email.endsWith("@institutocao.edu.br")
        }
    }
    val senhasIguais by remember {
        derivedStateOf { confirmarSenha.isEmpty() || senha == confirmarSenha }
    }
    val formularioValido by remember {
        derivedStateOf {
            nomeCompleto.isNotBlank() && matricula.isNotBlank() &&
            email.isNotBlank() && !emailJaEmUso && !emailInvalido &&
            senha.isNotBlank() && confirmarSenha.isNotBlank() && senhasIguais
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Crie sua conta",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )
            Text(
                text = "Preencha os dados abaixo para acessar a biblioteca digital.",
                fontSize = 14.sp,
                color = Color(0xFF757575),
                modifier = Modifier.padding(top = 6.dp, bottom = 24.dp)
            )

            // Nome Completo
            CampoTexto(
                label = "Nome Completo",
                valor = nomeCompleto,
                placeholder = "Ex: João Silva",
                onValorChange = { nomeCompleto = it }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Matrícula
            CampoTexto(
                label = "Matrícula",
                valor = matricula,
                placeholder = "Digite seu número de matrícula",
                onValorChange = { matricula = it },
                tipoTeclado = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Email Institucional
            CampoTexto(
                label = "Email Institucional",
                valor = email,
                placeholder = "nome@institutocao.edu.br",
                onValorChange = { email = it },
                tipoTeclado = KeyboardType.Email,
                isError = emailJaEmUso || emailInvalido
            )

            // ✅ Erro 1 — email já em uso (era duplicado com mensagem errada)
            if (emailJaEmUso) {
                MensagemErro("Email já em uso")
            }
            // ✅ Erro 2 — email não institucional
            if (emailInvalido) {
                MensagemErro("Utilize um email institucional")
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Senha
            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("Senha") },
                placeholder = { Text("••••••••") },
                visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                        Icon(
                            imageVector = if (senhaVisivel) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = Color(0xFF9E9E9E)
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AzulPrimario,
                    unfocusedBorderColor = Color(0xFFBDBDBD)
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Confirmar Senha
            OutlinedTextField(
                value = confirmarSenha,
                onValueChange = { confirmarSenha = it },
                label = { Text("Confirmar Senha") },
                placeholder = { Text("••••••••") },
                visualTransformation = if (confirmarSenhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                isError = !senhasIguais,
                trailingIcon = {
                    IconButton(onClick = { confirmarSenhaVisivel = !confirmarSenhaVisivel }) {
                        Icon(
                            imageVector = if (confirmarSenhaVisivel) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = Color(0xFF9E9E9E)
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AzulPrimario,
                    unfocusedBorderColor = Color(0xFFBDBDBD),
                    errorBorderColor = Color(0xFFD32F2F)
                )
            )
            if (!senhasIguais) {
                MensagemErro("Senhas não coincidem")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Info de termos
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = AzulPrimario,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Ao se cadastrar, você concorda com nossos termos de uso e política de privacidade de dados acadêmicos.",
                        fontSize = 12.sp,
                        color = Color(0xFF1565C0),
                        lineHeight = 17.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botão Cadastrar — ✅ habilitado apenas se formulário válido
            Button(
                onClick = onCadastrarSucesso,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario),
                enabled = formularioValido
            ) {
                Text(
                    text = "Cadastrar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Spacer(Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ "Fazer login" agora é clicável
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onIrParaLogin() },
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Já possui uma conta? ")
                        withStyle(SpanStyle(color = AzulPrimario, fontWeight = FontWeight.SemiBold)) {
                            append("Fazer login")
                        }
                    },
                    fontSize = 13.sp,
                    color = Color(0xFF757575)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun MensagemErro(texto: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = Color(0xFFD32F2F),
            modifier = Modifier.size(14.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = texto,
            fontSize = 12.sp,
            color = Color(0xFFD32F2F)
        )
    }
}

@Composable
private fun CampoTexto(
    label: String,
    valor: String,
    placeholder: String,
    onValorChange: (String) -> Unit,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(label) },
        placeholder = { Text(placeholder, color = Color(0xFFBDBDBD)) },
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF1565C0),
            unfocusedBorderColor = Color(0xFFBDBDBD)
        ),
        singleLine = true
    )
}


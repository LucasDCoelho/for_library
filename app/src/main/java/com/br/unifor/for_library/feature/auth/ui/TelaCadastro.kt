package com.br.unifor.for_library.feature.auth.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.br.unifor.for_library.feature.auth.emailInstitucionalValido
import com.br.unifor.for_library.feature.auth.inserirUsuarioPublico
import com.br.unifor.for_library.feature.auth.obterTipoPorEmail
import com.br.unifor.for_library.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

private val AzulPrimario = Color(0xFF1565C0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCadastro(
    onVoltar: () -> Unit = {},
    onCadastrarSucesso: () -> Unit = {},
    onIrParaLogin: () -> Unit = {}
) {
    var nomeCompleto by rememberSaveable { mutableStateOf("") }
    var matricula by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var confirmarSenha by rememberSaveable { mutableStateOf("") }
    var senhaVisivel by rememberSaveable { mutableStateOf(false) }
    var confirmarSenhaVisivel by rememberSaveable { mutableStateOf(false) }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var isSuccess by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            snackbarHostState.showSnackbar("Conta criada com sucesso!")
            onCadastrarSucesso()
            isSuccess = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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

            CampoTexto(
                label = "Nome Completo",
                valor = nomeCompleto,
                placeholder = "Ex: Joao Silva",
                onValorChange = { nomeCompleto = it; errorMessage = null }
            )

            Spacer(modifier = Modifier.height(14.dp))

            CampoTexto(
                label = "Matricula",
                valor = matricula,
                placeholder = "Digite seu numero de matricula",
                onValorChange = { matricula = it; errorMessage = null },
                tipoTeclado = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(14.dp))

            CampoTexto(
                label = "Email Institucional",
                valor = email,
                placeholder = "nome@edu.unifor.br",
                onValorChange = { email = it; errorMessage = null },
                tipoTeclado = KeyboardType.Email,
                isError = errorMessage != null
            )

            Text(
                text = "Utilize um email institucional",
                fontSize = 14.sp,
                color = Color(0xFF757575),
                modifier = Modifier.padding(top = 6.dp, bottom = 24.dp)
            )

            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it; errorMessage = null },
                label = { Text("Senha") },
                placeholder = { Text("********") },
                visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                        Icon(
                            imageVector = if (senhaVisivel) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Visibilidade senha",
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

            OutlinedTextField(
                value = confirmarSenha,
                onValueChange = { confirmarSenha = it; errorMessage = null },
                label = { Text("Confirmar Senha") },
                placeholder = { Text("********") },
                visualTransformation = if (confirmarSenhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                isError = errorMessage != null,
                trailingIcon = {
                    IconButton(onClick = { confirmarSenhaVisivel = !confirmarSenhaVisivel }) {
                        Icon(
                            imageVector = if (confirmarSenhaVisivel) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Visibilidade confirmar senha",
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

            if (errorMessage != null) {
                MensagemErro(errorMessage ?: "")
            }

            Spacer(modifier = Modifier.height(20.dp))

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
                        text = "Ao se cadastrar, voce concorda com nossos termos de uso e politica de privacidade de dados academicos.",
                        fontSize = 12.sp,
                        color = Color(0xFF1565C0),
                        lineHeight = 17.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (
                        nomeCompleto.isBlank() || matricula.isBlank() || email.isBlank() ||
                        senha.isBlank() || confirmarSenha.isBlank()
                    ) {
                        errorMessage = "Preencha todos os campos"
                        return@Button
                    }
                    if (!emailInstitucionalValido(email)) {
                        errorMessage = "Utilize um email institucional valido"
                        return@Button
                    }
                    if (senha != confirmarSenha) {
                        errorMessage = "As senhas nao coincidem"
                        return@Button
                    }
                    scope.launch {
                        isLoading = true
                        errorMessage = null
                        try {
                            val tipoIdentificado = obterTipoPorEmail(email)
                            val response = supabase.auth.signUpWith(Email) {
                                this.email = email.trim()
                                this.password = senha
                                data = buildJsonObject {
                                    put("nome", nomeCompleto.trim())
                                    put("matricula", matricula.trim())
                                    put("tipo", tipoIdentificado)
                                }
                            }
                            
                            val userId = response?.id 
                                ?: supabase.auth.currentUserOrNull()?.id
                                ?: throw IllegalStateException("Não foi possível obter o ID do usuário após o cadastro")

                            inserirUsuarioPublico(
                                authUserId = userId,
                                nome = nomeCompleto,
                                matricula = matricula,
                                email = email,
                                tipo = tipoIdentificado
                            )
                            isSuccess = true
                        } catch (e: Exception) {
                            errorMessage = e.message ?: "Erro ao realizar cadastro"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onIrParaLogin() },
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Ja possui uma conta? ")
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


package com.br.unifor.for_library.feature.aluno.presentation.configuracoes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaConfiguracoes(
    onVoltar: () -> Unit,
    onSairClick: () -> Unit,
    viewModel: ConfigViewModel = viewModel(factory = ConfigViewModelFactory(LocalContext.current))
) {
    val temaEscuro by viewModel.temaEscuro.collectAsState(initial = false)
    val notificacoesPush by viewModel.notificacoesPush.collectAsState(initial = true)

    var termosExpandido by remember { mutableStateOf(false) }
    var privacidadeExpandido by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Configurações",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color(0xFF212121)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color(0xFF212121)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // RF24.2: Seção GERAL
            item { SecaoHeader(titulo = "GERAL") }
            
            item {
                ItemSwitch(
                    texto = "Notificações Push",
                    checked = notificacoesPush,
                    onCheckedChange = { viewModel.toggleNotificacoesPush(it) }
                )
            }
            
            item {
                HorizontalDivider(
                    color = Color(0xFFEEEEEE),
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            
            item {
                ItemSwitch(
                    texto = "Tema Escuro",
                    checked = temaEscuro,
                    onCheckedChange = { viewModel.toggleTemaEscuro(it) }
                )
            }

            // RF24.3: Seção SOBRE O APP
            item { SecaoHeader(titulo = "SOBRE O APP") }

            item {
                ItemExpansivel(
                    texto = "Termos de Uso",
                    expandido = termosExpandido,
                    onClick = { termosExpandido = !termosExpandido },
                    textoOculto = "Ao utilizar o ForLibrary, você concorda com nossos termos de serviço. O aplicativo visa facilitar o acesso ao acervo digital da biblioteca unifor, permitindo empréstimos e leituras digitais."
                )
            }
            
            item {
                HorizontalDivider(
                    color = Color(0xFFEEEEEE),
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            
            item {
                ItemExpansivel(
                    texto = "Política de Privacidade",
                    expandido = privacidadeExpandido,
                    onClick = { privacidadeExpandido = !privacidadeExpandido },
                    textoOculto = "Sua privacidade é importante. Coletamos apenas dados necessários para a gestão do apliclativo. Seus dados nunca são compartilhados com terceiros."
                )
            }

            // RF24.4: Seção CONTA
            item { SecaoHeader(titulo = "CONTA") }

            item {
                ItemSair(
                    texto = "Sair da Conta",
                    onClick = onSairClick
                )
            }
        }
    }
}

@Composable
private fun SecaoHeader(titulo: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = titulo,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF757575),
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun ItemSwitch(
    texto: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = texto, fontSize = 14.sp, color = Color(0xFF424242), fontWeight = FontWeight.Medium)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF1565C0),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFBDBDBD),
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
private fun ItemExpansivel(
    texto: String,
    expandido: Boolean,
    onClick: () -> Unit,
    textoOculto: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = texto, fontSize = 14.sp, color = Color(0xFF424242), fontWeight = FontWeight.Medium)
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFFBDBDBD)
            )
        }
        AnimatedVisibility(visible = expandido) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFAFAFA))
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text(
                    text = textoOculto,
                    fontSize = 13.sp,
                    color = Color(0xFF757575),
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
private fun ItemSair(texto: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = "Sair",
            tint = Color(0xFFD32F2F),
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = texto,
            fontSize = 14.sp,
            color = Color(0xFFD32F2F),
            fontWeight = FontWeight.Bold
        )
    }
}

// Factory para o ViewModel com Contexto
class ConfigViewModelFactory(private val context: Context) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return ConfigViewModel(context) as T
    }
}

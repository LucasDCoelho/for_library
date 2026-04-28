package com.br.unifor.for_library.feature.aluno.perfil.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TelaConfiguracoes(
    onVoltar: () -> Unit,
    onSairClick: () -> Unit
) {
    var notificacoesPush by remember { mutableStateOf(true) }
    var temaEscuro by remember { mutableStateOf(false) }

    var termosExpandido by remember { mutableStateOf(false) }
    var privacidadeExpandido by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()        // ✅ Fix 1: movido para a Column raiz
            .navigationBarsPadding()    // ✅ Fix 4: protege o botão Sair da gesture bar
    ) {
        // ── RF24.1: Cabeçalho ────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp), // statusBarsPadding removido daqui
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVoltar) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Configurações",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        // ── RF24.2: Seção GERAL ──────────────────────────────────────────────
        SecaoCabecalho(titulo = "GERAL")

        ItemSwitch(
            texto = "Notificações Push",
            checked = notificacoesPush,
            onCheckedChange = { notificacoesPush = it }
        )
        HorizontalDivider(
            color = Color(0xFFEEEEEE),
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        ItemSwitch(
            texto = "Tema Escuro",
            checked = temaEscuro,
            onCheckedChange = { temaEscuro = it }
        )

        // ── RF24.3: Seção SOBRE O APP ────────────────────────────────────────
        SecaoCabecalho(titulo = "SOBRE O APP")

        ItemExpansivel(
            texto = "Termos de Uso",
            expandido = termosExpandido,
            onClick = { termosExpandido = !termosExpandido },
            textoOculto = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                    "Aqui ficarão os Termos de Uso oficiais do aplicativo.\n\n" +
                    "// TODO: Preencher com os Termos de Uso futuramente."
        )
        HorizontalDivider(
            color = Color(0xFFEEEEEE),
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        ItemExpansivel(
            texto = "Política de Privacidade",
            expandido = privacidadeExpandido,
            onClick = { privacidadeExpandido = !privacidadeExpandido },
            textoOculto = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                    "Aqui ficará a Política de Privacidade oficial do aplicativo.\n\n" +
                    "// TODO: Preencher com a Política de Privacidade futuramente."
        )

        // ── RF24.4: Seção CONTA ──────────────────────────────────────────────
        SecaoCabecalho(titulo = "CONTA")

        ItemSair(texto = "Sair", onClick = onSairClick)
    }
}

// ── Componentes Privados ────────────────────────────────────────────────────

@Composable
private fun SecaoCabecalho(titulo: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = titulo,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
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
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = texto, fontSize = 14.sp, color = Color.Black)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF1565C0),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.Gray
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
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = texto, fontSize = 14.sp, color = Color.Black)
            // ✅ Fix 2: ícone fixo conforme protótipo — não muda ao expandir
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
        AnimatedVisibility(visible = expandido) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFAFAFA))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = textoOculto,
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    lineHeight = 18.sp
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
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = "Sair",
            tint = Color(0xFFD32F2F)
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

@Preview(showBackground = true)
@Composable
fun TelaConfiguracoesPreview() {
    TelaConfiguracoes(
        onVoltar = {},
        onSairClick = {}
    )
}
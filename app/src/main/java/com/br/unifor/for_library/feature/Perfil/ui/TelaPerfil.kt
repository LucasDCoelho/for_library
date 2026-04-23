package com.br.unifor.for_library.feature.perfil.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TelaPerfil(
    // ── Callbacks de clique prontos para uso ──
    onEditarPerfilClick: () -> Unit,
    onEnvioObraClick: () -> Unit,
    onDuvidasClick: () -> Unit,
    onConfiguracoesClick: () -> Unit,
    onSairClick: () -> Unit
) {
    val azulPrimario = Color(0xFF1E88E5)
    val cinzaTexto = Color(0xFF757575)
    val corSair = Color(0xFFD32F2F) // Vermelho para o botão sair
    val corDivisoria = Color(0xFFEEEEEE)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // ── Cabeçalho Simples ──
        Text(
            text = "Perfil do Aluno",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121),
            modifier = Modifier.padding(16.dp)
        )

        HorizontalDivider(color = corDivisoria)

        // ── Informações do Usuário (Foto, Nome, Matrícula) ──
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto de Perfil Default
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF212121)), // Fundo escuro igual imagem
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto de perfil padrão",
                    tint = Color.LightGray,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Ricardo Ferreira",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )

            Text(
                text = "2613903-9",
                fontSize = 12.sp,
                color = cinzaTexto
            )
        }

        HorizontalDivider(color = corDivisoria)

        // ── Estatísticas (Livros, Resenhas, Pontos) ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EstatisticaItem(valor = "24", titulo = "LIVROS LIDOS", azulPrimario, Modifier.weight(1f))
            EstatisticaDivisoria()
            EstatisticaItem(valor = "18", titulo = "RESENHAS APROVADAS", azulPrimario, Modifier.weight(1f))
            EstatisticaDivisoria()
            EstatisticaItem(valor = "1.250", titulo = "PONTOS", azulPrimario, Modifier.weight(1f))
        }

        HorizontalDivider(color = corDivisoria, thickness = 4.dp) // Divisão um pouco mais grossa

        // ── Menu de Ações (5 Botões) ──
        Column(modifier = Modifier.fillMaxWidth()) {
            ItemMenuPerfil(
                icone = Icons.Default.Edit,
                texto = "Editar Perfil",
                onClick = onEditarPerfilClick
            )
            ItemMenuPerfil(
                icone = Icons.Default.CloudUpload,
                texto = "Envio de Obra",
                onClick = onEnvioObraClick
            )
            ItemMenuPerfil(
                icone = Icons.Default.HelpOutline,
                texto = "Dúvidas (FAQ)",
                onClick = onDuvidasClick
            )
            ItemMenuPerfil(
                icone = Icons.Default.Settings,
                texto = "Configurações",
                onClick = onConfiguracoesClick
            )

            // Botão Sair (Vermelho)
            ItemMenuPerfil(
                icone = Icons.AutoMirrored.Filled.ExitToApp,
                texto = "Sair",
                corPersonalizada = corSair,
                mostrarSeta = false,
                onClick = onSairClick
            )
        }
    }
}

// ── Componentes Reutilizáveis Internos ──

@Composable
private fun EstatisticaItem(valor: String, titulo: String, corValor: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = valor, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = corValor)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = titulo, fontSize = 9.sp, color = Color(0xFF757575), letterSpacing = 0.5.sp)
    }
}

@Composable
private fun EstatisticaDivisoria() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(40.dp)
            .background(Color(0xFFEEEEEE))
    )
}

@Composable
private fun ItemMenuPerfil(
    icone: ImageVector,
    texto: String,
    corPersonalizada: Color = Color(0xFF424242),
    mostrarSeta: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icone,
            contentDescription = null,
            tint = corPersonalizada,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = texto,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = corPersonalizada,
            modifier = Modifier.weight(1f)
        )
        if (mostrarSeta) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
    HorizontalDivider(color = Color(0xFFEEEEEE))
}
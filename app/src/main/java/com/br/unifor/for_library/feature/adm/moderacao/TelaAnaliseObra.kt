package com.br.unifor.for_library.feature.adm.moderacao

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ObraDetalhe(
    val id: String,
    val titulo: String,
    val genero: String,
    val autor: String,
    val sinopse: String,
    val urlCapa: String = "",
    val urlPdf: String = ""
)

private val mockObrasDetalhe = mapOf(
    "1" to ObraDetalhe(
        id = "1",
        titulo = "A Jornada Digital",
        genero = "Tecnologia",
        autor = "Ricardo Lima",
        sinopse = "Uma exploração profunda sobre como as tecnologias emergentes estão moldando o comportamento humano e as estruturas sociais no século XXI. Um guia essencial para entender o amanhã."
    ),
    "2" to ObraDetalhe(
        id = "2",
        titulo = "O Eco das Sombras",
        genero = "Suspense",
        autor = "Beatriz Soares",
        sinopse = "Em uma cidade onde os segredos nunca dormem, uma investigadora descobre que o passado pode ser mais perigoso do que qualquer ameaça presente."
    ),
    "3" to ObraDetalhe(
        id = "3",
        titulo = "Raízes do Amanhã",
        genero = "Ficção Científica",
        autor = "Marcos Vinicius",
        sinopse = "Uma saga épica sobre humanidade, sobrevivência e esperança em um planeta à beira do colapso. A natureza encontra a tecnologia em uma narrativa inesquecível."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAnaliseObra(
    obraId: String,
    onVoltar: () -> Unit = {},
    onAprovar: () -> Unit = {},
    onRejeitar: (String) -> Unit = {}
) {
    val obra = mockObrasDetalhe[obraId] ?: mockObrasDetalhe["1"]!!
    var motivoRejeicao by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analisar Obra", fontWeight = FontWeight.SemiBold, fontSize = 18.sp) },
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {

            // ── Capa do livro ──────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF2C2C2C)),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder escuro estilo capa de livro
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📖", fontSize = 40.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = obra.titulo,
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Título ─────────────────────────────────────────────────────────
            Label("TÍTULO")
            Text(
                text = obra.titulo,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )

            Spacer(Modifier.height(10.dp))

            // ── Gênero ───────────────────────────────���─────────────────────────
            Label("GÊNERO")
            Text(obra.genero, fontSize = 14.sp, color = Color(0xFF424242))

            Spacer(Modifier.height(10.dp))

            // ── Autor ──────────────────────────────────────────────────────────
            Label("AUTOR")
            Text(obra.autor, fontSize = 14.sp, color = Color(0xFF424242))

            Spacer(Modifier.height(14.dp))

            HorizontalDivider(color = Color(0xFFE0E0E0))

            Spacer(Modifier.height(14.dp))

            // ── Sinopse ────────────────────────────────────────────────────────
            Label("SINOPSE")
            Text(
                text = obra.sinopse,
                fontSize = 13.sp,
                color = Color(0xFF616161),
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(16.dp))

            // ── Botão Baixar PDF ──────────���────────────────────────────────────
            OutlinedButton(
                onClick = { /* TODO: abrir PDF */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1565C0)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1565C0))
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Baixar PDF para Análise", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(24.dp))

            // ── Motivo da Rejeição ──────────────────────────────���──────────────
            Text(
                text = "MOTIVO DA REJEIÇÃO (OPCIONAL)",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF9E9E9E),
                letterSpacing = 0.5.sp
            )
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = motivoRejeicao,
                onValueChange = { motivoRejeicao = it },
                placeholder = {
                    Text(
                        "Descreva o motivo caso a obra não cumpra as diretrizes...",
                        fontSize = 13.sp,
                        color = Color(0xFFBDBDBD)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = Color(0xFF1565C0)
                )
            )

            Spacer(Modifier.height(14.dp))

            // ── Aviso Diretrizes ───────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8F4FD), RoundedCornerShape(8.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFF1565C0),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = buildAnnotatedString {
                        append("Certifique-se de que a obra está de acordo com as ")
                        withStyle(
                            SpanStyle(
                                color = Color(0xFF1565C0),
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("Diretrizes Editoriais")
                        }
                        append(" antes de prosseguir com a aprovação.")
                    },
                    fontSize = 12.sp,
                    color = Color(0xFF424242),
                    lineHeight = 18.sp
                )
            }

            Spacer(Modifier.height(20.dp))

            // ── Botão Aprovar ──────────────────────────────────────────────────
            Button(
                onClick = onAprovar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Aprovar Obra", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(10.dp))

            // ── Botão Rejeitar ─────────────────────────────────────────────────
            OutlinedButton(
                onClick = { onRejeitar(motivoRejeicao) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFD32F2F))
            ) {
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Rejeitar Obra", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun Label(text: String) {
    Text(
        text = text,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF9E9E9E),
        letterSpacing = 0.5.sp
    )
    Spacer(Modifier.height(2.dp))
}




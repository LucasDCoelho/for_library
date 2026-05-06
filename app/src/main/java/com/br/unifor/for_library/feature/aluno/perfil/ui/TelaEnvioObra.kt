package com.br.unifor.for_library.feature.aluno.perfil.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AzulPrimario = Color(0xFF1565C0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaEnvioObra(
    onVoltar: () -> Unit = {},
    onObraEnviada: () -> Unit = {}
) {
    // ✅ rememberSaveable: campos do formulário sobrevivem à rotação de tela
    var titulo by rememberSaveable { mutableStateOf("") }
    var generoSelecionado by rememberSaveable { mutableStateOf("") }
    var sinopse by rememberSaveable { mutableStateOf("") }
    var arquivoAnexado by rememberSaveable { mutableStateOf(false) }
    // ✅ remember (UI-only): não precisa sobreviver à rotação
    var enviado by remember { mutableStateOf(false) }
    var expandirGenero by remember { mutableStateOf(false) }

    val generos = listOf("Romance", "Ficção Científica", "Suspense", "Tecnologia",
        "Autoajuda", "Poesia", "Biografia", "História")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Envio de Obra", fontWeight = FontWeight.SemiBold, fontSize = 17.sp) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF5F7FA)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Banner informativo
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = null,
                        tint = AzulPrimario,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Submeta sua obra autoral para avaliação e inclusão no acervo digital.",
                        fontSize = 13.sp,
                        color = Color(0xFF1A237E),
                        lineHeight = 18.sp
                    )
                }
            }

            // Campos do formulário
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                // Título da Obra
                Text("Título da Obra", fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF424242), modifier = Modifier.padding(bottom = 6.dp))
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    placeholder = { Text("Ex: A Jornada dos Algoritmos", color = Color(0xFFBDBDBD)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulPrimario,
                        unfocusedBorderColor = Color(0xFFBDBDBD),
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                Spacer(Modifier.height(16.dp))

                // Gênero (dropdown)
                Text("Gênero", fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF424242), modifier = Modifier.padding(bottom = 6.dp))
                ExposedDropdownMenuBox(
                    expanded = expandirGenero,
                    onExpandedChange = { expandirGenero = it }
                ) {
                    OutlinedTextField(
                        value = if (generoSelecionado.isEmpty()) "" else generoSelecionado,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Selecione um gênero", color = Color(0xFFBDBDBD)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandirGenero) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AzulPrimario,
                            unfocusedBorderColor = Color(0xFFBDBDBD),
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expandirGenero,
                        onDismissRequest = { expandirGenero = false }
                    ) {
                        generos.forEach { genero ->
                            DropdownMenuItem(
                                text = { Text(genero) },
                                onClick = {
                                    generoSelecionado = genero
                                    expandirGenero = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Sinopse
                Text("Sinopse Curta", fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF424242), modifier = Modifier.padding(bottom = 6.dp))
                OutlinedTextField(
                    value = sinopse,
                    onValueChange = { sinopse = it },
                    placeholder = { Text("Breve descrição sobre o conteúdo da sua obra...", color = Color(0xFFBDBDBD)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    shape = RoundedCornerShape(10.dp),
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulPrimario,
                        unfocusedBorderColor = Color(0xFFBDBDBD),
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                Spacer(Modifier.height(20.dp))

                // Botão Anexar PDF
                OutlinedButton(
                    onClick = { arquivoAnexado = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AzulPrimario)
                ) {
                    Icon(Icons.Default.AttachFile, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Anexar PDF", fontWeight = FontWeight.SemiBold)
                }

                // Arquivo anexado
                AnimatedVisibility(visible = arquivoAnexado) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = null,
                            tint = Color(0xFF757575), modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("monografia_v1.pdf", fontSize = 13.sp,
                            color = Color(0xFF424242), modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = { arquivoAnexado = false },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Remover",
                                tint = Color(0xFFEF5350), modifier = Modifier.size(18.dp))
                        }
                    }
                }

                Spacer(Modifier.height(28.dp))

                // Botão Enviar Obra
                Button(
                    onClick = {
                        enviado = true
                        onObraEnviada()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario),
                    enabled = titulo.isNotBlank() && generoSelecionado.isNotEmpty() && arquivoAnexado
                ) {
                    Text("Enviar Obra", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                }

                // Banner de sucesso
                AnimatedVisibility(visible = enviado) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2E7D32))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null,
                                tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("OBRA ENVIADA PARA ANÁLISE", fontSize = 12.sp,
                                fontWeight = FontWeight.Bold, color = Color.White,
                                letterSpacing = 0.5.sp)
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}


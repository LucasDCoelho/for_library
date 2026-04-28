package com.br.unifor.for_library.feature.adm.acervo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.br.unifor.for_library.core.designsystem.AzulPrimario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAdicionarObra(
    onVoltar: () -> Unit
) {
    // Estados do formulário
    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var anoLancamento by remember { mutableStateOf("") }
    var paginas by remember { mutableStateOf("") }
    var sinopse by remember { mutableStateOf("") }

    // Estado do Dropdown de Gênero
    var generoSelecionado by remember { mutableStateOf("Selecione um gênero") }
    var menuExpandido by remember { mutableStateOf(false) }
    val listaGeneros = listOf("Homem", "Mulher", "Não binário")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Obra", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // ── Área de Upload da Capa ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color(0xFFF9F9F9), RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                    .clickable { /* Abrir galeria */ },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Fazer upload da Capa (JPG/PNG)", color = Color.Gray, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Campos de Texto ──
            FormInput(label = "Título", placeholder = "Ex: O Alquimista", value = titulo, onValueChange = { titulo = it })
            FormInput(label = "Autor", placeholder = "Nome do autor", value = autor, onValueChange = { autor = it })

            // ── Menu de Gênero (Dropdown) ──
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Gênero", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(6.dp))
                ExposedDropdownMenuBox(
                    expanded = menuExpandido,
                    onExpandedChange = { menuExpandido = it }
                ) {
                    OutlinedTextField(
                        value = generoSelecionado,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpandido) },
                        shape = RoundedCornerShape(4.dp),
                        colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color(0xFFE0E0E0))
                    )
                    ExposedDropdownMenu(
                        expanded = menuExpandido,
                        onDismissRequest = { menuExpandido = false }
                    ) {
                        listaGeneros.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    generoSelecionado = item
                                    menuExpandido = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Ano e Páginas (Lado a Lado) ──
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    FormInput(label = "Ano de Lançamento", placeholder = "YYYY", value = anoLancamento, onValueChange = { if (it.length <= 4) anoLancamento = it })
                }
                Box(modifier = Modifier.weight(1f)) {
                    FormInput(label = "Páginas", placeholder = "000", value = paginas, onValueChange = { paginas = it })
                }
            }

            // ── Sinopse ──
            FormInput(
                label = "Sinopse",
                placeholder = "Breve descrição da obra...",
                value = sinopse,
                onValueChange = { sinopse = it },
                height = 120.dp,
                singleLine = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Botão Anexar Arquivo ──
            OutlinedButton(
                onClick = { /* Selecionar PDF/EPUB */ },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, AzulPrimario)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Description, contentDescription = null, tint = AzulPrimario)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Anexar Arquivo do Livro (PDF/ePub)", color = AzulPrimario)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Botões Finais ──
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedButton(
                    onClick = onVoltar,
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("Cancelar", color = Color.Black)
                }
                Button(
                    onClick = { /* Lógica de Salvar */ },
                    modifier = Modifier.weight(1f).height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("Salvar Obra")
                }
            }
        }
    }
}

@Composable
fun FormInput(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    height: androidx.compose.ui.unit.Dp = 56.dp,
    singleLine: Boolean = true
) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = 14.sp, color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth().height(height),
            singleLine = singleLine,
            shape = RoundedCornerShape(4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = AzulPrimario
            )
        )
    }
}

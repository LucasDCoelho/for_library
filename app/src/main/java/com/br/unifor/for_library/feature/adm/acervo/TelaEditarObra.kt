package com.br.unifor.for_library.feature.adm.acervo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.br.unifor.for_library.core.components.CapaLivro
import com.br.unifor.for_library.core.designsystem.AzulPrimario
import com.br.unifor.for_library.core.designsystem.CinzaTexto

private data class LivroEdicaoMock(
    val id: String,
    val titulo: String,
    val autor: String,
    val genero: String,
    val ano: String,
    val paginas: String,
    val sinopse: String,
    val isbn: String,
    val arquivoNome: String,
    val arquivoTamanho: String,
    val capaAtualizadaHa: String
)

private val mockLivroEdicao = LivroEdicaoMock(
    id = "1",
    titulo = "Design System Essentials",
    autor = "IBM Design Team",
    genero = "Tecnologia",
    ano = "2024",
    paginas = "256",
    sinopse = "A short text about the foundations of visual language in complex systems, covering scale, color theory, and modularity in digital products.",
    isbn = "9780132350884",
    arquivoNome = "essentials_v1.pdf",
    arquivoTamanho = "4.2 MB",
    capaAtualizadaHa = "2 dias"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaEditarObra(
    livroId: String,
    onVoltar: () -> Unit,
    onAtualizar: () -> Unit,
    onCancelar: () -> Unit
) {
    val livro = mockLivroEdicao

    var titulo by remember { mutableStateOf(livro.titulo) }
    var autor by remember { mutableStateOf(livro.autor) }
    var anoLancamento by remember { mutableStateOf(livro.ano) }
    var paginas by remember { mutableStateOf(livro.paginas) }
    var sinopse by remember { mutableStateOf(livro.sinopse) }

    var generoSelecionado by remember { mutableStateOf(livro.genero) }
    var menuExpandido by remember { mutableStateOf(false) }
    val listaGeneros = listOf("Ficção", "Acadêmico", "Tecnologia", "Biografia", "História", "Design")

    var arquivoAnexado by remember { mutableStateOf<Pair<String, String>?>(livro.arquivoNome to livro.arquivoTamanho) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Obra", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // ── Capa do Livro (RF29.4) ─────────────────────────────────────────
            Text("Capa do Livro", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CapaLivro(
                        isbn = livro.isbn,
                        tituloFallback = livro.titulo,
                        modifier = Modifier
                            .width(60.dp)
                            .height(84.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        corFallback = AzulPrimario
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            livro.titulo,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = Color(0xFF212121)
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            "Atualizada há ${livro.capaAtualizadaHa}",
                            fontSize = 11.sp,
                            color = CinzaTexto
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = { /* Abrir gerenciador de arquivos */ },
                            modifier = Modifier.height(32.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario),
                            shape = RoundedCornerShape(4.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp)
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Trocar Capa", color = Color.White, fontSize = 11.sp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Campos do formulário (RF29.3) ─────────────────────────────────
            FormInputEdicao(label = "Título", value = titulo, onValueChange = { titulo = it })
            FormInputEdicao(label = "Autor", value = autor, onValueChange = { autor = it })

            // Gênero (Dropdown)
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
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

            // Ano e Páginas (lado a lado)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    FormInputEdicao(label = "Ano de Lançamento", value = anoLancamento, onValueChange = { if (it.length <= 4) anoLancamento = it })
                }
                Box(modifier = Modifier.weight(1f)) {
                    FormInputEdicao(label = "Páginas", value = paginas, onValueChange = { paginas = it })
                }
            }

            // Sinopse
            FormInputEdicao(
                label = "Sinopse",
                value = sinopse,
                onValueChange = { sinopse = it },
                height = 100.dp,
                singleLine = false
            )

            // ── Arquivo Digital (RF29.3) ──────────────────────────────────────
            arquivoAnexado?.let { (nome, tamanho) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFDEAEA)),
                    border = BorderStroke(1.dp, Color(0xFFF8C9C9)),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.PictureAsPdf,
                            contentDescription = null,
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(nome, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF212121))
                            Text(tamanho, fontSize = 11.sp, color = CinzaTexto)
                        }
                        IconButton(onClick = { arquivoAnexado = null }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Remover arquivo",
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Botões finais (RF29.5) ────────────────────────────────────────
            Button(
                onClick = onAtualizar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("Atualizar Obra", color = Color.White, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(10.dp))

            OutlinedButton(
                onClick = onCancelar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Icon(Icons.Default.Close, contentDescription = null, tint = Color(0xFF424242), modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text("Cancelar", color = Color(0xFF424242))
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FormInputEdicao(
    label: String,
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

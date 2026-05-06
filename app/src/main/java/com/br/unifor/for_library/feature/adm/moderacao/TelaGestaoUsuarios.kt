package com.br.unifor.for_library.feature.adm.moderacao

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.br.unifor.for_library.core.designsystem.AzulPrimario
import com.br.unifor.for_library.core.designsystem.CinzaTexto

data class UsuarioMock(
    val id: String,
    val nome: String,
    val matricula: String,
    val email: String,
    val ativo: Boolean,
    val resenhasInadequadas: Int
)

private val mockUsuarios = listOf(
    UsuarioMock("1", "João Silva",     "2023001", "joao.silva@unifor.edu.br",     true,  0),
    UsuarioMock("2", "Ana Oliveira",   "2023045", "ana.oliveira@unifor.edu.br",   false, 3),
    UsuarioMock("3", "Ricardo Mendes", "2022112", "ricardo.mendes@unifor.edu.br", true,  1),
    UsuarioMock("4", "Maria Santos",   "2023089", "maria.santos@unifor.edu.br",   true,  0),
    UsuarioMock("5", "Pedro Costa",    "2021504", "pedro.costa@unifor.edu.br",    false, 3),
    UsuarioMock("6", "Juliana Lima",   "2023156", "juliana.lima@unifor.edu.br",   true,  0),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaGestaoUsuarios(onVoltar: () -> Unit = {}) {
    // ✅ rememberSaveable: busca sobrevive à rotação de tela
    var busca by rememberSaveable { mutableStateOf("") }
    var usuarioSelecionado by remember { mutableStateOf<UsuarioMock?>(null) }

    // ✅ derivedStateOf: re-filtra apenas quando busca ou lista mudam
    val usuariosFiltrados by remember {
        derivedStateOf {
            mockUsuarios.filter {
                busca.isBlank() ||
                it.nome.contains(busca, ignoreCase = true) ||
                it.matricula.contains(busca, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Gestão de Usuários",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121)
                    )
                },
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
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = busca,
                onValueChange = { busca = it },
                placeholder = { Text("Buscar por matrícula ou nome", fontSize = 13.sp, color = Color.LightGray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(6.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = AzulPrimario
                )
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "${usuariosFiltrados.size} USUÁRIOS ENCONTRADOS",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = CinzaTexto,
                letterSpacing = 0.5.sp
            )

            Spacer(Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // ✅ key estável: evita recomposições desnecessárias ao filtrar
                items(usuariosFiltrados, key = { it.id }) { usuario ->
                    ItemUsuario(
                        usuario = usuario,
                        onClick = { usuarioSelecionado = usuario }
                    )
                }
            }
        }
    }

    // RF39: Popup acionado ao clicar em um aluno
    usuarioSelecionado?.let { usuario ->
        PopupDetalhesUsuario(
            usuario = usuario,
            onFechar = { usuarioSelecionado = null }
        )
    }
}

@Composable
private fun ItemUsuario(
    usuario: UsuarioMock,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF757575), modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(usuario.nome, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color(0xFF212121))
                Text("Matrícula: ${usuario.matricula}", fontSize = 11.sp, color = CinzaTexto)
            }

            // Status badge
            val (corFundo, corTexto, label) = if (usuario.ativo) {
                Triple(Color(0xFFE6F4EA), Color(0xFF1B873B), "ATIVO")
            } else {
                Triple(Color(0xFFFDEAEA), Color(0xFFD32F2F), "BLOQUEADO")
            }
            Box(
                modifier = Modifier
                    .background(corFundo, RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(label, color = corTexto, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

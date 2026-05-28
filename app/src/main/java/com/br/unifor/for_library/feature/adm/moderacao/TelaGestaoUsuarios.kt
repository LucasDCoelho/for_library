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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.br.unifor.for_library.core.designsystem.AzulPrimario
import com.br.unifor.for_library.core.designsystem.CinzaTexto
import com.br.unifor.for_library.core.designsystem.VermelhoErro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaGestaoUsuarios(
    onVoltar: () -> Unit = {},
    viewModel: GestaoUsuariosViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val busca by viewModel.busca.collectAsState()
    val usuariosFiltrados by viewModel.usuariosFiltrados.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var usuarioSelecionado by remember { mutableStateOf<UsuarioAdminItem?>(null) }

    LaunchedEffect(state.erro) {
        state.erro?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.consumirErro()
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White
    ) { paddingValues ->

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AzulPrimario)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // RF38.2: SearchBar
            OutlinedTextField(
                value = busca,
                onValueChange = viewModel::onBuscaChange,
                placeholder = {
                    Text(
                        "Buscar por matrícula ou nome",
                        fontSize = 13.sp,
                        color = Color.LightGray
                    )
                },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(6.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = AzulPrimario
                )
            )

            Spacer(Modifier.height(8.dp))

            // RF38.3: Contador (Gate 2: atualiza com a busca)
            Text(
                text = "${usuariosFiltrados.size} USUÁRIO${if (usuariosFiltrados.size != 1) "S" else ""} ENCONTRADO${if (usuariosFiltrados.size != 1) "S" else ""}",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = CinzaTexto,
                letterSpacing = 0.5.sp
            )

            Spacer(Modifier.height(8.dp))

            if (usuariosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (busca.isBlank()) "Nenhum usuário cadastrado."
                               else "Nenhum resultado para \"$busca\".",
                        color = Color(0xFF9E9E9E),
                        fontSize = 14.sp
                    )
                }
            } else {
                // RF38.4: Listagem
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(usuariosFiltrados, key = { it.id }) { usuario ->
                        ItemUsuario(
                            usuario = usuario,
                            onClick = { usuarioSelecionado = usuario }
                        )
                    }
                }
            }
        }
    }

    // RF38.5: Popup de detalhes (RF39)
    usuarioSelecionado?.let { usuario ->
        PopupDetalhesUsuario(
            usuario = usuario,
            onFechar = { usuarioSelecionado = null },
            onAlterarStatus = { bloqueado ->
                viewModel.alterarStatus(usuario.id, bloqueado)
                // Reflete mudança imediata no item selecionado sem fechar o popup
                usuarioSelecionado = usuario.copy(bloqueado = bloqueado)
            }
        )
    }
}

@Composable
private fun ItemUsuario(
    usuario: UsuarioAdminItem,
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
            // RF38.4: Avatar (foto_perfil ou placeholder)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE3EEF9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = AzulPrimario,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    usuario.nome,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = Color(0xFF212121)
                )
                Text(
                    "Matrícula: ${usuario.matricula}",
                    fontSize = 11.sp,
                    color = CinzaTexto
                )
            }

            // Gate 1: status bloqueado em vermelho, ativo em verde
            val (corFundo, corTexto, label) = if (!usuario.bloqueado) {
                Triple(Color(0xFFE6F4EA), Color(0xFF1B873B), "ATIVO")
            } else {
                Triple(Color(0xFFFDEAEA), VermelhoErro, "BLOQUEADO")
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

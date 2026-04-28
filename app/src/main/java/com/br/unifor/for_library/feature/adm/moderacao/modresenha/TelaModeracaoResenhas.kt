package com.br.unifor.for_library.feature.adm.moderacao.modresenha


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Reviews
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.br.unifor.for_library.core.designsystem.AzulPrimario

data class ResenhaMock(
    val id: String, // Adicionado ID para navegação
    val nome: String,
    val livro: String,
    val estrelas: Int,
    val texto: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaModeracaoResenhas(
    onVoltar: () -> Unit,
    onResenhaClick: (String) -> Unit // NOVO: Ação de clique repassando o ID
) {
    val resenhasPendentes = listOf(
        ResenhaMock("1", "Ana Silva", "The Great Gatsby", 5, "A resenha explora profundamente a decadência do sonho americano..."),
        ResenhaMock("2", "Bruno Oliveira", "1984 - George Orwell", 4, "Impactante e ainda muito atual. A forma como o Big Brother..."),
        ResenhaMock("3", "Lucas Oliveira Santos", "Estrutura de Dados I", 5, "A disciplina de Estrutura de Dados I foi transformadora...")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Moderação de Resenhas", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            // ... (Cabeçalho "STATUS" continua igual ao código anterior) ...

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(resenhasPendentes) { resenha ->
                    // 👇 Passando o clique para o Card
                    ItemResenhaPendente(resenha = resenha, onClick = { onResenhaClick(resenha.id) })
                }
            }
        }
    }
}

@Composable
fun ItemResenhaPendente(resenha: ResenhaMock, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7)),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() } // 👇 CARD AGORA É CLICÁVEL!
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = resenha.nome, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Box(modifier = Modifier.background(Color(0xFFD0E4FF), RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                    Text("PENDING", color = Color(0xFF0056B3), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            Text(text = resenha.livro, fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(6.dp))
            Row {
                repeat(5) { index ->
                    Icon(Icons.Default.Star, contentDescription = null, tint = if (index < resenha.estrelas) AzulPrimario else Color.LightGray, modifier = Modifier.size(14.dp))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = resenha.texto, fontSize = 13.sp, color = Color.DarkGray, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}
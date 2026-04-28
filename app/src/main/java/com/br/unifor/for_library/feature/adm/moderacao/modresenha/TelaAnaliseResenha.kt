package com.br.unifor.for_library.feature.adm.moderacao.modresenha


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
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
fun TelaAnaliseResenha(
    resenhaId: String,
    onVoltar: () -> Unit,
    onAprovar: () -> Unit,
    onRejeitar: (motivo: String) -> Unit
) {
    var motivoRejeicao by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analisar Resenha", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
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
                .padding(16.dp)
        ) {
            // ── Card da Resenha ──
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7)),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Usuário
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(40.dp).background(Color.LightGray, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Lucas Oliveira Santos", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Estrelas
                    Row {
                        repeat(5) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = AzulPrimario, modifier = Modifier.size(18.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("TEXTO DA RESENHA", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "A disciplina de Estrutura de Dados I foi transformadora. O professor utiliza uma metodologia prática que facilita muito a compreensão de ponteiros e alocação dinâmica. O material de apoio é denso, porém muito bem organizado. Recomendo fortemente para quem deseja uma base sólida em computação. Só senti falta de mais exemplos em Python, já que o foco foi 100% em C.",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Enviado em 14 de Outubro, 2023 às 14:32", fontSize = 11.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Campo de Rejeição ──
            Text("Motivo da Rejeição", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = motivoRejeicao,
                onValueChange = { motivoRejeicao = it },
                placeholder = { Text("Opcional para aprovação, obrigatório para rejeição", fontSize = 13.sp) },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF7F7F7),
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Card de Aviso ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8F0FE), RoundedCornerShape(4.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(Icons.Default.Info, contentDescription = null, tint = AzulPrimario, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Certifique-se de que a resenha segue as diretrizes da comunidade antes de tomar uma decisão final.",
                    fontSize = 13.sp,
                    color = AzulPrimario
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // ── Botões Finais ──
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { onRejeitar(motivoRejeicao) },
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC3545)), // Vermelho
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Rejeitar Resenha", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onAprovar,
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF28A745)), // Verde
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Aprovar Resenha", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
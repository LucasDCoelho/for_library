package com.br.unifor.for_library.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.br.unifor.for_library.core.designsystem.AzulPrimario

enum class OrdemFiltro(val label: String) {
    MAIS_RECENTES("Mais Recentes"),
    MELHOR_AVALIADOS("Melhor Avaliados"),
    A_Z("A-Z"),
    Z_A("Z-A")
}

data class FiltroAvancadoState(
    val generosSelecionados: Set<String> = emptySet(),
    val ordem: OrdemFiltro = OrdemFiltro.MAIS_RECENTES
)

private val generosDisponiveis = listOf(
    "Ficção", "Acadêmico", "Tecnologia",
    "Biografia", "História", "Design"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltroAvancadoBottomSheet(
    visivel: Boolean,
    estadoInicial: FiltroAvancadoState = FiltroAvancadoState(),
    onDismiss: () -> Unit,
    onAplicar: (FiltroAvancadoState) -> Unit
) {
    if (!visivel) return

    var generosSelecionados by remember(estadoInicial) {
        mutableStateOf(estadoInicial.generosSelecionados)
    }
    var ordem by remember(estadoInicial) {
        mutableStateOf(estadoInicial.ordem)
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            // ── Header ────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filtros Avançados",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE0E0E0))

            // ── Gênero Literário ──────────────────────────────────────────
            Text(
                text = "GÊNERO LITERÁRIO",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF9E9E9E),
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            generosDisponiveis.chunked(3).forEach { linha ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    linha.forEach { genero ->
                        val selecionado = genero in generosSelecionados
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (selecionado) AzulPrimario else Color.White)
                                .border(
                                    width = 1.dp,
                                    color = if (selecionado) AzulPrimario else Color(0xFFBDBDBD),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .clickable {
                                    generosSelecionados = if (selecionado)
                                        generosSelecionados - genero
                                    else
                                        generosSelecionados + genero
                                }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                if (selecionado) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Text(
                                    text = genero,
                                    fontSize = 13.sp,
                                    color = if (selecionado) Color.White else Color.DarkGray,
                                    fontWeight = if (selecionado) FontWeight.Medium else FontWeight.Normal
                                )
                            }
                        }
                    }
                    repeat(3 - linha.size) { Spacer(modifier = Modifier.weight(1f)) }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(color = Color(0xFFE0E0E0))
            Spacer(modifier = Modifier.height(12.dp))

            // ── Ordenar Por ───────────────────────────────────────────────
            Text(
                text = "ORDENAR POR",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF9E9E9E),
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            OrdemFiltro.entries.forEach { opcao ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { ordem = opcao }
                        .padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = ordem == opcao,
                        onClick = { ordem = opcao },
                        colors = RadioButtonDefaults.colors(selectedColor = AzulPrimario)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = opcao.label,
                        fontSize = 14.sp,
                        fontWeight = if (ordem == opcao) FontWeight.Bold else FontWeight.Normal,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFE0E0E0))
            Spacer(modifier = Modifier.height(16.dp))

            // ── Botões ────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        generosSelecionados = emptySet()
                        ordem = OrdemFiltro.MAIS_RECENTES
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AzulPrimario),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AzulPrimario)
                ) {
                    Text("Limpar", fontWeight = FontWeight.Medium)
                }
                Button(
                    onClick = {
                        onAplicar(FiltroAvancadoState(generosSelecionados, ordem))
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario)
                ) {
                    Text("Aplicar Filtros", fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

package com.br.unifor.for_library.feature.adm.moderacao

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ItemModeracao(
    val icone: ImageVector,
    val titulo: String,
    val onClick: () -> Unit
)

@Composable
fun TelaListaModeracaoAdmin(
    onModeracaoUsuarios: () -> Unit = {},
    onModeracaoObras: () -> Unit = {},
    onModeracaoResenhas: () -> Unit = {}
) {
    val itens = listOf(
        ItemModeracao(
            icone = Icons.Default.ManageAccounts,
            titulo = "Moderação de Usuarios",
            onClick = onModeracaoUsuarios
        ),
        ItemModeracao(
            icone = Icons.Default.MenuBook,
            titulo = "Moderação de Obras",
            onClick = onModeracaoObras
        ),
        ItemModeracao(
            icone = Icons.Default.RateReview,
            titulo = "Moderação de Resenha",
            onClick = onModeracaoResenhas
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(20.dp))

        Text(
            text = "Lista de Moderação",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121)
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Moderação",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF616161)
        )

        Spacer(Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column {
                itens.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { item.onClick() }
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = item.icone,
                            contentDescription = item.titulo,
                            tint = Color(0xFF616161),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(Modifier.width(14.dp))
                        Text(
                            text = item.titulo,
                            fontSize = 14.sp,
                            color = Color(0xFF212121),
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Color(0xFFBDBDBD),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    if (index < itens.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Color(0xFFE0E0E0),
                            thickness = 0.5.dp
                        )
                    }
                }
            }
        }
    }
}



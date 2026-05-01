package com.br.unifor.for_library.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

private val itensAdmin = listOf(
    ItemNav(Rota.DashboardAdmin,  "Dashboard",  Icons.Filled.Dashboard,    Icons.Outlined.Dashboard),
    ItemNav(Rota.AcervoAdmin,     "Acervo",     Icons.Filled.LibraryBooks, Icons.Outlined.LibraryBooks),
    ItemNav(Rota.GestaoUsuarios,  "Moderação",  Icons.Filled.Gavel,        Icons.Outlined.Gavel),
    ItemNav(Rota.Eventos,         "Eventos",    Icons.Filled.Event,        Icons.Outlined.Event),
)

@Composable
fun AdminBottomBar(navController: NavController) {
    val backStack by navController.currentBackStackEntryAsState()
    val rotaAtual = backStack?.destination?.route

    val rotasComBarAdmin = listOf(
        Rota.DashboardAdmin.path,
        Rota.AcervoAdmin.path,
        Rota.GestaoUsuarios.path,
        Rota.EditarObra.path
    )
    if (rotaAtual !in rotasComBarAdmin) return

    NavigationBar(containerColor = androidx.compose.ui.graphics.Color.White) {
        itensAdmin.forEach { item ->
            val selecionado = rotaAtual == item.rota.path
            NavigationBarItem(
                selected = selecionado,
                onClick = {
                    if (!selecionado) {
                        navController.navigate(item.rota.path) {
                            popUpTo(Rota.DashboardAdmin.path) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selecionado) item.iconeSelecionado else item.iconeNormal,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label, fontSize = androidx.compose.ui.unit.TextUnit(10f, androidx.compose.ui.unit.TextUnitType.Sp)) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = androidx.compose.ui.graphics.Color(0xFF1565C0),
                    selectedTextColor = androidx.compose.ui.graphics.Color(0xFF1565C0),
                    unselectedIconColor = androidx.compose.ui.graphics.Color(0xFF9E9E9E),
                    unselectedTextColor = androidx.compose.ui.graphics.Color(0xFF9E9E9E),
                    indicatorColor = androidx.compose.ui.graphics.Color(0xFFE3EEF9)
                )
            )
        }
    }
}

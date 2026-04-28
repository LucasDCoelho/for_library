package com.br.unifor.for_library.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlin.collections.forEach
import kotlin.collections.listOf

data class ItemNav(
    val rota: Rota,
    val label: String,
    val iconeSelecionado: androidx.compose.ui.graphics.vector.ImageVector,
    val iconeNormal: androidx.compose.ui.graphics.vector.ImageVector
)

private val itens = listOf(
    ItemNav(Rota.HomeAluno, "Home",    Icons.Filled.Home,         Icons.Outlined.Home),
    ItemNav(Rota.Acervo,   "Acervo",  Icons.Filled.LibraryBooks,  Icons.Outlined.LibraryBooks),
    ItemNav(Rota.Estante,  "Estante", Icons.Filled.Bookmarks,     Icons.Outlined.Bookmarks),
    ItemNav(Rota.Eventos,  "Eventos", Icons.Filled.Event,          Icons.Outlined.Event),
    ItemNav(Rota.Perfil,   "Perfil",  Icons.Filled.Person,         Icons.Outlined.Person),
)

@Composable
fun ForLibraryBottomBar(navController: NavController) {
    val backStack by navController.currentBackStackEntryAsState()
    val rotaAtual = backStack?.destination?.route

    val rotasSemBottomBar = listOf(
        Rota.Splash.path,
        Rota.Login.path,
        Rota.Cadastro.path,
        Rota.RecuperarSenha.path,
        Rota.Notificacoes.path,
        Rota.EditarPerfil.path,
        Rota.Duvida.path,
        // rotas de adm
        Rota.AdicionarLivro.path,
        Rota.ModeracaoResenhas.path,
        Rota.AnaliseResenha.path
    )

    // Só exibe a bottom bar nas telas principais (não no Login/Cadastro)
    val rotasComBar = listOf(
        Rota.HomeAluno.path, Rota.Acervo.path,
        Rota.Estante.path, Rota.Eventos.path, Rota.Perfil.path
    )
    if (rotaAtual !in rotasComBar) return

    NavigationBar(containerColor = androidx.compose.ui.graphics.Color.White) {
        itens.forEach { item ->
            val selecionado = rotaAtual == item.rota.path
            NavigationBarItem(
                selected = selecionado,
                onClick = {
                    if (!selecionado) {
                        navController.navigate(item.rota.path) {
                            popUpTo(Rota.HomeAluno.path) { saveState = true }
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
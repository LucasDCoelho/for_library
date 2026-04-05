package com.br.unifor.for_library.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlin.collections.forEach
import kotlin.collections.listOf

// Modelo para os itens do menu
data class BottomNavItem(val nome: String, val rota: String, val icone: ImageVector)

val itensAluno = listOf(
    BottomNavItem("Home", Rota.HomeAluno.path, Icons.Default.Home),
    BottomNavItem("Acervo", Rota.Acervo.path, Icons.Default.Search),
    BottomNavItem("Estante", Rota.Estante.path, Icons.Default.Book),
    BottomNavItem("Eventos", Rota.Eventos.path, Icons.Default.Event),
    BottomNavItem("Perfil", Rota.Perfil.path, Icons.Default.Person)
)

@Composable
fun ForLibraryBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rotaAtual = navBackStackEntry?.destination?.route

    // Define em quais telas a BottomBar NÃO deve aparecer
    val rotasSemBottomBar = listOf(Rota.Splash.path, Rota.Login.path, Rota.Cadastro.path)

    if (rotaAtual !in rotasSemBottomBar) {
        NavigationBar {
            itensAluno.forEach { item ->
                val isSelected = navBackStackEntry?.destination?.hierarchy?.any { it.route == item.rota } == true

                NavigationBarItem(
                    icon = { Icon(item.icone, contentDescription = item.nome) },
                    label = { Text(item.nome) },
                    selected = isSelected,
                    onClick = {
                        navController.navigate(item.rota) {
                            // Evita criar uma pilha infinita de telas ao clicar nos botões
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}
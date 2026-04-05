package com.br.unifor.for_library;

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.br.unifor.for_library.core.navigation.ForLibraryBottomBar
import com.br.unifor.for_library.core.navigation.Rota
import com.br.unifor.for_library.feature.auth.ui.TelaLoginPlaceholder

@Composable
fun ForLibraryApp() {
    // Esse é o controlador mestre. Ele só é instanciado UMA vez aqui.
    val navController = rememberNavController()

    // O Scaffold gerencia o layout da tela, incluindo a BottomBar
    Scaffold(
        bottomBar = { ForLibraryBottomBar(navController = navController) }
    ) { paddingValues ->

        // O NavHost é onde as rotas são ligadas às telas
        NavHost(
            navController = navController,
            startDestination = Rota.Login.path,
            modifier = Modifier.padding(paddingValues)
        ) {

            // --- ÁREA DE AUTENTICAÇÃO ---
            composable(Rota.Login.path) {
                TelaLoginPlaceholder(
                    onLoginSucesso = {
                        navController.navigate(Rota.HomeAluno.path) {
                            // Limpa o histórico para o usuário não voltar pro Login apertando o botão "Voltar" do celular
                            popUpTo(Rota.Login.path) { inclusive = true }
                        }
                    },
                    onIrParaCadastro = { navController.navigate(Rota.Cadastro.path) }
                )
            }


        }
    }
}

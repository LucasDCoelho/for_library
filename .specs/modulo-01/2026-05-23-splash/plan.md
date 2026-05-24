# Plano de Implementação (plan.md) - RF01: Tela de Splash

## 1. Visão Geral da Tarefa
**Requisito:** RF01 - Tela de Splash (Splash Screen)
**Objetivo:** Criar a tela de abertura do aplicativo ForLibrary, garantindo a exibição da logomarca por exatamente 3 segundos enquanto verifica, em segundo plano, o estado de autenticação do usuário no Supabase para direcioná-lo à rota correta.

## 2. Arquitetura e Ficheiros Afetados
Esta feature atravessa as camadas de Apresentação (UI) e Lógica de Navegação/Estado (ViewModel):
* **UI:** `feature/auth/ui/TelaSplash.kt`
* **ViewModel:** `feature/auth/viewmodel/SplashViewModel.kt`
* **Navegação:** `core/navigation/Rotas.kt` e `ForLibraryApp.kt`

## 3. Passo a Passo de Implementação (Tasks)

### Passo 1: Construção da Lógica de Estado (ViewModel)
1. Criar a classe `SplashViewModel` com injeção de dependência do `SupabaseClient`.
2. Criar um fluxo de eventos (ex: `SharedFlow`) para emitir ações de navegação: `NavigateToLogin`, `NavigateToHomeAluno` e `NavigateToHomeAdmin`.
3. Implementar a rotina de inicialização (`init` block) rodando em uma corrotina (`viewModelScope.launch`).
4. Adicionar um bloqueio assíncrono de tempo (`delay(3000L)`) para satisfazer o RF01.2.
5. Implementar a consulta de sessão (`auth.currentUserOrNull()`).
6. Criar a ramificação condicional (RF01.3):
    - Se nulo -> Emitir `NavigateToLogin`.
    - Se não nulo -> Ler o `user_metadata` (campo "tipo"). Se for "admin", emitir `NavigateToHomeAdmin`; caso contrário, emitir `NavigateToHomeAluno`.

### Passo 2: Construção da Interface (Jetpack Compose)
1. Criar a função Composable `TelaSplash`.
2. Configurar o layout base utilizando uma `Box` com `fillMaxSize` e alinhamento central.
3. Aplicar a cor de fundo padrão (`FundoTela` do Design System).
4. Inserir a imagem da logomarca (`ic_forlibrary_logo`) com tamanho fixo e descrição de acessibilidade (RF01.1).
5. Observar o fluxo de eventos do ViewModel através de um `LaunchedEffect` para disparar os *callbacks* de navegação (`onNavigate`).

### Passo 3: Configuração do Roteamento
1. Adicionar o objeto `Splash` na *sealed class* de rotas (`Rotas.kt`).
2. No `NavHost` (`ForLibraryApp.kt`), definir a rota da Splash como `startDestination`.
3. Configurar os blocos de navegação para que, ao sair da Splash, ela seja removida da pilha de retorno (`popUpTo("splash_route") { inclusive = true }`), impedindo que o usuário volte para ela ao pressionar o botão de voltar do Android.
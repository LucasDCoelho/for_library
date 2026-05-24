# Plano de Implementação (plan.md) - RF03: Tela de Recuperação de Senha

## 1. Visão Geral da Tarefa
**Requisito:** RF03 - Tela de Recuperação de Senha
**Objetivo:** Refatorar a interface para corrigir as inconsistências textuais apontadas no campo de e-mail (RF03.3) e implementar a integração real com o SDK do Supabase para o envio do e-mail transacional de redefinição de senha, gerindo corretamente o feedback visual (Snackbar) e o redirecionamento do utilizador (RF03.5).

## 2. Arquitetura e Ficheiros Afetados
* **UI:** `feature/auth/ui/RecuperarSenha.kt`
* **ViewModel:** `feature/auth/viewmodel/RecuperarSenhaViewModel.kt` (Criação/Atualização)
* **Navegação:** `core/navigation/Rotas.kt` (Ajustes de popBackStack)

## 3. Passo a Passo de Implementação (Tasks)

### Passo 1: Construção da Lógica de Recuperação (ViewModel)
1. Criar/Atualizar a classe `RecuperarSenhaViewModel` com injeção do `SupabaseClient`.
2. Definir os estados da UI: `email` (String), `isLoading` (Boolean), `isSuccess` (Boolean), `errorMessage` (String?).
3. Implementar a função `enviarLinkRecuperacao()`:
    - Validar se o e-mail não está vazio e possui domínio válido (`@unifor.br` ou `@edu.unifor.br`).
    - Ativar `isLoading = true`.
    - Iniciar bloco `try/catch`.
    - Chamar a função nativa: `supabaseClient.auth.resetPasswordForEmail(email)`.
    - Em caso de sucesso, definir `isSuccess = true`.
    - Em caso de exceção, capturar e atualizar `errorMessage`.
    - Desativar `isLoading`.

### Passo 2: Refatoração da Interface (Jetpack Compose)
1. Abrir o ficheiro `RecuperarSenha.kt`.
2. **Correção RF03.3:** Localizar o `OutlinedTextField` do e-mail. Modificar a propriedade `label = { Text("Email institucional") }` (texto acima) e a propriedade `placeholder = { Text("Email") }` (texto interno), corrigindo a anotação "❌ FALTA ARRUMAR TEXTOS".
3. **Feedback Visual (RF03.5):** Adicionar um `SnackbarHost` ao `Scaffold` da tela.
4. Utilizar um `LaunchedEffect` observando o estado `isSuccess` do ViewModel. Quando for `true`:
    - Disparar o Snackbar com a mensagem: *"Link enviado com sucesso!"*.
    - Após a exibição (ou imediatamente após disparar), invocar a função de *callback* de navegação para retornar à Tela de Login.
5. Adicionar verificação de erros (`errorMessage`): Se existir erro, exibir abaixo do campo de texto com a cor `MaterialTheme.colorScheme.error`.
6. Conectar o estado `isLoading` ao botão "Enviar Link" (RF03.4) para evitar múltiplos cliques.

### Passo 3: Integração de Navegação
1. Garantir que o clique na "Seta de voltar" no canto superior esquerdo (RF03.1) aciona o `navController.popBackStack()`, devolvendo o utilizador ao Login sem criar uma nova instância da tela.
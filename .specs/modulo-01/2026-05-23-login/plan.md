# Plano de Implementação (plan.md) - RF02: Tela de Login

## 1. Visão Geral da Tarefa
**Requisito:** RF02 - Tela de Login
**Objetivo:** Refatorar a tela de login existente para corrigir os textos descritivos apontados pela equipe (RF02.3 e RF02.4), implementar o tratamento visual de erro (RF02.8) e integrar a autenticação real com o Supabase (conforme `auth_supabase.md`), resolvendo a dúvida sobre o redirecionamento de Admin/Aluno.

## 2. Arquitetura e Ficheiros Afetados
* **UI:** `feature/auth/ui/TelaLoginPlaceholder.kt` (ou `TelaLogin.kt`)
* **ViewModel:** `feature/auth/viewmodel/LoginViewModel.kt` (Criação/Atualização)
* **Navegação:** `core/navigation/Rotas.kt` e `ForLibraryApp.kt` (Ajuste de rotas de destino)

## 3. Passo a Passo de Implementação (Tasks)

### Passo 1: Construção da Lógica de Autenticação (ViewModel)
1. Criar a classe `LoginViewModel` recebendo o `SupabaseClient` por injeção de dependência.
2. Definir os estados da UI usando `StateFlow` ou `mutableStateOf`:
    - `email` e `senha` (Strings)
    - `isLoading` (Boolean)
    - `errorMessage` (String nula, para exibir o erro do RF02.8)
3. Implementar a função `realizarLogin()`:
    - Validar se o e-mail inserido termina em `@unifor.br` ou `@edu.unifor.br`. Se não, atualizar `errorMessage` para "Utilize um e-mail institucional válido".
    - Iniciar bloco `try/catch` ativando o `isLoading`.
    - Chamar `supabaseClient.auth.loginWith(Email) { email = ...; password = ... }`.
    - Se sucesso: ler o `user_metadata` do usuário. Se `tipo == "admin"`, emitir evento de navegação para `HomeAdmin`; senão, `HomeAluno`.
    - Se falha (catch): atualizar `errorMessage` para "Credenciais inválidas" (cumprindo RF02.8).

### Passo 2: Refatoração da Interface (Jetpack Compose)
1. Abrir o ficheiro da tela de Login.
2. **Correção RF02.3:** No `OutlinedTextField` do E-mail, adicionar a propriedade `label = { Text("Matrícula ou e-mail institucional") }` e `placeholder = { Text("Matrícula ou Email") }`.
3. **Correção RF02.4:** No `OutlinedTextField` da Senha, adicionar a propriedade `label = { Text("Senha") }`. Manter o ícone de visualização (olho) e a propriedade `visualTransformation = PasswordVisualTransformation()`.
4. **Correção RF02.8:** Logo abaixo do campo de senha (ou acima do botão "Entrar"), adicionar uma condição: `if (errorMessage != null) { Text(text = errorMessage, color = MaterialTheme.colorScheme.error) }`.
5. Modificar o botão "Entrar" para observar o estado `isLoading` (desabilitando-o ou mostrando um `CircularProgressIndicator` enquanto carrega).

### Passo 3: Integração de Navegação
1. Garantir que os botões "Esqueceu a senha?" e "Cadastre-se" disparam corretamente os callbacks para suas respectivas rotas (RF02.6 e RF02.7).
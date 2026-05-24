# Plano de Implementação (plan.md) - RF04: Tela de Cadastro de Aluno

## 1. Visão Geral da Tarefa
**Requisito:** RF04 - Tela de Cadastro de Aluno
**Objetivo:** Refatorar a lógica e a interface de registo para aplicar as validações de e-mail institucional exigidas (corrigindo a anotação "❓ NÃO SEGUE LÓGICA" do RF04.4), verificar a coincidência de palavras-passe (RF04.7) e integrar o processo com o Supabase, enviando os dados extra (Nome e Matrícula) através do `user_metadata`.

## 2. Arquitetura e Ficheiros Afetados
* **UI:** `feature/auth/ui/TelaCadastro.kt` (ou similar)
* **ViewModel:** `feature/auth/viewmodel/CadastroViewModel.kt` (Criação/Atualização)
* **Navegação:** Roteamento de retorno para `Rotas.Login`

## 3. Passo a Passo de Implementação (Tasks)

### Passo 1: Construção da Lógica de Registo (ViewModel)
1. Criar a classe `CadastroViewModel` com injeção do `SupabaseClient`.
2. Definir os estados observáveis: `nome`, `matricula`, `email`, `senha`, `confirmarSenha`, `isLoading`, `isSuccess` e `errorMessage`.
3. Implementar a função `cadastrarUsuario()`:
    - **Validação Local 1 (Campos Vazios):** Verificar se algum campo está em branco.
    - **Validação Local 2 (RF04.4):** Verificar se o e-mail termina em `@unifor.br` ou `@edu.unifor.br`. Se não, atualizar `errorMessage = "Utilize um email institucional válido"`.
    - **Validação Local 3 (RF04.7):** Verificar se `senha == confirmarSenha`. Se não, atualizar `errorMessage = "As senhas não coincidem"`.
    - Ativar `isLoading = true` e iniciar `try/catch`.
    - Chamar `supabaseClient.auth.signUpWith(Email)`. No bloco de configuração, popular o `user_metadata` do Supabase com as chaves `"nome"`, `"matricula"` e `"tipo"` (inferindo o tipo consoante o domínio do e-mail: `@unifor.br` -> admin, `@edu.unifor.br` -> aluno).
    - Em caso de sucesso, definir `isSuccess = true`. Em caso de erro (ex: e-mail já em uso), repassar a mensagem do Supabase para o `errorMessage`.
    - Desativar `isLoading`.

### Passo 2: Refatoração da Interface (Jetpack Compose)
1. Inspecionar e ajustar os componentes textuais superiores (RF04.2): Título "Crie sua conta" e subtítulo explicativo.
2. Garantir a existência de todos os 5 campos (`OutlinedTextField`) especificados no RF04.3.
3. Para a "Senha" e "Confirmar Senha", assegurar a presença do `PasswordVisualTransformation()` e do `IconButton` com o ícone de olho para alternar a visibilidade (RF04.5).
4. Inserir a componente visual (Checkbox ou simples Text) com o aviso dos Termos de Uso e Política de Privacidade (RF04.6).
5. Adicionar a componente de exibição do erro (`errorMessage`) em vermelho, posicionada acima do botão principal.
6. Ligar o botão "Cadastrar" (RF04.8) à função `cadastrarUsuario()` do ViewModel, alterando o seu conteúdo para um `CircularProgressIndicator` quando `isLoading == true`.
7. Utilizar um `LaunchedEffect(isSuccess)` para:
    - Disparar um Toast/Snackbar: *"Conta criada com sucesso!"*
    - Invocar o *callback* de navegação de volta para o Login.

### Passo 3: Integração de Navegação
1. Ligar a "Seta de voltar" no topo (RF04.1) e a *label* inferior "Já possui uma conta? Fazer login" (RF04.9) a uma função de *callback* que acione o `navController.popBackStack()`.
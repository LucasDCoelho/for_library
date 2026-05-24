# Plano de Validação (validation.md) - RF01: Tela de Splash

## 1. Visão Geral da Validação
**Requisito:** RF01 - Tela de Splash
**Objetivo:** Garantir que a tela de splash cumpre rigorosamente as restrições visuais, temporais e lógicas antes de liberar o acesso do usuário ao ecossistema do ForLibrary.

## 2. Critérios de Aceite
* [ ] A logomarca deve estar perfeitamente centralizada e o fundo deve respeitar o Design System (RF01.1).
* [ ] O ecrã não deve transitar antes de exatos 3 segundos (RF01.2).
* [ ] O sistema deve encaminhar corretamente para o fluxo de Login ou Home com base na persistência da sessão do Supabase (RF01.3).
* [ ] A tela de Splash não deve existir na pilha de retorno (*backstack*) após a transição.

## 3. Cenários de Teste (Testes Manuais e de Integração)

### CT01: Verificação Visual e Temporal
* **Pré-condição:** Aplicativo recém-instalado (sem sessão).
* **Ação:** Abrir o aplicativo.
* **Resultado Esperado:** A tela exibe a logo centralizada. Utilizando um cronômetro, a transição para a tela de Login deve ocorrer apenas após o término da contagem de 3 segundos.

### CT02: Roteamento Sem Sessão (Usuário Deslogado)
* **Pré-condição:** Nenhuma sessão ativa no Supabase (limpar dados do app, se necessário).
* **Ação:** Iniciar o aplicativo e aguardar 3 segundos.
* **Resultado Esperado:** O aplicativo direciona o usuário para a `TelaLoginPlaceholder`.

### CT03: Roteamento Com Sessão de Aluno
* **Pré-condição:** Aplicativo com sessão ativa cujo `user_metadata` possui `tipo = "aluno"`.
* **Ação:** Fechar o aplicativo (matar o processo) e abri-lo novamente.
* **Resultado Esperado:** Após 3 segundos, o aplicativo pula a tela de login e direciona diretamente para a `TelaHomeAluno`.

### CT04: Roteamento Com Sessão de Administrador
* **Pré-condição:** Aplicativo com sessão ativa cujo `user_metadata` possui `tipo = "admin"`.
* **Ação:** Fechar o aplicativo (matar o processo) e abri-lo novamente.
* **Resultado Esperado:** Após 3 segundos, o aplicativo pula a tela de login e direciona diretamente para a `TelaHomeAdmin`.

### CT05: Verificação de Backstack (Pilha de Navegação)
* **Pré-condição:** O usuário transitou da Splash para qualquer outra tela (Login ou Home).
* **Ação:** Pressionar o botão físico/gesto de "Voltar" do Android.
* **Resultado Esperado:** O aplicativo deve ser minimizado/fechado. O usuário **não** deve ver a tela de Splash novamente.
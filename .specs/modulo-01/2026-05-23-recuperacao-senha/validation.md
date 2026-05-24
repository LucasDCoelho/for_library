# Plano de Validação (validation.md) - RF03: Tela de Recuperação de Senha

## 1. Visão Geral da Validação
**Requisito:** RF03 - Tela de Recuperação de Senha
**Objetivo:** Garantir que o campo de e-mail possui os textos instrucionais exatos, que o Supabase dispara efetivamente o pedido de reset e que o fluxo devolve o utilizador à tela de Login acompanhado de uma confirmação visual (Snackbar).

## 2. Critérios de Aceite
* [ ] O campo de e-mail apresenta a label "Email institucional" e o placeholder "Email" (RF03.3).
* [ ] O botão superior de voltar navega corretamente para a tela de Login (RF03.1).
* [ ] O sistema comunica com a API do Supabase com sucesso.
* [ ] É exibido um Snackbar com "Link enviado com sucesso!" antes ou durante a transição de volta para o Login (RF03.5).

## 3. Cenários de Teste (Testes Manuais e de Integração)

### CT01: Verificação Visual e Textual (Correção)
* **Pré-condição:** Estar na Tela de Recuperação de Senha.
* **Ação:** Inspecionar o campo de entrada de texto e a seta de voltar.
* **Resultado Esperado:** O título da tela é "Recuperar Senha". O campo exibe "Email institucional" acima da linha de preenchimento e "Email" dentro dela. A seta de voltar leva à tela de Login.

### CT02: Submissão com E-mail Inválido
* **Pré-condição:** Estar na Tela de Recuperação.
* **Ação:** Inserir um texto sem formato de e-mail ou com domínio não institucional (ex: `joao@gmail.com`) e clicar em "Enviar Link".
* **Resultado Esperado:** O sistema bloqueia a submissão, não exibe o carregamento prolongado e mostra uma mensagem de erro indicando e-mail inválido. O utilizador permanece na tela.

### CT03: Fluxo Completo de Sucesso
* **Pré-condição:** Estar na Tela de Recuperação com conexão ativa.
* **Ação:** Inserir um e-mail institucional válido (ex: `teste@edu.unifor.br`) e clicar em "Enviar Link".
* **Resultado Esperado:**
    1. O botão exibe estado de *loading* ou fica desabilitado.
    2. O Supabase processa o pedido.
    3. Surge um Snackbar/Toast com a mensagem "Link enviado com sucesso!".
    4. O aplicativo volta automaticamente para a Tela de Login.
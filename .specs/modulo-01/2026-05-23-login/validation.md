# Plano de Validação (validation.md) - RF02: Tela de Login

## 1. Visão Geral da Validação
**Requisito:** RF02 - Tela de Login
**Objetivo:** Garantir que a UI possui os rótulos (labels) corretos, que mensagens de erro são exibidas em vermelho em caso de falha, e que a autenticação no Supabase bloqueia domínios inválidos e roteia corretamente alunos e administradores.

## 2. Critérios de Aceite
* [ ] O campo de e-mail exibe o label "Matrícula ou e-mail institucional" (RF02.3).
* [ ] O campo de senha exibe o label "Senha" acima do input e oculta os caracteres (RF02.4).
* [ ] A mensagem "Credenciais inválidas" aparece em vermelho em caso de erro (RF02.8).
* [ ] Apenas e-mails com terminação `@unifor.br` ou `@edu.unifor.br` são aceitos.
* [ ] O sistema roteia corretamente para Home Aluno ou Home Admin dependendo do metadata retornado pelo Supabase.

## 3. Cenários de Teste (Testes Manuais e de Integração)

### CT01: Verificação Visual dos Campos (Correções UI)
* **Pré-condição:** Estar na Tela de Login.
* **Ação:** Observar os campos de texto antes de digitar.
* **Resultado Esperado:** O primeiro campo possui o label "Matrícula ou e-mail institucional". O segundo campo possui o label "Senha". O ícone de olho está presente no campo de senha.

### CT02: Validação de Domínio de E-mail
* **Pré-condição:** Usuário deslogado na Tela de Login.
* **Ação:** Inserir `aluno@gmail.com` e uma senha qualquer. Clicar em "Entrar".
* **Resultado Esperado:** O sistema não realiza a requisição ao Supabase e exibe um erro informando para utilizar um e-mail institucional.

### CT03: Credenciais Inválidas (Erro Vermelho)
* **Pré-condição:** Usuário deslogado.
* **Ação:** Inserir um e-mail institucional válido (`teste@edu.unifor.br`), mas uma senha errada. Clicar em "Entrar".
* **Resultado Esperado:** O aplicativo exibe indicador de carregamento, faz a requisição, recebe a recusa e exibe a mensagem "Credenciais inválidas" em vermelho (RF02.8).

### CT04: Autenticação de Aluno (Sucesso)
* **Pré-condição:** Uma conta de aluno válida cadastrada no Supabase (`tipo = "aluno"`).
* **Ação:** Inserir as credenciais corretas e clicar em "Entrar".
* **Resultado Esperado:** O carregamento é concluído e o usuário é navegado para a `TelaHomeAluno`.

### CT05: Autenticação de Administrador (Sucesso)
* **Pré-condição:** Uma conta de administrador válida no Supabase (`tipo = "admin"` e domínio `@unifor.br`).
* **Ação:** Inserir as credenciais corretas e clicar em "Entrar".
* **Resultado Esperado:** O carregamento é concluído e o usuário é navegado para a `TelaHomeAdmin`.
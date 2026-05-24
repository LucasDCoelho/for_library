# Plano de Validação (validation.md) - RF04: Tela de Cadastro de Aluno

## 1. Visão Geral da Validação
**Requisito:** RF04 - Tela de Cadastro de Aluno
**Objetivo:** Garantir que as lógicas de barreira da aplicação (senhas idênticas e e-mail institucional) funcionam antes da submissão à API externa, e que o processo de registo no Supabase regista corretamente os metadados do aluno.

## 2. Critérios de Aceite
* [ ] Seta de voltar e hiperligação inferior regressam ao ecrã de Login sem duplicar instâncias na memória (RF04.1 e RF04.9).
* [ ] Inserir um e-mail que não acabe em `@edu.unifor.br` ou `@unifor.br` impede o registo e mostra erro (RF04.4).
* [ ] Inserir senhas diferentes impede o registo e mostra erro (RF04.7).
* [ ] Após um registo bem-sucedido, o utilizador é redirecionado de volta para a tela de Login (RF04.8).

## 3. Cenários de Teste (Testes Manuais e de Integração)

### CT01: Validação de E-mail Não Institucional
* **Pré-condição:** Estar no ecrã de Registo.
* **Ação:** Preencher todos os campos corretamente, exceto o e-mail, utilizando `teste@gmail.com`. Clicar em "Cadastrar".
* **Resultado Esperado:** Nenhuma chamada é feita à base de dados. Uma mensagem vermelha exibe "Utilize um email institucional válido".

### CT02: Validação de Senhas Distintas
* **Pré-condição:** Estar no ecrã de Registo.
* **Ação:** Preencher o e-mail institucional corretamente, mas inserir `senha123` no campo Senha e `senha321` no campo Confirmar Senha. Clicar em "Cadastrar".
* **Resultado Esperado:** A interface exibe a mensagem de erro "As senhas não coincidem".

### CT03: Cadastro Completo com Sucesso
* **Pré-condição:** Ecrã de Registo sem preenchimento prévio. Conexão ativa com a Internet. E-mail utilizado não existe na base de dados.
* **Ação:** Inserir dados válidos. E-mail: `novo.aluno@edu.unifor.br`. Senhas iguais. Clicar em "Cadastrar".
* **Resultado Esperado:**
    1. O botão entra em estado de carregamento.
    2. O Supabase processa a requisição.
    3. O utilizador é notificado com "Conta criada com sucesso!" (Toast/Snackbar).
    4. O sistema transita automaticamente para a Tela de Login.
       *(A equipa técnica pode validar, em back-office, se a linha foi criada no Supabase contendo o `user_metadata` correto com Nome e Matrícula).*

### CT04: Cadastro com E-mail Já Existente
* **Pré-condição:** A conta `teste@edu.unifor.br` já foi criada no CT03.
* **Ação:** Tentar realizar novamente o registo utilizando os mesmos dados (mesmo e-mail). Clicar em "Cadastrar".
* **Resultado Esperado:** O Supabase rejeita a requisição, e o ecrã exibe a mensagem de erro formatada: "Email já em uso".
# ForLibrary - Integração de Autenticação (auth_supabase.md)

Este documento especifica a arquitetura e o fluxo de autenticação do aplicativo **ForLibrary** utilizando o serviço de identidade do **Supabase (GoTrue)**.

Ao adotar o Supabase, eliminamos a necessidade de rotas customizadas de validação e delegamos o gerenciamento de credenciais e segurança diretamente para o BaaS (Backend as a Service). Na nossa **Clean Architecture**, o SDK do Supabase atuará na camada de **Data (Data Source)**, implementando as interfaces definidas pelo nosso **Domain**.

**Documentação Relacionada:** Veja `supabase_schema.md` para conhecer o schema completo das tabelas do banco de dados Supabase (PostgreSQL).

---

## 1. Configuração do SDK (Pilha Tecnológica)

A integração será feita utilizando o SDK oficial da comunidade para Kotlin/Android.

* **Dependência Principal:** `io.github.jan-tennert.supabase:gotrue-kt` (Módulo de Auth)
* **Armazenamento de Sessão:** O SDK pode ser configurado para persistir tokens automaticamente utilizando o `EncryptedSharedPreferences` nativo do Android, satisfazendo o nosso requisito de segurança (RNF01) e sessão em background (RF01.3).

---

## 2. Fluxos de Autenticação

O Supabase utiliza a abordagem padrão de E-mail e Senha. Para garantir o acesso exclusivo à comunidade acadêmica da universidade, aplicaremos uma regra de negócio estrita na validação do domínio durante a etapa de cadastro.

### 2.1 Cadastro de Usuário (Sign Up)
**Referência:** Tela de Cadastro (RF04)
* **Regra de Validação de E-mail (Institucional):** Antes de invocar a API, o aplicativo **DEVE** validar se o e-mail digitado termina obrigatoriamente com `@unifor.br` (perfil padrão/administrador) ou `@edu.unifor.br` (perfil de aluno). Caso contrário, a UI deve barrar o envio e exibir um erro exigindo o uso do e-mail institucional.
* **Ação:** O repositório invoca `supabase.gotrue.signUpWith(Email)`.
* **Payload:** E-mail institucional válido e senha fornecidos pelo usuário.
* **Metadados:** O Supabase permite passar dados adicionais (como Nome e Matrícula) dentro do objeto `user_metadata` no momento do cadastro. Isso evita chamadas secundárias de inserção no banco no primeiro acesso.
* **Fluxo:** Se bem-sucedido, o Supabase cria a linha na tabela interna `auth.users` e dispara um *Trigger* no banco de dados para popular a nossa tabela pública `usuarios` (`mapeamento_entidades.md`).

### 2.2 Login (Sign In)
**Referência:** Tela de Login (RF02)
* **Ação:** O repositório invoca `supabase.gotrue.loginWith(Email)`.
* **Payload:** E-mail institucional (`@unifor.br` ou `@edu.unifor.br`) e senha.
* **Retorno:** O Supabase retorna a sessão ativa e armazena automaticamente o `access_token` e o `refresh_token` no dispositivo.
* **Tratamento de Erros:** Exceções como `RestException` (credenciais inválidas) serão capturadas e repassadas ao `LoginViewModel` para exibir o texto de erro em vermelho na UI (RF02.8).

### 2.3 Recuperação de Senha (Reset Password)
**Referência:** Tela de Recuperação (RF03)
* **Ação:** O repositório invoca `supabase.gotrue.resetPasswordForEmail()`.
* **Fluxo:** O Supabase envia um e-mail transacional (personalizável no painel do Supabase) com o link de recuperação de senha (Deep Link) que redirecionará de volta para o aplicativo.

### 2.4 Verificação de Sessão (Splash Screen)
**Referência:** Splash Screen (RF01.3)
* **Ação:** Consulta síncrona/assíncrona ao estado atual via `supabase.gotrue.sessionStatus`.
* **Fluxo:** O `SplashViewModel` observa o status. Se for `Authenticated`, roteia para a `HomeAluno` ou `HomeAdmin` baseado no *role* do usuário salvo no banco. Se for `NotAuthenticated`, roteia para a `TelaLoginPlaceholder`.

---

## 3. Segurança e Sincronização de Banco de Dados

* **Row Level Security (RLS):** Como o Supabase fornece um banco PostgreSQL atrelado à autenticação, todas as tabelas (como `progresso_leitura`, `favoritos`, `resenhas`) terão RLS habilitado. O acesso direto do Android ao banco só retornará os dados onde `auth.uid() = usuario_id`, blindando o sistema contra vazamento de dados sem precisar de código extra no backend.
* **Sincronização:** Após o login, o aplicativo fará uma consulta na tabela `usuarios` para resgatar os `pontos_gamificacao` e o status de `tipo` (Aluno vs Admin), preenchendo as Entidades do Domínio para uso interno da interface.
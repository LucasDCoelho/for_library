# ForLibrary - Documentação de Especificações

Bem-vindo à pasta de especificações (`/.specs`) do projeto **ForLibrary**. Aqui estão documentados todos os requisitos, arquitetura, design system, entidades e integrações do aplicativo.

---

## 📋 Índice de Documentos

### Backend & Infraestrutura

- **`supabase_schema.md`** ⭐ **NOVO**
  - Schema PostgreSQL completo do banco de dados Supabase
  - 11 tabelas principais: `usuarios`, `livros`, `favoritos`, `progresso_leitura`, `resenhas`, `eventos`, `notificacoes`, `obras_autorais`, `historico_pontos`, `atividades_admin`, `configuracoes_sistema`
  - Relacionamentos, constraints e políticas de Row Level Security (RLS)
  - Índices recomendados para otimização

- **`auth_supabase.md`**
  - Integração de autenticação com Supabase (GoTrue)
  - Fluxos de Sign Up, Sign In, Password Reset
  - Configuração do SDK Kotlin/Android
  - Segurança e sincronização com o banco

- **`supabase_schema.md`**
  - Documentação detalhada de todas as tabelas
  - Tipos de dados, constraints, relacionamentos
  - Exemplos de uso e sincronização

### Análise e Requisitos

- **`requirements.md`**
  - Requisitos funcionais e não-funcionais do sistema

- **`requisitos.md`**
  - Descripção dos requisitos em português
  
- **`mapeamento_entidades.md`**
  - Mapping entre entidades de negócio e tabelas do banco
  - Relacionamentos de domínio

### Design & UI

- **`design_system.md`**
  - Paleta de cores
  - Tipografia
  - Componentes reutilizáveis
  - Tokens de design

- **`design.md`**
  - Wireframes e fluxos de UI
  - Considerações de UX

### Módulos e Features

- **`modulo-01/`**
  - `2026-05-23-splash/` - Splash screen e verificação de sessão
  - `2026-05-23-login/` - Fluxo de login
  - `2026-05-23-cadastro-aluno/` - Sign up de alunos
  - `2026-05-23-recuperacao-senha/` - Password reset

---

## 🔧 Stack Tecnológico

### Backend
- **Banco de Dados:** Supabase (PostgreSQL)
- **Autenticação:** Supabase Auth (GoTrue)
- **Segurança:** Row Level Security (RLS) em nível de banco
- **Infraestrutura:** Backend as a Service (BaaS)

### Aplicativo Android
- **Linguagem:** Kotlin
- **UI Framework:** Jetpack Compose
- **Arquitetura:** MVVM + Clean Architecture
- **Gradle:** Gradle Wrapper (JDK 17+)

---

## 📊 Arquitetura do Banco

```
Supabase PostgreSQL Server
├── auth.users (Supabase Auth)
│   └── Trigger → created_at_usuario()
│       └── Insere automaticamente em usuarios
│
└── public schema
    ├── usuarios (núcleo de identidade)
    ├── livros (catálogo)
    ├── favoritos (N:M usuarios ↔ livros)
    ├── progresso_leitura (leitura em andamento)
    ├── resenhas (avaliações)
    ├── eventos (comunidade)
    ├── notificacoes (sistema)
    ├── obras_autorais (user-generated content)
    ├── historico_pontos (gamificação audit)
    ├── atividades_admin (admin audit)
    └── configuracoes_sistema (tuning)
```

---

## 🔐 Segurança

- **Row Level Security (RLS):** Habilitado em todas as tabelas que contêm dados pessoais
- **Autenticação:** Supabase GoTrue com e-mail e senha
- **Validação de E-mail:** Apenas domínios `@unifor.br` e `@edu.unifor.br`
- **Token Management:** Persistência segura via `EncryptedSharedPreferences`
- **Sincronização:** Dados críticos (pontos, status) sincronizados após login

---

## 📱 Fluxos Principais

### 1. **Autenticação**
   - Splash Screen → verificação de sessão
   - Login/SignUp com validação de domínio
   - Home (Aluno ou Admin)

### 2. **Leitura**
   - Acervo (catálogo) → Livro → Leitura com progresso
   - Favoritos armazenados em `LivrosSalvosState` globalmente

### 3. **Comunidade**
   - Resenhas (moderadas)
   - Eventos (campanhas, palestras)
   - Notificações

### 4. **Gamificação**
   - Pontos por ações (resenhas, conclusões, eventos)
   - Histórico completo em `historico_pontos`
   - Níveis baseados em `pontos_gamificacao`

---

## 🚀 Como Começar

1. **Confira o Schema Supabase:** Leia `supabase_schema.md` para entender as tabelas
2. **Entenda a Autenticação:** Veja `auth_supabase.md` para fluxos de login/signup
3. **Revise Requisitos:** Consulte `requirements.md` para features esperadas
4. **Design:** Visualize em `design_system.md` e `design.md`
5. **Implementação:** Siga as convenções em `../../../copilot-instructions.md`

---

## 📝 Convenções

- **Rotas:** Portuguese labels + snake_case paths (ex: `rota_home_aluno`)
- **Nomes de Telo:** Prefixo `Tela...` (ex: `TelaLogin`, `TelaAcervo`)
- **Tabelas:** Plurais em snake_case (ex: `usuarios`, `livros`, `favoritos`)
- **Colunas:** snake_case com prefixos para relação (ex: `usuario_id`, `livro_id`)

---

## 🔗 Documentação Externa

- [Supabase Docs](https://supabase.com/docs)
- [Supabase Auth](https://supabase.com/docs/guides/auth)
- [PostgreSQL Docs](https://www.postgresql.org/docs/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)

---

**Última atualização:** 2026-05-24

**Status:** Documentação ativa e em evolução 🚀


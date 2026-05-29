# STACK.md — Stack Tecnológico do ForLibrary

## Aplicativo Android

| Camada | Tecnologia | Versão / Notas |
|---|---|---|
| Linguagem | Kotlin | JDK 17+ |
| UI Framework | Jetpack Compose + Material 3 | Tema via `Theme.Material3.DayNight.NoActionBar` |
| Arquitetura UI | MVVM | `ViewModel` + `StateFlow` / `mutableStateOf` |
| Navegação | Jetpack Navigation Compose | `NavHost` + `sealed class Rota` |
| Async | Kotlin Coroutines | `viewModelScope`, `LaunchedEffect`, `rememberCoroutineScope` |
| Build | Gradle Wrapper | `build.gradle.kts` |

## Backend (BaaS)

| Serviço | Tecnologia | Uso |
|---|---|---|
| Banco de dados | Supabase PostgreSQL | 11 tabelas públicas + `auth.users` |
| Autenticação | Supabase Auth (GoTrue) | Email/senha, validação de domínio `@unifor.br` |
| Segurança | Row Level Security (RLS) | Políticas por `auth.uid()` e papel admin/aluno |
| Storage | Supabase Storage | Capas de livros, PDFs, fotos de perfil (a integrar) |
| SDK Android | `io.github.jan.supabase:supabase-kt` | Acessa `auth`, `postgrest`, `storage` |

## APIs Externas

| API | Uso | Status |
|---|---|---|
| OpenLibrary (openlibrary.org) | Carregamento de capas de livros por ISBN | Em uso (`CapaLivro.kt`) |
| Unsplash (mock) | Banners de eventos em dados mock | Apenas para desenvolvimento |
| IA (Anthropic/OpenAI) | ChatBot RF41 | **Não conectada** (bloqueio BLOQ-02) |

## Armazenamento Local

| Tecnologia | Uso |
|---|---|
| `EncryptedSharedPreferences` | Persistência segura de tokens de sessão |
| Sem Room/SQLite | Não há banco local — dados sempre do Supabase |

## Ferramentas de Desenvolvimento

| Ferramenta | Uso |
|---|---|
| Android Studio | IDE principal |
| Git + GitHub | Controle de versão; branches: `develop` (main), `lucasdev`, feature branches por módulo |

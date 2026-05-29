# STACK.md — Stack Tecnológico do ForLibrary

> Última sincronização com o código: 2026-05-29 (branch `lucasdev`).

## Aplicativo Android

| Camada | Tecnologia | Versão / Notas |
|---|---|---|
| Linguagem | Kotlin | `kotlin = 2.2.10` |
| Build | Android Gradle Plugin | `agp = 9.1.1` (`build.gradle.kts`) |
| SDK | compileSdk 36 (minor 1) · targetSdk 36 · minSdk 28 | `app/build.gradle.kts` |
| JDK | Java 11 | `sourceCompatibility/targetCompatibility = VERSION_11` |
| UI Framework | Jetpack Compose + Material 3 | `material3 = 1.4.0` |
| Ícones | `material-icons-extended` | `composeIcons = 1.6.0` |
| Imagens | Coil | `io.coil-kt:coil-compose:2.6.0` (capas via `AsyncImage`) |
| Arquitetura UI | MVVM | `ViewModel` + `StateFlow`/`mutableStateOf` |
| Navegação | Jetpack Navigation Compose | `navigationCompose = 2.9.7` · `NavHost` + `sealed class Rota` |
| Async | Kotlin Coroutines | `viewModelScope`, `LaunchedEffect`, `rememberCoroutineScope` |
| Serialização | `kotlinx.serialization` | `kotlinxSerializationJson = 1.11.0` (DTOs `@Serializable`) |

## Backend (BaaS) — Supabase

| Serviço | Tecnologia | Uso |
|---|---|---|
| Banco de dados | Supabase PostgreSQL | tabelas públicas + `auth.users` |
| Autenticação | Supabase Auth (GoTrue) | Email/senha, validação de domínio institucional |
| Segurança | Row Level Security (RLS) | Políticas por `auth.uid()` e papel admin/aluno |
| Storage | Supabase Storage | **Em uso** — buckets `imagens_livros`, `arquivos_livros`, `avatars`, `obras` |
| SDK Android | `io.github.jan-tennert.supabase` `3.6.0` | `supabase-kt`, `auth-kt`, `postgrest-kt`, `storage-kt` |
| HTTP Engine | Ktor OkHttp | `ktorClientOkhttp = 3.5.0` |

Cliente global `supabase` declarado como `val` de topo em `MainActivity.kt`, com `install(Auth)`, `install(Postgrest)`, `install(Storage)`. URL e chave estão **hardcoded** no arquivo (ver `CONCERNS.md`).

## APIs Externas

| API | Uso | Status |
|---|---|---|
| OpenLibrary (`covers.openlibrary.org`) | Capas de livros por ISBN | Em uso (`CapaLivro.kt`) |
| IA (Anthropic/OpenAI) | ChatBot RF41 | **Não conectada** — `ChatBotViewModel` usa respostas por palavra-chave hardcoded + delay simulado |

## Armazenamento Local

| Tecnologia | Uso |
|---|---|
| Jetpack DataStore Preferences | `datastore = 1.1.1` — preferências do app (tema escuro, notificações push) em `ConfigViewModel` (`Context.dataStore`, store `"settings"`) |
| Sessão de auth | Persistida pelo próprio `supabase-kt` Auth (não há `EncryptedSharedPreferences` no código) |
| Estado em memória (mock) | `core/data/LivrosSalvosState` + `catalogoGlobal` — ISBNs salvos e catálogo hardcoded, não persistidos |
| Sem Room/SQLite | Não há banco local — dados vêm do Supabase |

## Ferramentas de Desenvolvimento

| Ferramenta | Uso |
|---|---|
| Android Studio | IDE principal |
| Git + GitHub | Controle de versão; branches: `develop` (main), `lucasdev`, feature branches por módulo |

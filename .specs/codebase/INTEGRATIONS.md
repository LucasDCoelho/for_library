# INTEGRATIONS.md — Integrações Externas do ForLibrary

> Última sincronização com o código: 2026-05-29 (branch `lucasdev`).

## 1. Supabase (BaaS Principal)

### Autenticação (GoTrue)

**SDK:** `supabaseClient.auth`

| Fluxo | Chamada | Resultado |
|---|---|---|
| Login | `auth.loginWith(Email) { email; password }` | Sessão criada; `user_metadata.tipo` define rota (`admin` vs aluno) |
| Cadastro | `auth.signUpWith(Email) { email; password }` | Trigger cria linha em `public.usuarios` |
| Logout | `auth.signOut()` | Sessão destruída; navega para Login |
| Recovery | `auth.resetPasswordForEmail(email)` | Envia link por e-mail |
| Validação de domínio | Manual no ViewModel | Rejeitar se não termina em `@unifor.br` ou `@edu.unifor.br` |

**Persistência de sessão:** `EncryptedSharedPreferences`

### PostgreSQL (PostgREST)

**SDK:** `supabaseClient.postgrest`

Acesso via `supabaseClient.postgrest["nome_tabela"]` com `.select()`, `.insert()`, `.update()`, `.delete()`.

RLS garante isolamento: cada `auth.uid()` acessa apenas seus próprios dados (exceto admin).

### Storage

**Status:** ✅ Integrado (BLOQ-01 resolvido). `install(Storage)` em `MainActivity.kt`.

Buckets em uso (nomes reais no código):
| Bucket | Conteúdo | Usado em |
|---|---|---|
| `imagens_livros` | Capas (JPG) | `CadastroLivroViewModel`, `EdicaoLivroViewModel` |
| `arquivos_livros` | PDFs de livros | `CadastroLivroViewModel`, `EdicaoLivroViewModel` |
| `avatars` | Foto de perfil | `EdicaoPerfilViewModel` |
| `obras` | PDFs de obras autorais | `EnvioObraViewModel` |

Padrão de upload: ler bytes via `context.contentResolver.openInputStream(uri)` → `supabase.storage.from(bucket).upload(nome, bytes)` → `.publicUrl(nome)` salvo na coluna `capa_url`/`arquivo_url`/etc.

---

## 2. OpenLibrary (Capas de Livros)

**Arquivo:** `core/components/CapaLivro.kt`

**URL base:** `https://covers.openlibrary.org/b/isbn/{ISBN}-L.jpg`

| Estado | Comportamento |
|---|---|
| Carregando | Placeholder cinza |
| ISBN encontrado | Capa exibida via `AsyncImage` (Coil) |
| Não encontrado / sem URL | Fallback com iniciais do título em fundo colorido (`coresFallback`) |

---

## 3. ChatBot (RF41) — UI pronta, IA ainda não conectada (BLOQ-02 parcial)

**Implementado:** `feature/aluno/home/ui/ChatBotAluno.kt` + `home/viewmodel/ChatBotViewModel.kt`.

Estado atual: **respostas locais por palavra-chave** (`quandoPergunta()` com `when` sobre termos como "livro", "ponto", "evento") + `delay(1500)` simulando digitação. Mensagem inicial hardcoded (RF41.4). **Não há chamada a nenhuma API de IA.**

Para conectar IA de verdade:
- **Anthropic Claude API** — modelo `claude-haiku-4-5-20251001` (econômico para chat)
- Substituir `quandoPergunta()` por chamada de rede; a estrutura `MensagemChat`/`ChatBotState` já suporta histórico.

---

## 4. Android Calendar Intent (RF17.5)

Integração nativa via `Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI)`.

Status atual: não implementado — deve exibir Toast verde "Adicionado ao calendário" após sucesso.

---

## 5. Android File Picker (RF15.5, RF28.5, RF29.3/29.4)

Seleção de PDFs/imagens via `Uri` + `ActivityResult`, lidos com `contentResolver.openInputStream`.

Status atual: ✅ **funcional** nos fluxos que fazem upload — `TelaAdicionarObra`/`CadastroLivroViewModel`, `TelaEditarObra`/`EdicaoLivroViewModel`, `TelaEnvioObra`/`EnvioObraViewModel`, `TelaEditarPerfil`/`EdicaoPerfilViewModel`.

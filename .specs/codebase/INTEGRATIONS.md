# INTEGRATIONS.md — Integrações Externas do ForLibrary

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

**Status:** Não integrado ainda (BLOQ-01)

Buckets esperados:
- `capas` — imagens JPG/PNG de livros
- `arquivos` — PDFs/ePubs de livros
- `fotos-perfil` — avatares de usuários
- `obras-autorais` — PDFs submetidos por alunos

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

## 3. ChatBot (Pendente — BLOQ-02)

**RF41** requer integração com IA.

Opções candidatas:
- **Anthropic Claude API** — modelo `claude-haiku-4-5-20251001` (mais econômico para chat)
- **OpenAI API** — alternativa

Arquitetura sugerida quando implementado:
```
TelaChat.kt → ChatViewModel → ChatRepository → Anthropic/OpenAI API
```
A mensagem inicial do assistente (`RF41.4`) pode ser hardcoded ou vir de um prompt de sistema.

---

## 4. Android Calendar Intent (RF17.5)

Integração nativa via `Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI)`.

Status atual: não implementado — deve exibir Toast verde "Adicionado ao calendário" após sucesso.

---

## 5. Android File Picker (RF15.5, RF28.5, RF29.3/29.4)

Intent nativo `Intent.ACTION_OPEN_DOCUMENT` para selecionar PDFs e imagens.

Status atual: não funcional em `TelaEnvioObra`, `TelaAdicionarObra`, `TelaEditarObra`.

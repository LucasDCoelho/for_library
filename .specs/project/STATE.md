# STATE.md — Estado Atual do Projeto ForLibrary

## Última Atualização: 2026-05-28

### Evento: Correção RF06.7 + RF09 (navegação Acervo→Detalhes e bugs visuais)

**Ação executada:** Auditoria completa da estrutura `.specs/`, criação da hierarquia `project/`, `codebase/`, padronização de conteúdo e atualização do README.

---

## Estado do Branch Atual

**Branch:** `lucasdev`  
**Base:** `develop`

### Arquivos Modificados (em progresso)
| Arquivo | Natureza da Mudança |
|---|---|
| `ForLibraryApp.kt` | Ajuste de rotas / imports |
| `CapaLivro.kt` | Refatoração do componente de capa |
| `AdicionarLivro.kt` | Integração com `CadastroLivroViewModel` |
| `TelaEditarObra.kt` | Integração com `EdicaoLivroViewModel` |
| `TelaGestaoAcervo.kt` | Integração com `GestaoAcervoViewModel` |
| `TelaAcervoDigital.kt` | Ajustes de busca/filtro |
| `TelaEstante.kt` | Ajustes de favoritos |

### Arquivos Novos (não commitados)
| Arquivo | Propósito |
|---|---|
| `CadastroLivroViewModel.kt` | ViewModel para RF28 (Adição de Livro) |
| `EdicaoLivroViewModel.kt` | ViewModel para RF29 (Edição de Livro) |
| `GestaoAcervoViewModel.kt` | ViewModel para RF27 (Gestão de Acervo) |

---

## Bloqueios Ativos

| ID | Descrição | Impacto | Responsável |
|---|---|---|---|
| ~~BLOQ-01~~ | ~~Upload de arquivo (PDF/capa) para Supabase Storage~~ | ✅ **RESOLVIDO em 2026-05-28** | LucasDCoelho |
| BLOQ-02 | ChatBot (RF41) sem integração com IA — Anthropic ou OpenAI API não conectada | RF41.7–41.9 bloqueados | A definir |
| BLOQ-03 | Notificações push (RNF07) sem implementação de FCM/Supabase Realtime | RF18 completamente não funcional | A definir |
| BLOQ-04 | Foto de perfil (RF20.2) sem upload — dependente de Supabase Storage | RF20.2 bloqueado | A definir |

> **BLOQ-01 resolvido:** `CadastroLivroViewModel` e `EdicaoLivroViewModel` agora fazem upload de capa (bucket `imagens_livros`) e PDF (bucket `arquivos_livros`) via `supabase.storage`. `LivroAdmin` expandido com todos os campos. `TelaEditarObra` recebeu área de upload de PDF, campos de Total de Páginas, Ano e Sinopse. Lista de gêneros padronizada conforme CONVENTIONS.md em ambas as telas.

---

## Decisões Arquiteturais Registradas (ADRs)

| ADR | Decisão | Data |
|---|---|---|
| ADR-007 | Telas de listagem admin com BottomBar não têm botão de voltar (RF27.1) | 2026-05-24 |
| ADR-008 | Ícone de favorito substituído por "salvar/bookmark"; estado renomeado `salvo`; VM renomeado `toggleSalvo` (RF09.5, RF13.2, RF13.3) | ✅ 2026-05-28 |
| ADR-009 | RF31.2: Exclusão de eventos terá popup próprio (não reusar RF30 de livros) | 2026-05-24 |

---

## Dívidas Técnicas Conhecidas

| Área | Dívida |
|---|---|
| Design System | Tokenização parcial — cores e tipografia hardcoded em ~60% das telas |
| Arquitetura | Ausência de camada Repository em features mais antigas (auth, eventos) |
| Estado UI | Dados de gamificação/notificações apenas mockados; sem StateFlow conectado ao Supabase |
| Texto | Strings em inglês na moderação de resenhas (RF34.2, RF34.4) |
| UX | Labels e placeholders errados em Login (RF02.3/02.4) e Recuperação de Senha (RF03.3) |

---

## Progresso Geral

```
Módulo 1 (Auth):      ████████░░  ~85%
Módulo 2 (Aluno):     ██████░░░░  ~60%
Módulo 3 (Admin):     █████░░░░░  ~52%  (+RF27/28/29/30 concluídos)
─────────────────────────────────────
Global:               ██████░░░░  ~63%
```

---

## Histórico de Eventos de Estado

| Data | Evento |
|---|---|
| 2026-05-28 | **RF06.7 + RF09 corrigidos** — navegação Acervo→Detalhes corrigida (Home hardcoded `"livro_id_exemplo"` → ID real); `capa_url` adicionado a `LivroDetalhes`; `CapaLivro` aceita `capaUrl` com prioridade sobre ISBN; background `Color.White` → `MaterialTheme`; ADR-008 aplicado (`favoritado`→`salvo`, `toggleFavorito`→`toggleSalvo`) |
| 2026-05-28 | **RF30.3 concluído** — exclusão de livros funcional: `deletarLivro()` + `PopupExclusaoObra` + remoção reativa da lista + Snackbar |
| 2026-05-28 | **BLOQ-01 resolvido** — Storage conectado em `CadastroLivroViewModel` e `EdicaoLivroViewModel`; `TelaEditarObra` e `LivroAdmin` expandidos |
| 2026-05-28 | Auditoria `.specs/` — criação de `project/`, `codebase/`, padronização |
| 2026-05-24 | Commit massa RF20–26 (`03dbbbc`) — múltiplos requisitos implementados |
| 2026-05-23 | Módulo 1 (auth) implementado — splash, login, cadastro, recuperação |
| 2026-04-24 | Módulo 2 base (RF05–RF07) implementado |

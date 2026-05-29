# ForLibrary — Índice de Especificações (`.specs/`)

> Pasta de especificações do projeto **ForLibrary** — biblioteca digital universitária Android.  
> Última auditoria/padronização: **2026-05-28**

---

## Estrutura

```
.specs/
├── project/                  ← Estado e visão do projeto
│   ├── PROJECT.md            ← Contexto, objetivos, restrições, critérios de sucesso
│   ├── ROADMAP.md            ← Milestones por módulo, status de cada feature, próximos passos
│   └── STATE.md              ← Estado atual, branch ativo, bloqueios, ADRs, dívidas técnicas
│
├── codebase/                 ← Documentação técnica do código existente (brownfield)
│   ├── ARCHITECTURE.md       ← MVVM + Clean Architecture, estrutura de pacotes, fluxo de dados
│   ├── STACK.md              ← Kotlin, Jetpack Compose, Supabase, dependências
│   ├── CONVENTIONS.md        ← Nomenclatura, padrões de Compose, enums, tokens obrigatórios
│   ├── DATA_MODEL.md         ← Data classes de domínio, tabelas Supabase, regras de negócio
│   ├── INTEGRATIONS.md       ← Supabase (auth/DB/storage), OpenLibrary, ChatBot, Calendar
│   ├── ROUTES.md             ← Todas as rotas de navegação e telas correspondentes
│   └── COMPONENTS.md        ← Componentes compartilhados (CapaLivro, BottomBars, PopupLogout)
│
├── modulo-01/                ← Features: Autenticação (RF01–RF04)
│   ├── 2026-05-23-splash/
│   ├── 2026-05-23-login/
│   ├── 2026-05-23-cadastro-aluno/
│   └── 2026-05-23-recuperacao-senha/
│
├── modulo-02/                ← Features: Área do Aluno (RF05–RF25)
│   ├── 2026-04-24-RF05-home-aluno/
│   ├── 2026-04-24-RF06-acervo-digital/
│   └── ... (21 features)
│
├── modulo-03/                ← Features: Área do Administrador (RF26–RF41)
│   ├── 2026-05-24-RF26-Dashboard-Admin/
│   ├── 2026-05-24-RF27-Gestao-Acervo/
│   └── ... (16 features)
│
├── requirements.md           ← Tabela completa de RFs/RNFs com status de implementação
├── requisitos.md             ← Dump original dos requisitos (formato bruto da equipe)
├── auth_supabase.md          ← Detalhes de autenticação Supabase (GoTrue SDK)
├── design.md                 ← Wireframes e fluxos de UI
├── design_system.md          ← Tokens visuais, paleta real, tipografia, inconsistências
├── mapeamento_entidades.md   ← Data classes Kotlin completas com anotações
├── supabase_schema.md        ← Schema PostgreSQL completo (11 tabelas, RLS, índices)
└── supabase_policies.md      ← Políticas RLS detalhadas por tabela
```

---

## Como Usar Esta Pasta

### Iniciando numa nova feature

1. Leia `project/STATE.md` para saber o contexto atual e bloqueios
2. Leia `project/ROADMAP.md` para identificar a próxima feature a implementar
3. Consulte `codebase/ARCHITECTURE.md` para entender onde o código deve ser criado
4. Abra o `plan.md` da feature correspondente em `modulo-0X/`

### Implementando uma feature

Cada pasta de feature contém:
- `plan.md` — arquitetura, entidades afetadas, passo a passo de implementação, ADRs locais
- `validation.md` — checklist de critérios de aceitação vinculados aos RFs

### Rastreabilidade de Requisitos

Todos os requisitos funcionais estão em `requirements.md` com status `✔️ / ❌ / ❓`.  
Os `validation.md` de cada feature referenciam os RFs correspondentes com `[cite: requirements.md]`.

---

## Estado Rápido

| Módulo | Progresso |
|---|---|
| M1 — Autenticação (RF01–04) | ~85% |
| M2 — Área Aluno (RF05–25) | ~60% |
| M3 — Área Admin (RF26–41) | ~45% |
| **Global** | **~60%** |

**Sprint atual:** RF27/RF28/RF29 — ViewModels + persistência Supabase no acervo admin  
**Branch:** `lucasdev`

---

## Links Rápidos

- [Estado atual do projeto](project/STATE.md)
- [Roadmap e próximos passos](project/ROADMAP.md)
- [Requisitos completos](requirements.md)
- [Design System](design_system.md)
- [Schema do banco](supabase_schema.md)

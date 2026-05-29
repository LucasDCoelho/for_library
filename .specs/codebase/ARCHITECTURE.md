# ARCHITECTURE.md — Arquitetura do ForLibrary

> Última sincronização com o código: 2026-05-29 (branch `lucasdev`).

## Padrão Arquitetural

**MVVM (sem Clean Architecture completa)** — Compose para a View, `ViewModel` + `StateFlow` para o estado, acesso **direto** ao cliente Supabase global. **Não há camada Repository** em nenhuma feature.

```
app/
└── src/main/java/com/br/unifor/for_library/
    ├── MainActivity.kt             # Entry point + cliente global `supabase` (Auth/Postgrest/Storage)
    ├── ForLibraryApp.kt            # NavHost raiz + Scaffold + troca de BottomBar + PopupLogout
    │
    ├── core/
    │   ├── components/             # Composables compartilhados (CapaLivro, PopupLogout, FiltroAvancado)
    │   ├── data/                   # LivrosSalvosState + catalogoGlobal (estado mock em memória)
    │   ├── designsystem/           # Color.kt, Theme.kt
    │   └── navigation/             # Rotas.kt (sealed class Rota), ForLibraryBottomBar, AdminBottomBar
    │
    ├── feature/
    │   ├── auth/
    │   │   ├── AuthUtils.kt        # Validação de domínio, tipo por e-mail, tradução de erros
    │   │   ├── UsuarioSupabase.kt  # Constantes/DTO de usuário
    │   │   └── ui/                 # TelaSplash, TelaLoginPlaceholder, TelaCadastro, RecuperarSenha
    │   │
    │   ├── aluno/
    │   │   ├── acervo/   ui/ + viewmodel/   # TelaAcervoDigital, BuscaVaziaPlaceholder, AcervoViewModel
    │   │   ├── estante/  ui/ + viewmodel/   # TelaEstante, EstanteViewModel
    │   │   ├── eventos/  ui/ + viewmodel/   # TelaEventos, TelaDetalhesEvento, Eventos/DetalhesEventoViewModel, EventoDb
    │   │   ├── home/     ui/ + viewmodel/   # TelaHomeAluno, ChatBotAluno, Home/ChatBotViewModel
    │   │   ├── livro/    ui/ + viewmodel/   # TelaDetalhesLivro, TelaLeitorDigital, TelaAvaliacaoResenha, TelaFimLeitura
    │   │   ├── notificacao/ ui/ + viewmodel/
    │   │   ├── perfil/   ui/ + viewmodel/   # TelaPerfil, TelaEditarPerfil, TelaEnvioObra, TelaDuvidas, TelaConfiguracoes, TelaConfirmacaoLogout
    │   │   └── presentation/                # configuracoes/, gamificacao/, historico/ (Tela + ViewModel)
    │   │
    │   ├── adm/
    │   │   ├── acervo/             # TelaGestaoAcervo, AdicionarLivro, TelaEditarObra + Gestao/Cadastro/EdicaoLivroViewModel
    │   │   ├── dashboard/          # TelaDashboardAdmin + AdminDashboardViewModel
    │   │   ├── moderacao/          # TelaListaModeracaoAdmin, TelaGestaoUsuarios, TelaModeracaoObras, TelaAnaliseObra, PopupDetalhesUsuario
    │   │   │   └── modresenha/     # TelaModeracaoResenhas, TelaAnaliseResenha
    │   │   └── TelaConfigAdm.kt    # fun TelaConfiguracoesSistema
    │   │
    │   └── (raiz do pacote feature/ — fora de adm, ver CONCERNS)
    │       ├── TelaGestaoEventos.kt
    │       ├── TelaAdicionarEvento.kt
    │       └── TelaExclusaoObra.kt
```

## Fluxo de Dados

```
UI (Composable)
   ↕  observa state: StateFlow<XxxState> (collectAsState)
ViewModel  (XxxViewModel : ViewModel)
   ↕  monta DTOs @Serializable e chama o cliente diretamente
supabase  (global em MainActivity.kt) → auth · postgrest (.from) · storage
```

Padrão dominante por feature:
- Um **DTO `@Serializable`** com campos em `snake_case` que casa com a tabela (ex: `LivroAcervo`, `NovoLivro`, `EventoDb`), frequentemente `private` dentro do próprio ViewModel.
- Um **data class de UI state** (`XxxUiState`/`XxxState`) exposto via `MutableStateFlow(...).asStateFlow()`.
- Leitura/escrita com `supabase.from("tabela").select{...}.decodeList<DTO>()` / `.insert(...)` e upload com `supabase.storage.from("bucket").upload(...)`.

**Sem Repository:** features antigas e novas acessam `supabase` diretamente no ViewModel. Não existe `core/domain/model` no código — os modelos de domínio descritos em `DATA_MODEL.md` são parcialmente aspiracionais; a realidade são DTOs por feature.

## Injeção de Dependência

Sem framework de DI (Hilt/Koin não configurado). ViewModels são instanciados via `viewModel()` padrão do Compose e acessam o singleton global `supabase` por import direto.

## Navegação

**Jetpack Navigation Compose** com rotas tipadas em `core/navigation/Rotas.kt` (`sealed class Rota(val path)`). O `NavHost` raiz fica em `ForLibraryApp.kt` dentro de um `Scaffold`. Rotas com argumento expõem helper `criarRota(...)` (ex: `Rota.DetalhesLivro.criarRota(id)`). Ver `ROUTES.md`.

## Dois Contextos Visuais

| Contexto | BottomBar | Critério |
|---|---|---|
| Aluno | `ForLibraryBottomBar` | rota **não** está em `rotasAdmin` |
| Admin | `AdminBottomBar` | rota está no set `rotasAdmin` de `ForLibraryApp.kt` |

O padding do conteúdo só é aplicado para rotas no set `rotasComPadding`; rotas de auth/detalhe ficam `fillMaxSize`.

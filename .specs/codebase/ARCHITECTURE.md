# ARCHITECTURE.md — Arquitetura do ForLibrary

## Padrão Arquitetural

**MVVM + Clean Architecture (parcial)**

```
app/
└── src/main/java/com/br/unifor/for_library/
    ├── core/                        # Código compartilhado entre features
    │   ├── components/              # Composables reutilizáveis (CapaLivro, PopupLogout...)
    │   ├── designsystem/            # Tema, cores, tipografia (Color.kt, Theme.kt)
    │   ├── domain/model/            # Data classes de domínio (Livro, Usuario, Resenha...)
    │   └── navigation/              # Rotas, BottomBars (Rota.kt, ForLibraryBottomBar.kt, AdminBottomBar.kt)
    │
    ├── feature/
    │   ├── auth/
    │   │   └── ui/                  # TelaLoginPlaceholder, TelaCadastro, TelaRecuperarSenha, TelaSplashScreen
    │   │
    │   ├── aluno/
    │   │   ├── acervo/ui/           # TelaAcervoDigital
    │   │   ├── estante/ui/          # TelaEstante
    │   │   ├── eventos/ui/          # TelaEventos, TelaDetalhesEvento
    │   │   ├── home/ui/             # TelaHomeAluno
    │   │   ├── livro/ui/            # TelaDetalhesLivro, TelaLeitorDigital, TelaAvaliacaoResenha
    │   │   ├── notificacao/ui/      # TelaNotificacoes
    │   │   ├── perfil/ui/           # TelaPerfil, TelaEditarPerfil, TelaDuvidas, TelaEnvioObra
    │   │   └── presentation/        # TelaConfiguracoes, TelaGamificacao, TelaHistoricoLeitura
    │   │
    │   └── adm/
    │       ├── acervo/              # TelaGestaoAcervo, TelaAdicionarObra, TelaEditarObra + ViewModels
    │       ├── dashboard/           # TelaDashboardAdmin
    │       ├── moderacao/           # TelaListaModeracaoAdmin, TelaGestaoUsuarios, TelaModeracaoObras...
    │       │   └── modresenha/      # TelaModeracaoResenhas, TelaAnaliseResenha
    │       └── TelaConfiguracoesSistema
    │
    └── ForLibraryApp.kt             # NavHost raiz + Scaffold + BottomBar switching
```

## Fluxo de Dados

```
UI (Composable)
   ↕  observa StateFlow/State
ViewModel
   ↕  chama suspend functions
Repository (incompleto — não existe em todas as features)
   ↕  chama SDK
SupabaseClient (auth, postgrest, storage)
```

**Obs:** Features mais antigas (auth, eventos) ainda não têm camada Repository separada — o ViewModel acessa o `supabaseClient` diretamente.

## Injeção de Dependência

Sem framework de DI (Hilt/Koin não configurado). O `supabaseClient` é passado manualmente ou acessado como singleton global definido em `ForLibraryApp.kt`.

## Navegação

Baseada em **Jetpack Navigation Compose** com rotas tipadas via `sealed class Rota` em `core/navigation/Rota.kt`. O `NavHost` raiz está em `ForLibraryApp.kt` com um `Scaffold` que troca a BottomBar conforme a rota atual.

## Dois Contextos Visuais

| Contexto | BottomBar | Rotas |
|---|---|---|
| Aluno | `ForLibraryBottomBar` | Home, Acervo, Estante, Eventos, Perfil |
| Admin | `AdminBottomBar` | Dashboard, Acervo, Moderação, Eventos |

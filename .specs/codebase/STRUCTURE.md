# STRUCTURE.md — Estrutura de Diretórios do ForLibrary

> Última sincronização com o código: 2026-05-29 (branch `lucasdev`).

## Raiz do repositório

```
for_library/
├── app/                      # Módulo Android único
│   ├── build.gradle.kts      # Dependências do app
│   └── src/
│       ├── main/java/com/br/unifor/for_library/   # Código de produção
│       ├── test/             # Testes unitários JVM (apenas exemplo)
│       └── androidTest/      # Testes instrumentados (apenas exemplo)
├── build.gradle.kts          # Config raiz Gradle
├── settings.gradle.kts
├── gradle/libs.versions.toml # Version catalog (fonte das versões)
├── gradle.properties · local.properties
├── docs/                     # Documentação geral do projeto
├── .specs/                   # Specs (este mapeamento + requisitos/módulos)
│   ├── codebase/             # ESTE diretório (mapeamento de brownfield)
│   ├── project/              # PROJECT.md, ROADMAP.md, STATE.md
│   ├── modulo-01..03/        # Specs por módulo
│   └── *.md                  # requisitos, schema/políticas Supabase, design
└── README.md
```

## `app/src/main/java/com/br/unifor/for_library/`

```
MainActivity.kt              # Entry point + cliente global `supabase` + constantes SUPABASE_URL/KEY
ForLibraryApp.kt             # NavHost + Scaffold + troca de BottomBar + PopupLogout

core/
├── components/              # CapaLivro.kt · PopupLogout.kt · FiltroAvancado.kt
├── data/                    # LivrosSalvosState.kt (catalogoGlobal + ISBNs salvos em memória)
├── designsystem/            # Color.kt · Theme.kt
└── navigation/              # Rotas.kt · ForLibraryBottomBar.kt · AdminBottomBar.kt

feature/
├── auth/
│   ├── AuthUtils.kt · UsuarioSupabase.kt
│   └── ui/                  # TelaSplash · TelaLoginPlaceholder · TelaCadastro · RecuperarSenha
│
├── aluno/
│   ├── acervo/      ui/ (TelaAcervoDigital, BuscaVaziaPlaceholder) + viewmodel/ (AcervoViewModel)
│   ├── estante/     ui/ + viewmodel/
│   ├── eventos/     ui/ (TelaEventos, TelaDetalhesEvento) + viewmodel/ (Eventos/DetalhesEventoViewModel, EventoDb)
│   ├── home/        ui/ (TelaHomeAluno, ChatBotAluno) + viewmodel/ (Home/ChatBotViewModel)
│   ├── livro/       ui/ (TelaDetalhesLivro, TelaLeitorDigital, TelaAvaliacaoResenha, TelaFimLeitura) + viewmodel/
│   ├── notificacao/ ui/ + viewmodel/
│   ├── perfil/      ui/ (TelaPerfil, TelaEditarPerfil, TelaEnvioObra, TelaDuvidas, TelaConfiguracoes, TelaConfirmacaoLogout) + viewmodel/
│   └── presentation/
│       ├── configuracoes/   TelaConfiguracoes + ConfigViewModel (DataStore)
│       ├── gamificacao/     TelaGamificacao + GamificacaoViewModel
│       └── historico/       TelaHistoricoLeitura + HistoricoLeituraViewModel
│
├── adm/
│   ├── acervo/      TelaGestaoAcervo · AdicionarLivro · TelaEditarObra + Gestao/Cadastro/EdicaoLivroViewModel
│   ├── dashboard/   TelaDashboardAdmin + AdminDashboardViewModel
│   ├── moderacao/   TelaListaModeracaoAdmin · TelaGestaoUsuarios · TelaModeracaoObras · TelaAnaliseObra · PopupDetalhesUsuario
│   │   └── modresenha/  TelaModeracaoResenhas · TelaAnaliseResenha
│   └── TelaConfigAdm.kt     # fun TelaConfiguracoesSistema
│
└── (raiz feature/ — itens fora de adm/, ver CONCERNS)
    ├── TelaGestaoEventos.kt
    ├── TelaAdicionarEvento.kt
    └── TelaExclusaoObra.kt
```

## Convenções de organização

- **1 feature = pacote** com sub-pacotes `ui/` e `viewmodel/` (telas mais novas do aluno seguem isso à risca).
- `presentation/` (sob `aluno/`) é uma nomenclatura paralela a `ui+viewmodel` usada em 3 telas — inconsistência herdada.
- Composables raiz seguem `Tela<Nome>.kt`; exceções conhecidas em `CONCERNS.md`.
- Modelos: **não há `core/domain/model`**; DTOs vivem por feature (ver `DATA_MODEL.md`).

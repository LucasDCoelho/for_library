# ROUTES.md — Rotas de Navegação do ForLibrary

> Última sincronização com o código: 2026-05-29 (branch `lucasdev`).
> Fonte: `core/navigation/Rotas.kt` + `ForLibraryApp.kt`.

> ⚠️ Nomes de arquivo divergem da função/classe em vários casos (coluna "Arquivo → fun").

## Rotas Definidas (`sealed class Rota`)

### Fluxo de Autenticação (sem BottomBar)

| `Rota` | path | Arquivo → fun |
|---|---|---|
| `Splash` | `splash` | `auth/ui/TelaSplash.kt` → `TelaSplashScreen` |
| `Login` | `login` | `auth/ui/TelaLoginPlaceholder.kt` → `TelaLoginPlaceholder` |
| `Cadastro` | `cadastro` | `auth/ui/TelaCadastro.kt` → `TelaCadastro` |
| `RecuperarSenha` | `recuperar_senha` | `auth/ui/RecuperarSenha.kt` → `TelaRecuperarSenha` |

`Splash` decide o destino: Login, `HomeAluno` ou `DashboardAdmin`.

### Área Aluno

| `Rota` | path | Arquivo |
|---|---|---|
| `HomeAluno` | `home_aluno` | `aluno/home/ui/TelaHomeAluno.kt` |
| `Acervo` | `acervo` | `aluno/acervo/ui/TelaAcervoDigital.kt` |
| `Estante` | `estante` | `aluno/estante/ui/TelaEstante.kt` |
| `Eventos` | `eventos` | `aluno/eventos/ui/TelaEventos.kt` |
| `Perfil` | `perfil` | `aluno/perfil/ui/TelaPerfil.kt` |
| `DetalhesLivro` | `detalhes_livro/{livroId}` | `aluno/livro/ui/TelaDetalhesLivro.kt` |
| `LeitorDigital` | `leitor/{livroId}/{titulo}` | `aluno/livro/ui/TelaLeitorDigital.kt` |
| `AvalicaoLivro` | `avaliacao/{livroId}` | `aluno/livro/ui/TelaAvaliacaoResenha.kt` |
| `DetalhesEvento` | `detalhes_evento/{eventoId}` (Int) | `aluno/eventos/ui/TelaDetalhesEvento.kt` |
| `Notificacoes` | `notificacoes` | `aluno/notificacao/ui/TelaNotificacoes.kt` |
| `EditarPerfil` | `editar_perfil` | `aluno/perfil/ui/TelaEditarPerfil.kt` |
| `EnvioObra` | `envio_obra` | `aluno/perfil/ui/TelaEnvioObra.kt` |
| `Duvida` | `duvida` | `aluno/perfil/ui/TelaDuvidas.kt` |
| `Configuracoes` | `configuracoes` | `aluno/presentation/configuracoes/TelaConfiguracoes.kt` |
| `MeusPontos` | `meus_pontos` | `aluno/presentation/gamificacao/TelaGamificacao.kt` |
| `HistoricoLeitura` | `historico_leitura` | `aluno/presentation/historico/TelaHistoricoLeitura.kt` |

> Notas: a rota de gamificação é `MeusPontos` (não `Gamificacao`); a de avaliação é `AvalicaoLivro` (grafia com typo no código). Existe um segundo `TelaConfiguracoes.kt` em `aluno/perfil/ui/` que **não** é referenciado pelo NavHost.

### Área Admin (com `AdminBottomBar`)

| `Rota` | path | Arquivo → fun |
|---|---|---|
| `DashboardAdmin` | `dashboard_admin` | `adm/dashboard/TelaDashboardAdmin.kt` |
| `AcervoAdmin` | `acervo_admin` | `adm/acervo/TelaGestaoAcervo.kt` |
| `AdicionarLivro` | `adicionar_livro` | `adm/acervo/AdicionarLivro.kt` → `TelaAdicionarObra` |
| `EditarObra` | `editar_obra/{livroId}` | `adm/acervo/TelaEditarObra.kt` |
| `ListaModeracao` | `lista_moderacao` | `adm/moderacao/TelaListaModeracaoAdmin.kt` |
| `GestaoUsuarios` | `gestao_usuarios` | `adm/moderacao/TelaGestaoUsuarios.kt` |
| `ModeracaoObras` | `moderacao_obras` | `adm/moderacao/TelaModeracaoObras.kt` |
| `AnaliseObra` | `analise_obra/{obraId}` | `adm/moderacao/TelaAnaliseObra.kt` |
| `ModeracaoResenhas` | `moderacao_resenhas` | `adm/moderacao/modresenha/TelaModeracaoResenhas.kt` |
| `AnaliseResenha` | `analise_resenha/{resenhaId}` | `adm/moderacao/modresenha/TelaAnaliseResenha.kt` |
| `EventosAdmin` | `eventos_admin` | `feature/TelaGestaoEventos.kt` (fora de `adm/`) |
| `AdicionarEvento` | `adicionar_evento` | `feature/TelaAdicionarEvento.kt` (fora de `adm/`) |
| `NotificacoesAdmin` | `notificacoes_admin` | `aluno/notificacao/ui/TelaNotificacoes.kt` (compartilhado — "Bug 3") |
| `ConfiguracoesSistema` | `configuracoes_sistema` | `adm/TelaConfigAdm.kt` → `TelaConfiguracoesSistema` |

Helpers de rota com argumento: `EditarObra.criarRota`, `AnaliseObra.criarRota`, `AnaliseResenha.criarRota`, `DetalhesLivro.criarRota`, `LeitorDigital.criarRota`, `AvalicaoLivro.criarRota`, `DetalhesEvento.criarRota`.

## BottomBar / padding switching (`ForLibraryApp.kt`)

- `rotaAtual in rotasAdmin` → `AdminBottomBar`; senão → `ForLibraryBottomBar`.
- `rotaAtual in rotasComPadding` → aplica `padding`; senão `fillMaxSize` (auth/detalhe).
- O logout dispara o Composable `PopupLogout` controlado por `mostrarPopupSair` no nível do app.

## Rota Inicial

`startDestination = Rota.Splash.path`

# ROUTES.md — Rotas de Navegação do ForLibrary

Fonte: `core/navigation/Rota.kt` + `ForLibraryApp.kt`

## Rotas Definidas (sealed class Rota)

### Fluxo de Autenticação

| Rota | Tela | Arquivo |
|---|---|---|
| `Rota.Splash` | TelaSplashScreen | `feature/auth/ui/TelaSplashScreen.kt` |
| `Rota.Login` | TelaLoginPlaceholder | `feature/auth/ui/TelaLoginPlaceholder.kt` |
| `Rota.Cadastro` | TelaCadastro | `feature/auth/ui/TelaCadastro.kt` |
| `Rota.RecuperarSenha` | TelaRecuperarSenha | `feature/auth/ui/TelaRecuperarSenha.kt` |

### Área Aluno (com ForLibraryBottomBar)

| Rota | Tela | Arquivo |
|---|---|---|
| `Rota.HomeAluno` | TelaHomeAluno | `feature/aluno/home/ui/TelaHomeAluno.kt` |
| `Rota.Acervo` | TelaAcervoDigital | `feature/aluno/acervo/ui/TelaAcervoDigital.kt` |
| `Rota.Estante` | TelaEstante | `feature/aluno/estante/ui/TelaEstante.kt` |
| `Rota.Eventos` | TelaEventos | `feature/aluno/eventos/ui/TelaEventos.kt` |
| `Rota.Perfil` | TelaPerfil | `feature/aluno/perfil/ui/TelaPerfil.kt` |
| `Rota.DetalhesLivro` | TelaDetalhesLivro | `feature/aluno/livro/ui/TelaDetalhesLivro.kt` |
| `Rota.LeitorDigital` | TelaLeitorDigital | `feature/aluno/livro/ui/TelaLeitorDigital.kt` |
| `Rota.AvaliacaoResenha` | TelaAvaliacaoResenha | `feature/aluno/livro/ui/TelaAvaliacaoResenha.kt` |
| `Rota.DetalhesEvento` | TelaDetalhesEvento | `feature/aluno/eventos/ui/TelaDetalhesEvento.kt` |
| `Rota.Notificacoes` | TelaNotificacoes | `feature/aluno/notificacao/ui/TelaNotificacoes.kt` |
| `Rota.EditarPerfil` | TelaEditarPerfil | `feature/aluno/perfil/ui/TelaEditarPerfil.kt` |
| `Rota.EnvioObra` | TelaEnvioObra | `feature/aluno/perfil/ui/TelaEnvioObra.kt` |
| `Rota.Duvidas` | TelaDuvidas | `feature/aluno/perfil/ui/TelaDuvidas.kt` |
| `Rota.Configuracoes` | TelaConfiguracoes | `feature/aluno/presentation/configuracoes/TelaConfiguracoes.kt` |
| `Rota.Gamificacao` | TelaGamificacao | `feature/aluno/presentation/gamificacao/TelaGamificacao.kt` |
| `Rota.HistoricoLeitura` | TelaHistoricoLeitura | `feature/aluno/presentation/historico/TelaHistoricoLeitura.kt` |

### Área Admin (com AdminBottomBar)

| Rota | Tela | Arquivo |
|---|---|---|
| `Rota.DashboardAdmin` | TelaDashboardAdmin | `feature/adm/dashboard/TelaDashboardAdmin.kt` |
| `Rota.AcervoAdmin` | TelaGestaoAcervo | `feature/adm/acervo/TelaGestaoAcervo.kt` |
| `Rota.AdicionarLivro` | TelaAdicionarObra | `feature/adm/acervo/AdicionarLivro.kt` |
| `Rota.EditarObra` | TelaEditarObra | `feature/adm/acervo/TelaEditarObra.kt` |
| `Rota.ListaModeracao` | TelaListaModeracaoAdmin | `feature/adm/moderacao/TelaListaModeracaoAdmin.kt` |
| `Rota.GestaoUsuarios` | TelaGestaoUsuarios | `feature/adm/moderacao/TelaGestaoUsuarios.kt` |
| `Rota.ModeracaoObras` | TelaModeracaoObras | `feature/adm/moderacao/TelaModeracaoObras.kt` |
| `Rota.AnaliseObra` | TelaAnaliseObra | `feature/adm/moderacao/TelaAnaliseObra.kt` |
| `Rota.ModeracaoResenhas` | TelaModeracaoResenhas | `feature/adm/moderacao/modresenha/TelaModeracaoResenhas.kt` |
| `Rota.AnaliseResenha` | TelaAnaliseResenha | `feature/adm/moderacao/modresenha/TelaAnaliseResenha.kt` |
| `Rota.EventosAdmin` | TelaGestaoEventos | `feature/TelaGestaoEventos.kt` |
| `Rota.AdicionarEvento` | TelaAdicionarEvento | `feature/TelaAdicionarEvento.kt` |
| `Rota.NotificacoesAdmin` | TelaNotificacoes | (compartilhado) |
| `Rota.ConfiguracoesSistema` | TelaConfiguracoesSistema | `feature/adm/TelaConfiguracoesSistema.kt` |

## BottomBar Switching

Em `ForLibraryApp.kt`, a BottomBar exibida depende da rota atual:
- Rotas do conjunto `rotasComPadding` com prefixo de aluno → `ForLibraryBottomBar`
- Rotas do conjunto `rotasComPadding` com prefixo de admin → `AdminBottomBar`
- Rotas sem BottomBar (auth, detalhes) → sem bottom bar

## Rota Inicial

`startDestination = Rota.Splash.path`

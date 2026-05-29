# CONVENTIONS.md — Convenções do ForLibrary

> Última sincronização com o código: 2026-05-29 (branch `lucasdev`).

## Nomenclatura de Arquivos e Classes

| Tipo | Padrão | Exemplo |
|---|---|---|
| Tela (Composable raiz) | `Tela<Nome>.kt` | `TelaGestaoAcervo.kt`, `TelaEstante.kt` |
| ViewModel | `<Nome>ViewModel.kt` | `GestaoAcervoViewModel.kt`, `AcervoViewModel.kt` |
| DTO Supabase (`@Serializable`) | PascalCase, campos `snake_case` | `LivroAcervo`, `NovoLivro`, `EventoDb` |
| UI state | `<Nome>UiState` ou `<Nome>State` | `CadastroLivroUiState`, `AcervoState` |
| Tabela Supabase | plural, snake_case | `livros`, `usuarios`, `progresso_leitura` |
| Coluna Supabase | snake_case | `auth_user_id`, `livro_id`, `data_cadastro` |
| Rota de navegação | PascalCase na `sealed class Rota` | `Rota.HomeAluno`, `Rota.AcervoAdmin` |

**Atenção (drift conhecido):** vários arquivos quebram a regra `Tela<Nome>.kt` — o nome do arquivo difere da função/classe. Ex: `TelaConfigAdm.kt` → `TelaConfiguracoesSistema`, `TelaSplash.kt` → `TelaSplashScreen`, `RecuperarSenha.kt` → `TelaRecuperarSenha`, `AdicionarLivro.kt` → `TelaAdicionarObra`. Ver `CONCERNS.md`.

## Estrutura de Pacotes

```
feature/<contexto>/<sub-area>/<camada>/
```

Exemplos:
- `feature.aluno.acervo.ui` — telas de acervo do aluno
- `feature.adm.acervo` — telas e ViewModels de acervo admin
- `core.components` — Composables compartilhados
- `core.domain.model` — Data classes de domínio

## Padrões de Compose

- Telas usam `Scaffold` com `topBar` e/ou `bottomBar`
- Headers seguem padrão: `Row(avatar + título + ação de sino/config)`
- Listas longas usam `LazyColumn` ou `LazyVerticalGrid`
- `contentPadding` padrão de `16dp` nas listas
- Dialogs de confirmação (logout, exclusão) são Composables separados (ex: `PopupLogout`)

## Estados UI no ViewModel (padrão dominante)

Um único `StateFlow` com um data class de estado imutável:
```kotlin
private val _state = MutableStateFlow(AcervoState())
val state: StateFlow<AcervoState> = _state.asStateFlow()

// data class AcervoState(val isLoading: Boolean = false, val livros: List<...> = emptyList(), val error: String? = null)
```
Atualizações com `copy(...)`; alguns ViewModels usam um helper `private fun update(block: State.() -> State)`.

O padrão antigo `var x by mutableStateOf(...)` ainda aparece em telas mais simples, mas `StateFlow` + `collectAsState()` é o predominante nas features novas.

## DTOs e Serialização

- Cada feature define **DTOs `@Serializable`** com campos em `snake_case` casando com a tabela (ex: `LivroAcervo`, `NovoLivro`). Frequentemente declarados `private` dentro do próprio ViewModel.
- Conversão DTO → modelo de UI feita por função de extensão local (ex: `EventoDb.toEvento()`).

## Acesso ao Supabase

- Acesso **direto** ao cliente global `supabase` (sem Repository); import `com.br.unifor.for_library.supabase`.
- Leitura: `supabase.from("tabela").select { filter { ... }; order(...) }.decodeList<DTO>()` / `.decodeSingle<DTO>()`.
- Escrita: `supabase.from("tabela").insert(dto)` / `.update { ... }` / `.delete { ... }`.
- Storage: `supabase.storage.from("bucket").upload(nome, bytes)` + `.publicUrl(nome)`.
- Chamadas dentro de `viewModelScope.launch { ... }`, com `try/catch` atualizando `error`/`erro` no state.

## Autenticação (helpers centralizados em `feature/auth/AuthUtils.kt`)

- `emailInstitucionalValido(email)` — aceita `@unifor.br` e `@edu.unifor.br`.
- `obterTipoPorEmail(email)` — `@unifor.br` → admin; demais → aluno.
- `traduzirErroAuth(e)` — mapeia mensagens do GoTrue para texto amigável em PT-BR.
- `UsuarioBloqueadoException` — sinaliza conta `BLOQUEADO`.

## Enums de Domínio

| Enum | Valores |
|---|---|
| `TipoUsuario` | `ALUNO`, `ADMIN` |
| `StatusUsuario` | `ATIVO`, `BLOQUEADO` |
| `StatusLeitura` | `LENDO`, `CONCLUIDO` |
| `StatusModeracao` | `PENDENTE`, `APROVADA`, `REJEITADA` |
| `TipoEvento` | `WORKSHOP`, `PALESTRA`, `LANCAMENTO` |

## Tokens de Design Obrigatórios

Definidos em `core/designsystem/Color.kt`:
- `AzulPrimario = #1565C0` — cor de marca principal
- `AzulChip = #1E88E5` — ações secundárias
- `CinzaTexto = #616161` — textos secundários
- `FundoTela = #F2F4F8` — background de telas

**Evitar:** hardcodar outros tons de azul (`#1B65F6`, `#1E54FA`, etc.) — usar `AzulPrimario`.

## Gêneros Literários (lista oficial)

`FICÇÃO`, `ACADÊMICO`, `TECNOLOGIA`, `BIOGRAFIA`, `HISTÓRIA`, `DESIGN`

Usar esta lista em todos os Dropdowns (RF15.4, RF28.4, RF07.2).

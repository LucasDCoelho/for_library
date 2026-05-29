# CONVENTIONS.md — Convenções do ForLibrary

## Nomenclatura de Arquivos e Classes

| Tipo | Padrão | Exemplo |
|---|---|---|
| Tela (Composable raiz) | `Tela<Nome>.kt` | `TelaLogin.kt`, `TelaGestaoAcervo.kt` |
| ViewModel | `<Nome>ViewModel.kt` | `GestaoAcervoViewModel.kt`, `LoginViewModel.kt` |
| Data class de domínio | PascalCase | `Livro`, `Usuario`, `Resenha` |
| Tabela Supabase | plural, snake_case | `livros`, `usuarios`, `progresso_leitura` |
| Coluna Supabase | snake_case | `usuario_id`, `livro_id`, `data_cadastro` |
| Rota de navegação | PascalCase na `sealed class Rota` | `Rota.HomeAluno`, `Rota.AcervoAdmin` |

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

## Estados UI no ViewModel

```kotlin
var isLoading by mutableStateOf(false)
var errorMessage by mutableStateOf<String?>(null)
var <entidade>s by mutableStateOf<List<Entidade>>(emptyList())
```

Ou com `StateFlow`:
```kotlin
private val _state = MutableStateFlow(UiState())
val state: StateFlow<UiState> = _state.asStateFlow()
```

## Acesso ao Supabase

- O cliente Supabase (`supabaseClient`) é acessado no ViewModel
- Chamadas assíncronas dentro de `viewModelScope.launch { ... }`
- Erros capturados com `try/catch`; `errorMessage` atualizado no catch

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

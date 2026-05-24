# Plano de Correção e Implementação — RF22 (Histórico de Leitura e Lupa)

## 1. Localização e Escopo

| Campo         | Valor                                                                                                               |
|---------------|---------------------------------------------------------------------------------------------------------------------|
| **Módulo**    | `feature/aluno`                                                                                                     |
| **Tela**      | `feature/aluno/presentation/historico/TelaHistoricoLeitura.kt`                                                      |
| **ViewModel** | `HistoricoLeituraViewModel.kt`                                                                                      |

---

## 2. Refatoração da Lógica de Busca (ViewModel)

### 2.1 Estados Reativos

```kotlin
private val _searchQuery       = MutableStateFlow("")
val           searchQuery       = _searchQuery.asStateFlow()

private val _historicoOriginal = MutableStateFlow<List<ProgressoLeitura>>(emptyList())
```

### 2.2 Flow de Filtragem

```kotlin
val historicoFiltrado = combine(_historicoOriginal, _searchQuery) { historico, query ->
    if (query.isBlank()) {
        historico
    } else {
        historico.filter {
            // Busca case-insensitive no título e no autor
            it.livro?.titulo?.contains(query, ignoreCase = true) == true ||
            it.livro?.autor?.contains(query, ignoreCase = true) == true
        }
    }
}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
```

### 2.3 Ação de Escrita

```kotlin
fun updateSearchQuery(newQuery: String) {
    _searchQuery.value = newQuery
}
```

---

## 3. Estrutura da Interface — RF22.1 (TopAppBar Dinâmica)

### Estado local da tela

```kotlin
var isSearching by remember { mutableStateOf(false) }
```

### Modo Normal (`isSearching = false`)

| Elemento  | Comportamento                                                                         |
|-----------|---------------------------------------------------------------------------------------|
| `title`   | Texto fixo `"Histórico de Leitura"`                                                   |
| `actions` | `IconButton` com `Icons.Filled.Search` → ao clicar, define `isSearching = true`      |

### Modo Busca (`isSearching = true`)

| Elemento  | Comportamento                                                                                                        |
|-----------|----------------------------------------------------------------------------------------------------------------------|
| `title`   | `TextField` sem borda, `placeholder = "Pesquisar obra..."`, com `FocusRequester` para foco automático               |
| `actions` | `IconButton` com `Icons.Filled.Close` → ao clicar, define `isSearching = false` e chama `updateSearchQuery("")`     |

---

## 4. Listagem — RF22.2

- Consumir `historicoFiltrado` via `collectAsStateWithLifecycle()`.
- Renderizar com `LazyColumn`.
- Cada item exibe:
  - **Esquerda:** capa do livro (`CapaLivro`)
  - **Centro:** `Column` com Título, Autor e Data de Conclusão formatada

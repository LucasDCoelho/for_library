# Plano de Implementação - RF08 (Tela de Resultados de Busca Vazia)

## 1. Localização
- **Módulo:** `feature/aluno`
- **Arquivo Alvo:** `app/src/main/java/com/br/unifor/for_library/feature/aluno/components/AcervoEmptyState.kt`

## 2. Estrutura do Componente Compose
- Criar a função `@Composable fun AcervoEmptyState(onLimparFiltrosClick: () -> Unit)`
- A tela principal `TelaAcervo.kt` manterá o cabeçalho (RF08.1) e a `Bottom Navigation Bar` (RF08.6) fixos. O `AcervoEmptyState` será chamado no `body` da tela quando a lista de livros (`State` do ViewModel) retornar vazia.

## 3. UI e Estilização (Baseado em design_system.md)
- **Container:** `Column` centralizada (`Arrangement.Center`, `Alignment.CenterHorizontally`) ocupando todo o espaço restante (`fillMaxSize()`).
- **Imagem (RF08.3):**
    - Usar componente `Image` carregando o drawable amigável de livro vazio.
    - Aplicar `padding` inferior de `24dp` (múltiplo de 4dp).
- **Texto Principal (RF08.4):**
    - String: "Ops! Silêncio na biblioteca. Nenhum livro encontrado para esta pesquisa"
    - Estilo: `MaterialTheme.typography.titleMedium` ou similar.
    - Peso: `SemiBold`.
    - Cor: Texto forte primário.
- **Texto Explicativo (RF08.5):**
    - String: "Tente usar palavras-chave diferentes ou verifique a ortografia."
    - Estilo: `MaterialTheme.typography.bodyMedium`.
    - Cor: `CinzaTexto` (#616161).
    - Espaçamento: `padding` superior de `8dp`, inferior de `24dp`.
- **Botão (RF08.5/RF08.6):**
    - Componente: `Button`.
    - Cor de fundo: `AzulPrimario` (#1565C0).
    - Shape: `RoundedCornerShape(8.dp)`.
    - Ação: Invoca `onLimparFiltrosClick()` para despachar evento ao `AcervoViewModel` limpando a query/filtros e restaurando o Grid.

## 4. Integração
- Em `TelaAcervo.kt`:
```kotlin
  if (uiState.livrosBusca.isEmpty() && uiState.isSearching) {
      AcervoEmptyState(onLimparFiltrosClick = { viewModel.limparFiltros() })
  } else {
      AcervoGrid(livros = uiState.livrosBusca)
  }
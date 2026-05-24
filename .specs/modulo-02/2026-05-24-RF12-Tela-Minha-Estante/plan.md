# Plano de Implementação - RF12 e RF13 (Tela Minha Estante)

## 1. Localização e Escopo
- **Módulo:** `feature/aluno`
- **Componente Principal:** `app/src/main/java/com/br/unifor/for_library/feature/aluno/presentation/estante/TelaMinhaEstante.kt`
- **Subcomponentes:** `ItemLivroLendo.kt` e `ItemLivroFavorito.kt`.

## 2. Lógica de Domínio (EstanteViewModel)
- **Estado (UI State):** `isLoading`, `livrosLendo` (Lista combinando `Livro` e `ProgressoLeitura`), `livrosFavoritos` (Lista de `Livro` com ID de `Favorito`).
- **Ações:**
    - Consultar Supabase na tabela `progresso_leitura` filtrando por `auth.uid()` e `status = 'LENDO'`, e fazendo *join* manual ou via view com a tabela `livros`.
    - Consultar Supabase na tabela `favoritos` filtrando por `auth.uid()`, fazendo *join* com `livros`.
    - `removerFavorito(livroId)`: Invoca a deleção na tabela `favoritos`.

## 3. Estrutura da Interface (Compose)
- **Container Raiz:** `Scaffold` com `ForLibraryBottomBar` embutida no `bottomBar` (RF12.4 / RF13.4).
- **TopBar (RF12.1 ajustado):**
    - Título: "Minha Estante" (`titleLarge`, `SemiBold`).
    - Action (Direita): `IconButton` com `Icons.Outlined.History` para navegar até `rota_historico_leitura`.
    - *ADR Aplicado:* Ícone de Lupa suprimido por inoperância conforme queixa documentada no RF13.1.
- **TabRow (RF12.2):**
    - `TabRow(selectedTabIndex = tabIndex, containerColor = FundoTela, contentColor = AzulPrimario)`.
    - `Tab` 0: "Lendo".
    - `Tab` 1: "Favoritos".
- **Conteúdo das Abas (Crossfade ou Pager):**
    - **Aba Lendo (RF12.3):**
        - `LazyColumn` com `contentPadding = 16.dp` e `verticalArrangement = 12.dp`.
        - Componente `ItemLivroLendo`:
            - `Card` contendo `Row`.
            - Esquerda: `CapaLivro` (largura fixa `72.dp`).
            - Direita: `Column` com Título, Autor.
            - Base da Coluna Direita: `LinearProgressIndicator` (progress = `paginaAtual.toFloat() / totalPaginas`, color = `AzulPrimario`). Abaixo dele um texto miúdo (`labelSmall`) "{paginaAtual} / {totalPaginas} págs".
    - **Aba Favoritos (RF13.2):**
        - `LazyVerticalGrid(columns = GridCells.Fixed(2))` com `contentPadding = 16.dp`.
        - Componente `ItemLivroFavorito`:
            - `Card` vertical.
            - Topo direito fixo (Box sobreposto à capa): `IconButton(onClick = { desfavoritar })` contendo `Icons.Filled.Bookmark` e `tint = AzulPrimario` (Corrigindo o bug visual do coração apontado no RF13.3).
            - Centro: `CapaLivro` e `Text` com Título/Autor truncados (`maxLines = 1`).
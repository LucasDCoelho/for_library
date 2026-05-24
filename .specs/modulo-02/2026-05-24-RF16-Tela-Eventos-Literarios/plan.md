# Plano de Implementação - RF16 (Tela de Eventos Literários)

## 1. Localização e Escopo
- **Módulo:** `feature/aluno`
- **Arquivo Alvo:** `app/src/main/java/com/br/unifor/for_library/feature/aluno/presentation/eventos/TelaEventos.kt`
- **ViewModel:** `EventoViewModel` (Gerencia a lista de eventos filtrados).

## 2. Estrutura da Interface (Compose)
- **TopBar:** `Row` com a foto de perfil, Título "Eventos Literários" e o `IconButton` do sino.
- **Filtros (RF16.2):**
    - `LazyRow` contendo `FilterChip` (ou `AssistChip` do Material3).
    - Estado: `filtroSelecionado: TipoEvento?`.
- **Listagem de Eventos (RF16.3, RF16.4):**
    - `LazyColumn` com `contentPadding = PaddingValues(16.dp)` e `verticalArrangement = Arrangement.spacedBy(16.dp)`.
    - **Componente Card:**
        - `Card(shape = RoundedCornerShape(12.dp))`.
        - **Banner (Box):** Imagem com `contentScale = ContentScale.Crop`.
        - **Badge Data (RF16.4):** `Surface` com `shape = RoundedCornerShape(topStart = 12.dp, bottomEnd = 8.dp)`, cor `AzulChip`, contendo texto com a data do evento (`labelSmall`, cor `White`).
        - **Descrição:** Título, Local, Descrição curta (`maxLines = 2`, `overflow = Ellipsis`).
        - **Botão Seta (RF16.5):** `IconButton` com `Icons.AutoMirrored.Filled.ArrowForward` (cor `AzulPrimario`).

## 3. Lógica de Integração
- **Supabase:** Consultar tabela `eventos` com `select(*)`.
- **Filtro:** `viewModel.eventos.filter { it.tipo == filtroSelecionado || filtroSelecionado == null }`.
- **Navegação:** `navController.navigate("detalhes_evento/${evento.id}")`.
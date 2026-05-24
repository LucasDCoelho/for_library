# Plano de Implementação - RF10 (Tela de Leitor Digital)

## 1. Localização e Escopo
- **Módulo:** `feature/aluno`
- **Componentes:**
    - `app/src/main/java/com/br/unifor/for_library/feature/aluno/presentation/leitor/TelaLeitorDigital.kt`
    - `LeitorViewModel.kt` (Gerenciamento de cache do PDF, PdfRenderer e sincronização do ProgressoLeitura).

## 2. Lógica de Domínio e Dados (LeitorViewModel)
- **Estado (UI State):** `isLoading`, `pdfRenderer` (referência nativa), `paginaAtual`, `totalPaginas`, `overlayVisible`.
- **Ação Inicial:** Recebe o `livroId`. Verifica na tabela `progresso_leitura` qual a `paginaAtual`. Baixa o PDF a partir de `arquivoUrl` (tabela `livros`) para o `Context.cacheDir`. Instancia o `android.graphics.pdf.PdfRenderer`.
- **Auto-Save (RF10.5):** Método `salvarProgresso(pagina)` que realiza um `UPSERT` na tabela `progresso_leitura` do Supabase com os valores atualizados (`paginaAtual`, `porcentagemConclusao`).

## 3. Estrutura da Interface (Compose)
- **Container Raiz:** `Box(modifier = Modifier.fillMaxSize())`.
- **Renderização e Swipe (RF10.2, RF10.3):**
    - Implementar `HorizontalPager(state = pagerState, pageCount = totalPaginas)`.
    - Cada página extrai um `Bitmap` do `PdfRenderer.Page`, exibido através de um componente `Image`.
    - Usar `pointerInput(Unit) { detectTapGestures(onTap = { viewModel.toggleOverlay() }) }` sobre o Pager para alternar a visibilidade das barras de controle.
- **Barra Superior (Overlay - RF10.1):**
    - `AnimatedVisibility` ancorada no topo (`Alignment.TopCenter`).
    - `Surface` com elevação e fundo levemente transparente (ex: `White.copy(alpha = 0.9f)`).
    - `Row` com seta de voltar e Título do Livro.
- **Barra Inferior (Overlay - RF10.4):**
    - `AnimatedVisibility` ancorada na base (`Alignment.BottomCenter`).
    - `Surface` contendo uma `Column`.
    - Linha 1: Slider (sincronizado com `pagerState.currentPage`).
    - Linha 2: Seta Esquerda (`IconButton`), Texto da página ("{paginaAtual + 1} de {totalPaginas}"), Seta Direita (`IconButton`). As setas invocam `pagerState.animateScrollToPage()`.

## 4. Integração com Fim de Leitura (RF11)
- Lançar um `LaunchedEffect(pagerState.currentPage)`:
    - Se `pagerState.currentPage == totalPaginas - 1`, a leitura está 100% concluída.
    - O sistema marca o `status` em `progresso_leitura` como `CONCLUIDO` e aciona o callback que invocará o Popup de Fim de Leitura (RF11).

## 5. Integração Ciclo de Vida (Auto-save)
- Utilizar `DisposableEffect(Unit)` na raiz da Tela:
    - No bloco `onDispose`, invocar `viewModel.salvarProgresso(pagerState.currentPage)`. Garantindo que se o usuário destruir a tela pela seta ou via gestos do Android, a transação não seja perdida.
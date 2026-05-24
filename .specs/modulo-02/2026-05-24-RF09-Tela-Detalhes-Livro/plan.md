# Plano de Implementação - RF09 (Tela de Detalhes do Livro)

## 1. Localização e Escopo
- **Módulo:** `feature/aluno`
- **Caminho:** `app/src/main/java/com/br/unifor/for_library/feature/aluno/presentation/livro/TelaDetalhesLivro.kt`
- **ViewModel:** `DetalhesLivroViewModel` (Gerencia o carregamento de `Livro`, a lista de `Resenha` associada e o estado de Favoritado).

## 2. Estrutura do Layout (Compose)
O componente raiz será um `Scaffold` para estabilizar o cabeçalho.
- **TopBar (RF09.1):**
    - Usar um `Row` com `Modifier.fillMaxWidth()` e `height(56.dp)`.
    - Esquerda: `IconButton` com `Icons.AutoMirrored.Filled.ArrowBack` (retorna ao acervo).
    - Centro: Texto "Detalhes do livro" (`fontWeight = FontWeight.Bold`, `fontSize = 18.sp`).
    - Direita: `IconButton` com `Icons.Outlined.Notifications`.

- **Body (`LazyColumn`):**
    - Todo o conteúdo deve ser rolável. Adicionar `contentPadding = PaddingValues(16.dp)`.

## 3. Dissecação das Seções do LazyColumn

### 3.1. Cabeçalho do Livro (RF09.2 & RF09.3)
- **Capa Ampliada:** Reutilizar o componente `CapaLivro` (do `core/components/CapaLivro.kt`), passando um modificador de tamanho ampliado (ex: `height(240.dp)`) e alinhamento central.
- **Dados:**
    - Título (`titleLarge`, `SemiBold`, `AzulPrimario` ou `Color(0xFF212121)` para contraste forte).
    - Autor, Gênero, Ano (`bodyMedium`, cor `CinzaTexto`).
- **Rating:** `Row` com 5 ícones de estrela (amarelas se `notaMedia` cobrir, cinzas se vazio) e texto ao lado: "($qtdAvaliacoes avaliações)".

### 3.2. Área de Ações (RF09.4 & RF09.5)
- `Row` com `horizontalArrangement = Arrangement.spacedBy(12.dp)` no centro.
- **Botão Primário (RF09.4):**
    - `Button` preenchendo a maior parte da largura (`weight(1f)`).
    - Cor: `AzulPrimario`. Text: "Ler Agora".
    - Ação: Navega para a Rota do Leitor (RF10), passando o `livroId`.
- **Botão Secundário (RF09.5):**
    - **CORREÇÃO DE BUG:** O ícone de coração está reprovado na spec.
    - Usar `IconButton` ou `OutlinedButton` com o ícone `Icons.Outlined.BookmarkBorder` (se não salvo) ou `Icons.Filled.Bookmark` (se salvo).

### 3.3. Sinopse Expansível (RF09.6)
- Título da seção: "Sinopse" (`titleMedium`, `SemiBold`).
- Gerenciar estado local `var expandido by remember { mutableStateOf(false) }`.
- O Texto da sinopse usará `maxLines = if (expandido) Int.MAX_VALUE else 3`, com `overflow = TextOverflow.Ellipsis`.
- Botão "Ler mais" / "Ler menos" posicionado abaixo, usando texto em `AzulPrimario` e `clickable`.

### 3.4. Avaliações (RF09.7)
- Título da seção: "Avaliações de Usuários".
- **Correção de BUG (Textos quebrados):**
    - Para cada item na lista de resenhas (`uiState.resenhas`), renderizar um componente isolado `ResenhaItemCard`.
    - `ResenhaItemCard` estrutura: Um `Card` com borda leve (1dp, `#E0E0E0`) e fundo claro.
    - O texto da resenha *deve* ter `Modifier.fillMaxWidth()` para não vazar a tela horizontalmente e quebrar corretamente as linhas.
    - Formatação de Data Relativa: O ViewModel deve ter uma função utilitária que converta `LocalDateTime` de publicação em strings literais como "1 dia atrás", "2 dias atrás" baseado no `LocalDateTime.now()`, prevenindo bugs lógicos na UI.
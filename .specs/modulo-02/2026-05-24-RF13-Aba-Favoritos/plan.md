# Plano de Implementação - RF13 (Componente Aba Favoritos)

## 1. Localização e Escopo
- **Módulo:** `feature/aluno`
- **Arquivo Alvo:** `app/src/main/java/com/br/unifor/for_library/feature/aluno/presentation/estante/components/ItemLivroFavorito.kt`
- **Contexto:** Este componente será renderizado dentro do `LazyVerticalGrid` (Aba 1) do arquivo `TelaMinhaEstante.kt`.

## 2. Estrutura do Componente Compose (RF13.2)
- **Assinatura:** `@Composable fun ItemLivroFavorito(livro: Livro, onDesfavoritarClick: (Long) -> Unit)`
- **Contêiner Base:** `Card` com `shape = RoundedCornerShape(8.dp)` (seguindo Design System), `elevation = CardDefaults.cardElevation(2.dp)` e fundo claro.
- **Layout (Column):**
    - **Área da Capa (Box):**
        - `CapaLivro(url = livro.capaUrl)` ocupando a largura total do card e altura fixa (ex: `160.dp`).
        - *Sobreposto à capa (Alinhamento TopEnd):* `IconButton` com fundo translúcido (ex: `Surface` circular com 70% de opacidade) para garantir visibilidade.
    - **Área de Dados (Column interna):**
        - `Padding` de `8.dp`.
        - `Text` para Título (`titleMedium`, `SemiBold`, `maxLines = 1`, `overflow = TextOverflow.Ellipsis`).
        - `Text` para Autor (`bodySmall`, cor `CinzaTexto`, `maxLines = 1`).

## 3. Correção Crítica de UI (RF13.3)
- O `IconButton` sobreposto à capa NÃO usará `Icons.Filled.Favorite`.
- **Obrigatório o uso de:** `Icon(imageVector = Icons.Filled.Bookmark, tint = AzulPrimario, contentDescription = "Remover favorito")`.
- Ao ser clicado, executa `onDesfavoritarClick(livro.id)`.

## 4. Integração ViewModel
- Na `EstanteViewModel`, criar a função `removerFavorito(livroId: Long)`.
- **Ação Supabase:** Utilizar o cliente `gotrue-kt` para pegar o usuário atual, em seguida acessar `supabase.from("favoritos").delete { filter { eq("usuario_id", auth.uid); eq("livro_id", livroId) } }`.
- Atualizar a lista localmente filtrando o item removido para que a UI reaja na mesma hora, sem precisar fazer um novo `SELECT` pesado no banco.
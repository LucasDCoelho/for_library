# ADR 009 - Consolidação da Edição de Acervo (RF29)

- **Contexto:** RF29.2 e RF29.4 são redundantes, ambos lidando com a lógica de "Trocar Capa/Arquivo". O estado atual está ❌ (Não funcional) conforme `requirements_6.md`.
- **Decisão:**
    - Consolidar RF29.2 e RF29.4 em uma única diretriz: "A tela deve permitir a substituição seletiva de mídia (Capa ou PDF), utilizando o seletor de arquivos do sistema".
    - **Data Fetching:** A tela deve disparar um `Flow` de carregamento assim que o ID do livro for injetado via `NavHost` (ref: `design_6.md`)[cite: 4].
    - **Persistência:** Implementar a função `atualizarLivro()` no `LivroRepository`. A operação deve ser `PATCH`, enviando apenas os campos alterados para a tabela `livros` em `supabase_schema_6.md`[cite: 10].
- **Motivação:** Eliminar ambiguidade e garantir que a atualização da UI (`Preview da Capa`) seja sincronizada com o estado do banco.
- **Consequências:** Fim da redundância de código e otimização das chamadas de rede no Supabase.

## Plano de Implementação
1. **State:** Expandir `EditarLivroUiState` para contemplar `isLoading`, `isSuccess` e `errorMessage`.
2. **UI:**
    - `TelaEdicaoLivro.kt`: Reutilizar componentes do formulário de `RF28` (adição), mas com os campos pré-preenchidos.
    - Exibir a capa atual (ou placeholder) com botão de "Trocar Capa".
3. **Persistência:** O botão "Atualizar Obra" deve:
    - Verificar quais campos foram modificados.
    - Se a imagem for trocada, realizar upload para o bucket `imagens_livros` antes do UPDATE na tabela `livros`.
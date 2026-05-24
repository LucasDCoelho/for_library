# ADR 007 - Padronização de Navegação em Telas Admin (RF27)

- **Contexto:** RF27.1 exige um botão de seta para "voltar" em uma tela de listagem de acervo que já é parte do `AdminBottomBar`.
- **Decisão:** Remover o botão de voltar em `RF27` (e telas de listagem similares do módulo admin). A navegação deve ser regida exclusivamente pela `AdminBottomBar` definida em `design_system_5.md`[cite: 6].
- **Motivação:** Eliminar redundância, reduzir a carga cognitiva do administrador e evitar que a interface ocupe espaço vertical precioso.
- **Consequências:** Aumento da área de listagem de obras e conformidade com o design system unificado.

## Plano de Implementação
1. **Entidades:** Utilizar a data class `Livro` definida em `mapeamento_entidades_5.md` para mapear os dados da tabela `livros` (Supabase)[cite: 6, 10].
2. **ViewModel:** Criar `GestaoAcervoViewModel` injetando `LivroRepository`. Deve suportar:
    - `buscarLivros(query: String)`: Busca dinâmica.
    - `excluirLivro(id: Long)`: Chamada para RF30.
    - `contarLivros()`: Exibição da contagem total (RF27.2).
3. **UI:**
    - `LazyColumn` ou `LazyVerticalGrid` para listagem.
    - Componente de busca (`OutlinedTextField`) com placeholder "Busque por título ou autor".
    - `FloatingActionButton` (FAB) posicionado no canto inferior direito para RF28[cite: 9].
4. **Segurança (RLS):** Garantir que a query de busca no Supabase respeite as políticas de escrita restritas ao papel de Admin definidas em `auth_supabase_5.md`[cite: 4].
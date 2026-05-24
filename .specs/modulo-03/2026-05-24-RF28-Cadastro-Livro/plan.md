# ADR 008 - Implementação do Formulário de Cadastro de Acervo (RF28)

- **Contexto:** O RF28 é a porta de entrada para novos itens no sistema. A falha nesta camada resulta em dados inconsistentes no `supabase_schema`.
- **Decisão Arquitetural:**
    - **Formulário:** Utilizar `State Hoisting` com um `CadastroLivroState` que mantém o estado de preenchimento dos campos (`titulo`, `autor`, `isbn`, `descricao`, `categoria`).
    - **Upload de Mídia:** O upload da capa do livro deve ser feito via `Supabase Storage`. A UI deve mostrar um indicador de progresso (`CircularProgressIndicator`) durante o upload para evitar cliques duplicados.
    - **Validação:** Implementar uma camada de validação *client-side* com `Regex` para ISBN e obrigatoriedade de campos (`TextFormField.validator`).
- **Motivação:** Garantir que apenas dados validados cheguem ao banco de dados, protegendo a integridade referencial.
- **Consequências:** Redução drástica de erros de UI/UX e maior confiabilidade nas consultas de busca (RF27).

## Plano de Implementação
1. **ViewModel:** Criar `CadastroLivroViewModel` para gerenciar o `UiState` do formulário.
2. **UI:** Criar `TelaCadastroLivro.kt`.
    - Campos: `OutlinedTextField` com `isError` parametrizado.
    - Botão de Upload: Área clicável com `Box` para preview da imagem.
    - Botão de Salvar: Bloqueado (enabled = false) se a validação falhar.
3. **Persistência:** Implementar a função `adicionarLivro()` no `LivroRepository`, que deve:
    - Fazer upload da imagem para o bucket `imagens_livros`.
    - Inserir o registro na tabela `livros` com a URL pública da imagem.
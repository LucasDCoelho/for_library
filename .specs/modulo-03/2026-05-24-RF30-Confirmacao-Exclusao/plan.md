# ADR 010 - Exclusão de Obras e Integridade Referencial (RF30)

- **Contexto:** RF30 é a interface de confirmação para exclusão de obras (RF27). A exclusão é uma operação "hard-delete" no banco, exigindo cuidado com relacionamentos de chaves estrangeiras[cite: 9, 10].
- **Decisão Arquitetural:**
    - **Banco de Dados:** A tabela `livros` no `supabase_schema_8.md` possui relacionamentos com `favoritos`, `progresso_leitura` e `resenhas`. A exclusão deve ser tratada no nível do repositório ou via `Cascade Delete` no PostgreSQL para evitar erros de integridade (Foreign Key Violation)[cite: 10].
    - **Interface:** Utilizar um `AlertDialog` do Material 3 para o popup de confirmação (padrão `design_system_8.md`)[cite: 5].
    - **Comportamento:** Ao confirmar a exclusão (`RF30.3`), o sistema deve remover o registro da tabela `livros` e, opcionalmente, disparar uma `AtividadeAdmin` em `atividades_admin` registrando a remoção da obra por auditoria[cite: 6, 10].
- **Motivação:** Integridade referencial do banco de dados e auditoria de ações administrativas.
- **Consequências:** Remoção segura de registros sem deixar "ruínas" de dados no Supabase.

## Plano de Implementação
1. **Repository:** Adicionar método `deletarLivro(id: Long)` ao `LivroRepository`.
    - Implementar lógica que, ao deletar o livro, garante a limpeza das tabelas dependentes (ou via CASCADE).
2. **UI:** Criar `PopupConfirmacaoExclusao.kt`.
    - Deve receber o título do livro via parâmetro para exibir a mensagem personalizada (RF30.2).
    - Botão "Excluir" deve aplicar o estilo de cor `#D32F2F` (Erro/Destrutivo) conforme `design_system_8.md`[cite: 5].
3. **Fluxo:** Ao confirmar, disparar `ViewModel.deletarLivro(id)` e, no sucesso, navegar de volta ao `RF27` com atualização da lista.
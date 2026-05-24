# Protocolo de Validação - RF13

## Pré-requisitos
- Executar o app logado como Aluno.
- Adicionar pelo menos dois livros aos favoritos usando a tela de Acervo/Detalhes (RF09).
- Abrir a Tela "Minha Estante" e selecionar a aba "Favoritos".

## Casos de Teste Implacáveis

### 1. Auditoria Visual do Grid (RF13.2)
- **Ação:** Visualizar a aba de Favoritos.
- **Espera-se:** Os livros devem estar dispostos lado a lado (2 colunas), mostrando apenas a capa, título truncado e autor. Se o título do livro for gigantesco, ele deve apresentar "..." (ellipsis) e não quebrar o layout do cartão ou empurrar a altura do grid desproporcionalmente.

### 2. Validação da Correção de Bug do Ícone (RF13.3)
- **Ação:** Inspecionar os ícones nos cantos superiores direitos das capas.
- **Espera-se:** A presença de ícone de Coração é estritamente proibida. Deve haver o ícone `Bookmark` (marca-página) preenchido na cor `AzulPrimario` (#1565C0).

### 3. Teste de Exclusão Reativa
- **Ação:** Clicar no ícone de `Bookmark` de um dos livros da lista.
- **Espera-se:**
    1. O livro deve sumir imediatamente do Grid (resposta reativa da StateFlow).
    2. Inspecionando o banco de dados Supabase (tabela `favoritos`), a linha correspondente ao `usuario_id` e `livro_id` deve ter sido aniquilada com sucesso, mantendo a integridade referencial. Se o livro reaparecer após atualizar a tela, o `DELETE` falhou e a ViewModel está mentindo para o estado.
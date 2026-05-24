# Protocolo de Validação - RF08

## Pré-requisitos
- Executar o app e realizar login com uma conta de Aluno (domínio `@edu.unifor.br`).
- Navegar para a tela "Acervo" pela Bottom Navigation Bar.

## Casos de Teste

1. **Validação de Gatilho do Placeholder (RF08.2)**
    - **Ação:** Inserir uma string na Search Bar que não corresponda a nenhum livro do banco de dados (ex: "XkcdUnifor999").
    - **Espera-se:** O Grid de livros deve sumir. O componente de busca vazia deve assumir a tela. O Cabeçalho (com sino e foto) e a Bottom Bar devem permanecer intactos na tela.

2. **Validação Visual e Correção de Bug (RF08.3, RF08.4, RF08.5)**
    - **Ação:** Observar a tela de busca vazia.
    - **Espera-se:**
        - A ilustração do livro vazio deve ser exibida e centralizada.
        - O texto "Ops! Silêncio na biblioteca. Nenhum livro encontrado para esta pesquisa" deve estar legível, sem quebras incorretas (Validando a correção do bug apontado por Miguel).
        - O texto secundário "Tente usar palavras-chave diferentes ou verifique a ortografia." deve estar renderizado na cor cinza (`CinzaTexto`).

3. **Validação de Recuperação de Estado (RF08.5 / RF08.6)**
    - **Ação:** Clicar no botão "Limpar filtros".
    - **Espera-se:** A query da Search Bar e os filtros ativos devem ser limpos. O sistema deve re-consultar o acervo (tabela `livros` no Supabase) e renderizar novamente o Grid original (RF06.5) contendo todos os livros.
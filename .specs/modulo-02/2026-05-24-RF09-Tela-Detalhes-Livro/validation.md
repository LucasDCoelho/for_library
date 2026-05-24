# Protocolo de Validação e Homologação - RF09

## Pré-requisitos
- O banco de dados do Supabase deve possuir ao menos 1 registro válido na tabela `livros` e 2 registros na tabela `resenhas` associados a ele, com status "APROVADA".
- Acessar o sistema com perfil válido e clicar em um livro pelo Acervo (RF06).

## Testes de Aceitação e Resolução de Bugs

### 1. Teste de Identidade Visual e Bug do Ícone (RF09.2, RF09.3, RF09.5)
- **Ação:** Inspecionar a camada principal de dados logo abaixo da TopBar.
- **Passos:**
    1. Verificar se a capa ocupa posição de destaque e carrega corretamente o componente unificado `CapaLivro`.
    2. Verificar as estrelas e a quantidade de avaliações.
    3. **Checagem de Correção:** Procurar o botão de Favoritar ao lado do "Ler Agora". **Nenhum ícone de coração deve ser visível.** O botão deve obrigatoriamente exibir um ícone de marca-página (`Bookmark`). Clicar nele para garantir que o estado visual muda (preenchido/vazio).

### 2. Teste da Expansão Textual (RF09.6)
- **Ação:** Interagir com a sinopse do livro.
- **Passos:**
    1. Se a sinopse for extensa, garantir que ela trunca na terceira linha.
    2. Clicar no botão "Ler mais". A caixa de texto deve empurrar o resto do conteúdo para baixo fluidamente (graças ao `LazyColumn`).
    3. Clicar em "Ler menos" para retrair a área.

### 3. Teste de Resolução de Bug nas Avaliações (RF09.7)
- **Ação:** Rolar a tela até o final, na seção "Avaliações de Usuários".
- **Passos:**
    1. Verificar se as resenhas não ultrapassam a margem direita da tela (validando a correção de estouro de layout).
    2. Analisar o texto da data. Ele **não** pode ser um timestamp bruto. Deve estar parseado logicamente como "1 dia atrás" ou equivalente.

### 4. Teste de Navegação Opcional (RF09.4)
- **Ação:** Clicar no botão primário azul "Ler Agora".
- **Espera-se:** O `NavHost` deve empilhar a rota referente à Tela de Leitor Digital (RF10), carregando a referência do PDF atual associado a este ID.
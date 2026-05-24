# Validação de Requisitos: RF07 - Popup (Bottom Sheet) de Filtros Avançados

## 1. Critérios de Aceite (CA)
* **CA 01:** A *Bottom Sheet* deve ser exibida fluidamente a partir da base da tela ao clicar no ícone de "Filtros" na barra de busca (RF06.3).
* **CA 02:** O cabeçalho deve exibir o título "Filtros Avançados" com clareza e possuir um botão com ícone de "X" operante no canto direito.
* **CA 03:** A janela modal deve ser encerrada e removida da tela com sucesso ao utilizar a ação do ícone de "X" ou através do gesto de deslizar a tela para baixo (*swipe*).
* **CA 04:** A interface deve apresentar uma seção intitulada "GÊNERO LITERÁRIO" contendo *Chips* das categorias com possibilidade de seleção e mudança de cor ao toque.
* **CA 05:** Deve ser exibida uma lista de *RadioButtons* com as opções exatas: "A-Z", "Z-A", "Mais Recentes" e "Melhor Avaliados", permitindo apenas uma escolha por vez.
* **CA 06:** O acionamento do botão "Limpar" deve, de forma reativa, anular todas as escolhas visuais do painel, retornando ao estado zerado.
* **CA 07:** O acionamento do botão "Aplicar Filtros" deve fechar automaticamente o componente Modal e invocar a atualização em tempo real do *Grid* (2 colunas) de livros no Acervo Digital.

## 2. Roteiro de Testes

1. Autentique-se como Aluno através da Tela de Login.
2. Utilize a *Bottom Navigation Bar* para navegar até a interface principal do **Acervo Digital**.
3. Localize e toque no ícone de **Filtros**, situado ao lado da barra central de pesquisa.
4. **Inspeção de Interface (UI):** Valide visualmente a presença do título da tela ("Filtros Avançados") e se o ícone faltante de "X" foi corretamente renderizado.
5. **Teste de Cancelamento:** Clique no ícone de "X". O popup deve fechar. Reabra-o e teste o fechamento utilizando o dedo para arrastar o painel para baixo (*swipe*).
6. **Teste de Estado (Limpar):** Com o popup aberto, interaja marcando um *Chip* (ex: "Tecnologia") e uma ordem (ex: "Z-A"). Em seguida, clique em **Limpar**. Confirme se a interface removeu todas as seleções ativas.
7. **Teste de Ação Primária (Aplicar):** Marque filtros de sua preferência. Clique no botão azul primário **Aplicar Filtros**. Valide se o popup fecha sozinho e se a lista de capas do acervo é reconstruída refletindo a busca do banco de dados.
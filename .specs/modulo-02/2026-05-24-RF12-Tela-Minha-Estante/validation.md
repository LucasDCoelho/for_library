# Protocolo de Validação e Auditoria - RF12 e RF13

## Pré-requisitos
- O banco de dados do Supabase (`progresso_leitura` e `favoritos`) deve conter registros atrelados ao `auth.uid()` do aluno logado. Para testes, insira um livro lido pela metade e um livro favoritado.
- Iniciar o app e navegar pela Bottom Navigation Bar até a aba "Estante".

## Casos de Teste Implacáveis

### 1. Auditoria da TopBar e Tabs (RF12.1 e RF12.2)
- **Ação:** Observar o cabeçalho.
- **Espera-se:** O título deve ser exatamente "Minha Estante". A lupa NÃO deve existir (código morto erradicado). O ícone de relógio deve estar presente e, ao clicar, empilhar a Rota do Histórico de Leitura. As abas "Lendo" e "Favoritos" devem estar acessíveis para toque.

### 2. Validação Matemática da Barra de Progresso (RF12.3)
- **Ação:** Com a aba "Lendo" ativa, inspecionar o cartão de um livro que está em leitura.
- **Espera-se:** O `LinearProgressIndicator` deve apresentar o preenchimento proporcional exato do banco. Exemplo: Se o `Livro.totalPaginas` é 300 e a `ProgressoLeitura.paginaAtual` é 150, a barra deve estar preenchida cravada em 50%. O texto auxiliar inferior deve exibir "150 / 300 págs". O componente não pode estourar os limites laterais do card (`Modifier.fillMaxWidth()`).

### 3. Validação do Grid e Supressão de Bug Visual (RF13.2 e RF13.3)
- **Ação:** Clicar na aba "Favoritos".
- **Espera-se:** O layout deve transmutar violentamente de Lista Vertical para um Grid de 2 colunas.
- **Auditoria do Ícone (Bugfix):** A presença de qualquer ícone de "Coração" reprova o teste imediatamente. O botão de remoção no canto superior direito de cada capa DEVE ser um `Bookmark` preenchido.
- **Ação 2:** Clicar no botão `Bookmark` em um dos itens.
- **Espera-se:** A entidade `Favorito` deve ser deletada do Supabase e a `StateFlow` reativa deve remover a carta do Grid em tempo real, sem necessidade de *pull to refresh*.
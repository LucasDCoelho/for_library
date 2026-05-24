# Plano de Implementação: RF05 - Tela Home do Aluno

## 1. Visão Geral
A tela Home do Aluno atua como o dashboard principal da jornada acadêmica e pertence ao módulo `feature/aluno/`. Ela consolida informações de perfil, progresso de leitura e destaques do acervo.

## 2. Requisitos Associados
* O sistema deve exibir um cabeçalho com a foto em miniatura do perfil, a saudação "Olá, [Nome]!" e a quantidade de pontos atuais de gamificação.
* O sistema deve redirecionar o usuário para Meus Pontos (RF21) ao clicar na área de pontos.
* O sistema deve exibir um símbolo de sino que redireciona o usuário para a tela de notificações.
* O sistema deve conter um card "Continue Lendo" exibindo a capa, título do livro, nome do autor, capítulo atual e uma barra de progresso (em %) do último livro aberto.
* O sistema deve redirecionar para o Leitor (RF10) ao clicar no card "Continue Lendo".
* O sistema deve exibir um carrossel horizontal com o título "Destaques do Acervo" mostrando capa, título e autor.
* O sistema deve redirecionar o usuário para a tela de acervo digital (RF06) ao clicar no texto "ver todos".
* O sistema deve exibir um ícone de "bookmark" no canto superior direito dos livros salvos no carrossel.
* O sistema deve exibir um card "LIVROS LIDOS" informando a quantidade de livros lidos.
* O sistema deve exibir um card "TEMPO TOTAL" informando o tempo de leitura em horas.
* O sistema deve conter um botão com ícone de robô que acione o PopUp do Chat Bot (RF41).
* O sistema deve possuir uma Bottom Navigation Bar fixa com ícones para: Home, Acervo, Estante, Eventos e Perfil.

## 3. Abordagem Técnica
* **Interface (UI):** Utilização de Jetpack Compose com componentes `MaterialTheme` e cores de `core/designsystem/Color.kt`. A barra inferior utilizará o componente centralizado `ForLibraryBottomBar.kt`.
* **Imagens:** As capas dos livros devem ser renderizadas utilizando a biblioteca assíncrona Coil.
* **Dados (Supabase):** * Os dados do usuário (nome, foto_perfil, pontos_gamificacao) serão consultados na tabela `usuarios`.
    * O card "Continue Lendo" consultará a tabela `progresso_leitura` relacionando com `livros`.
    * O carrossel de destaques fará uma query na tabela `livros`.
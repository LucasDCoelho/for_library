# Plano de Implementação: RF06 - Tela de Acervo Digital

## 1. Visão Geral
A tela de Acervo Digital é o catálogo principal onde o aluno pode buscar, filtrar e explorar os livros disponíveis na base de dados.

## 2. Requisitos Associados
* O sistema deve exibir um cabeçalho com a foto de perfil, o título "Acervo Digital" e um símbolo de sino para notificações.
* O sistema deve conter uma Search Bar no topo com o placeholder "Busque por título ou autor".
* O sistema deve conter um botão com ícone de "Filtros" que abre o Popup de Filtros Avançados (RF07).
* O sistema deve exibir filtros genéricos (ex: Tudo, Ficção, Tecnologia, História, Design) logo abaixo da search bar.
* O sistema deve listar os livros em formato de Grid de 2 colunas, exibindo capa, título e autor.
* O sistema deve exibir uma label "NOVO" no canto inferior direito de livros inseridos recentemente.
* O sistema deve redirecionar para a Tela de Detalhes do Livro (RF09) correspondente ao livro clicado.
* O sistema deve exibir a Bottom Navigation Bar fixa.

## 3. Abordagem Técnica
* **Interface (UI):** O Grid será implementado via `LazyVerticalGrid` utilizando o `contentPadding` típico de `16dp`. O componente `CapaLivro.kt` será reutilizado para exibir as capas via OpenLibrary.
* **Dados (Supabase):** As buscas devem interagir com a tabela `livros`, filtrando por `titulo`, `autor` ou `genero`. O identificador do livro clicado será repassado para a rota da tela de detalhes.
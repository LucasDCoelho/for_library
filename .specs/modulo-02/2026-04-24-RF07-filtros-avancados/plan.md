# Plano de Implementação: RF07 - Popup (Bottom Sheet) de Filtros Avançados

## 1. Visão Geral
O objetivo é corrigir e finalizar a lógica e a interface do componente de Filtros Avançados (`FiltroAvancadoBottomSheet`), que é invocado dentro da Tela de Acervo Digital do aluno. O componente base já existe arquiteturalmente no caminho `core/components/FiltroAvancado.kt`, mas necessita de correções visuais e integração de estado para funcionar corretamente.

## 2. Diretrizes Técnicas e de UI
* **Estrutura Base:** Utilizar o componente declarativo `ModalBottomSheet` do Material 3 no Jetpack Compose.
* **Design System:** O componente deve respeitar o raio de canto (shape) de `20dp` para o topo do *bottom sheet*. O padrão de cor do botão de confirmar/aplicar deve usar o token primário (AzulPrimario `#1565C0`).
* **Arquitetura:** Como o componente reside na pasta `core/`, ele não deve importar regras de negócio diretamente de `feature/aluno`. O estado e as ações devem ser repassados via parâmetros de função (*callbacks*) para o `AcervoViewModel`.

## 3. Escopo de Desenvolvimento (Passo a Passo)

* **Passo 1: Correção do Cabeçalho e Ações de Fechamento (RF07.1)**
    * Adicionar o título "Filtros Avançados" no topo do componente.
    * Implementar o ícone de "X" no canto superior direito para fechamento, sanando o bug apontado no levantamento.
    * Garantir que o componente nativo já suporte o fechamento automático por gesto de arrastar para baixo (*swipe*).

* **Passo 2: Implementação da Seção "GÊNERO LITERÁRIO" (RF07.2)**
    * Renderizar a tipografia do título da seção em letras maiúsculas (caps).
    * Adicionar um agrupamento de *Chips* selecionáveis contendo as opções institucionais: Ficção, Acadêmico, Tecnologia, Biografia, História e Design.

* **Passo 3: Implementação da Seção de Ordenação (RF07.3)**
    * Adicionar botões de rádio (*RadioButtons*) em lista vertical para definir o método de ordenação dos livros.
    * As opções estritas devem ser: "A-Z", "Z-A", "Mais Recentes" e "Melhor Avaliados".

* **Passo 4: Ações de Rodapé e Lógica de Estado (RF07.4 e RF07.5)**
    * **Botão "Limpar":** Conectar este botão a um método que reseta o estado interno de variáveis locais para os valores padrão (desmarcar *Chips* e *RadioButtons*).
    * **Botão "Aplicar Filtros":** Resolver a pendência ("FALTA APLICAR FILTRO"). Ao ser clicado, este botão deve invocar a ação de fechar o popup e disparar um evento para a tela pai (Acervo) enviar a query atualizada para o banco de dados.
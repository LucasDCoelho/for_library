# Protocolo de Validação e Homologação - RF11

## Pré-requisitos
- Sistema deve estar autenticado com perfil de aluno.
- Abrir um livro na tela de leitor (RF10) que não esteja marcado como concluído no Supabase.

## Casos de Teste Implacáveis

### 1. Teste de Gatilho e Correção de Bug Textual (RF11.1 e RF11.2)
- **Ação:** No leitor, deslize ou avance as páginas até encostar na última folha do PDF.
- **Espera-se:** O gatilho de detecção da última página deve suspender o input primário do leitor e renderizar automaticamente o componente `PopupFimLeitura` sobre o PDF.
- **Validação Crítica:** O texto central "Parabéns! Você concluiu a leitura e ganhou X pontos." NÃO deve conter o caractere genérico "X". Deve refletir explicitamente a quantidade de recompensa de `ConfiguracaoSistema` (ex: "ganhou 50 pontos").

### 2. Teste de Identidade e Layout (RF11.5)
- **Ação:** Observar a estrutura do Popup aberto.
- **Espera-se:** A label com os dizeres "Leitura finalizada" deve estar no cabeçalho ou imediatamente abaixo do ícone de livro ilustrativo. Os cantos do popup devem obedecer aos `16.dp` ditados pelo Design System para janelas flutuantes.

### 3. Teste de Redirecionamento - Fluxo Positivo (RF11.3)
- **Ação:** Clicar no botão azul primário "Avaliar Livro".
- **Espera-se:** O leitor atual deve ser desmontado de forma limpa da stack (`onDispose` disparado) e o `NavHost` deve empilhar a Tela de Resenha / Avaliação (RF14) com o ID deste livro específico previamente injetado.

### 4. Teste de Interrupção - Fluxo Negativo (RF11.4)
- **Ação:** Refazer o cenário (fechar a tela de avaliação, entrar no livro novamente, que agora deve abrir direto na última página ou requerer reset). O Popup abrirá. Clique em "Fechar".
- **Espera-se:** O leitor digital será encerrado e o controlador de navegação fará o PopBackStack, retornando estritamente à Tela de Detalhes do Livro de onde o aluno partiu (RF09). A UI não pode engasgar nem travar na última página.
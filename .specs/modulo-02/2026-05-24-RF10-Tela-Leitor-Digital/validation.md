# Protocolo de Validação e Auditoria - RF10

## Pré-requisitos
- Um registro na tabela `livros` com um `arquivoUrl` apontando para um PDF funcional hospedado no Supabase Storage ou serviço remoto acessível.
- Estar logado com perfil de Aluno.
- Clicar em "Ler Agora" no Detalhes do Livro (RF09).

## Casos de Teste Implacáveis

### 1. Teste de Renderização e Carregamento Seguro (RF10.2)
- **Ação:** Iniciar a abertura do leitor digital.
- **Espera-se:** Uma tela de *loading* visual até que o PDF seja colocado em cache. Após isso, o leitor deve abrir ocupando 100% da tela (sem barras de navegação padrão atrapalhando a leitura). A primeira página do PDF deve renderizar nitidamente sem distorções severas. O erro de "LIVRO ERRADO" deve estar sanado, correspondendo o arquivo aberto ao ID do livro clicado.

### 2. Teste de Imersão e Controles (RF10.3 e RF10.4)
- **Ação 1 (Swipe):** Deslizar o dedo horizontalmente para a esquerda.
- **Espera-se:** A página deve transitar de forma fluida para a página 2 (RF10.3).
- **Ação 2 (Overlays):** Tocar rapidamente no meio da tela do documento.
- **Espera-se:** Devem surgir fluidamente as barras de controle. A barra superior com o título, e a inferior informando "Página 2 de X". O Slider deve refletir fisicamente a proporção lida.
- **Ação 3 (Paginação via Setas):** Na barra inferior, tocar na seta para a direita.
- **Espera-se:** O leitor deve avançar para a página 3 através de uma animação, e os contadores numéricos devem atualizar em tempo real.

### 3. Auditoria de Auto-save no Banco de Dados (RF10.5)
- **Ação:** Estando na página 5, tocar na seta de "voltar" do cabeçalho do leitor, retornando à tela anterior (RF09).
- **Espera-se:** O painel do banco de dados (tabela `progresso_leitura`) deve ser inspecionado imediatamente. O campo `pagina_atual` deve estar cravado em 5, e a `porcentagem_conclusao` atualizada proporcionalmente.
- **Segunda Ação:** Clicar novamente em "Ler Agora".
- **Espera-se:** O livro não pode abrir na página 1. Deve carregar a engine do PDF e pular automaticamente para a página 5.

### 4. Gatilho do Fim de Leitura (Trigger RF11)
- **Ação:** Usar o slider da barra inferior para pular diretamente para a última página do PDF.
- **Espera-se:** Ao renderizar a última página, o componente deve interceptar o término, atualizar o status da entidade `ProgressoLeitura` para `CONCLUIDO` e acionar o surgimento imediato da interface de "Parabéns!" (Popup do Fim de Leitura - RF11).
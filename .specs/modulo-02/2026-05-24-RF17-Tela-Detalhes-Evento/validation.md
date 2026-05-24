# Protocolo de Validação - RF17

## Casos de Teste

1. **Validação de Renderização de Dados (RF17.2, RF17.3, RF17.4)**
    - **Ação:** Abrir um evento pelo catálogo (RF16).
    - **Espera-se:**
        - Data e Hora formatadas corretamente com a string "(GMT-3)".
        - Local e complemento exibidos de forma clara.
        - Texto "Sobre o evento" em negrito, seguido pelo texto descritivo.

2. **Validação de Feedback do Calendário (RF17.5)**
    - **Ação:** Clicar no botão "Adicionar ao Meu Calendário".
    - **Espera-se:**
        1. O seletor de calendário/agenda nativa do Android deve abrir.
        2. Após o usuário confirmar a inserção e retornar ao app, um `Snackbar` com fundo verde (sucesso) deve ser exibido na parte inferior da tela com o texto "Evento adicionado ao calendário!". Se este snackbar não aparecer, o requisito RF17.5 está reprovado.

3. **Validação de Navegação (RF17.1)**
    - **Ação:** Clicar na seta de voltar.
    - **Espera-se:** Deve retornar exatamente para a lista de eventos (RF16) mantendo o estado dos filtros aplicados anteriormente.
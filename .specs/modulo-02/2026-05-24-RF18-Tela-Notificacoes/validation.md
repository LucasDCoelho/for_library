# Protocolo de Validação - RF18

## Casos de Teste

1. **Validação do Círculo Azul e Fundo (RF18.5, RF18.6)**
    - **Ação:** Receber uma nova notificação.
    - **Espera-se:** Notificação aparece no topo com fundo azulado e círculo azul visível.
    - **Ação:** Tocar na notificação.
    - **Espera-se:** O fundo muda para branco e o círculo azul desaparece instantaneamente.

2. **Validação de Persistência (RF18.6)**
    - **Ação:** Fechar o app, reabrir e navegar para a tela de notificações.
    - **Espera-se:** A notificação clicada anteriormente deve permanecer marcada como lida (fundo branco). Se voltar a ficar azul, o `UPDATE` no Supabase falhou.

3. **Validação de Ordenação e Agrupamento (RF18.3, RF18.4)**
    - **Ação:** Enviar notificações com datas diferentes (hoje, ontem, 3 dias atrás).
    - **Espera-se:** As labels "Hoje", "Ontem" e "Anteriores" devem aparecer exatamente nesta ordem, separando corretamente os itens.
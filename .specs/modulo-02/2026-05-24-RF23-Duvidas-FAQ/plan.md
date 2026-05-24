# ADR XXX - Refatoração do Módulo de FAQ (RF23)

- **Contexto:** A tela de FAQ (RF23) possui inconsistências de texto no título e falta de conteúdo nas respostas do accordion (RF23.3)[cite: 8].
- **Decisão:** Refatorar o componente de Accordion para aceitar uma lista de Data Classes, garantindo que o conteúdo seja injetado dinamicamente e que o texto do cabeçalho siga a especificação exata.
- **Motivação:** Eliminar bugs de texto relatados em requirements.md e padronizar a experiência de suporte.
- **Consequências:** Melhoria imediata na UX e conformidade total com RF23.1 e RF23.3.

## Plano de Implementação
1. **Correção RF23.1:** Alterar o `Text` do header para "Dúvidas Frequentes" (corrigindo o typo "Frenquentes") e garantir a navegação de retorno.
2. **Implementação RF23.3:** Criar a estrutura `data class FAQItem(val pergunta: String, val resposta: String)`.
3. **UI:** Utilizar `LazyColumn` com `ExpandableCard` (componente reutilizável padrão) mapeando a lista de `FAQItem`.
4. **Design System:** Aplicar `MaterialTheme.typography.titleMedium` para perguntas e `bodyMedium` para respostas, respeitando o espaçamento de 16dp.
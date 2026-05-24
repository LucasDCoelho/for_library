# ADR 012 - Formulário Unificado de Eventos (RF32)

- **Contexto:** RF32 lida com a entrada de dados para eventos (Criação e Edição). A duplicação entre estas telas causaria inconsistências.
- **Decisão Arquitetural:**
    - **Formulário Unificado:** Utilizar um `EventFormState` que se comporta de forma diferente dependendo da injeção de um `eventId`. Se nulo, é criação (POST); se presente, é edição (PATCH).
    - **Validação:** Implementar uma camada de validação robusta para `dataInicio` (deve ser > data atual) e `duracao` (deve ser > 0).
    - **Integração:** `Supabase` será acionado via `EventRepository` usando o método `upsert` (ou lógica separada `insert/update` para maior controle de erros).
- **Motivação:** Manter o princípio DRY (Don't Repeat Yourself) e garantir que as regras de negócio de datas sejam idênticas em ambos os modos.
- **Consequências:** UI consistente e manutenção simplificada.

## Plano de Implementação
1. **ViewModel:** Criar `EventosFormViewModel`.
    - Gerenciar o estado do formulário e o `LoadingState` para feedback visual durante o envio.
2. **UI:** Criar `TelaCadastroEvento.kt`.
    - Campos: `titulo`, `descricao`, `data`, `tipo` (Dropdown/Radio) e `local/link`.
    - DatePicker: Utilizar `MaterialDatePicker` para garantir que o usuário não insira datas em formato inválido.
3. **Persistência:** O botão "Salvar" deve disparar a validação antes de invocar o `repository.saveEvent(event: Evento)`.
4. **Navegação:** Após persistência, redirecionar automaticamente para `RF31`.
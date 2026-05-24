# ADR 011 - Refatoração da Gestão de Eventos (RF31)

- **Contexto:** A listagem de eventos (RF31) apresenta erros de contagem e falta de funcionalidade nas ações de editar/apagar (RF31.2).
- **Decisão Arquitetural:**
    - **ViewModel:** Implementar `GestaoEventosViewModel` consumindo a tabela `eventos` definida em `supabase_schema_9.md`.
    - **Data Layer:** Utilizar o repositório de eventos para recuperar a lista completa e aplicar a lógica de contagem correta na `Home` do Administrador.
    - **UI:** Utilizar `LazyColumn` para a listagem (RF31.2) com `Card` customizado seguindo o `design_system_9.md`[cite: 6].
    - **Ações:** O clique no ícone de "lixeira" deve redirecionar para `RF30` (adaptação do contexto de exclusão para eventos), garantindo que a exclusão seja irreversível e auditada.
- **Motivação:** Restaurar a operacionalidade da gestão de eventos e corrigir o bug de contagem (RF31.1)[cite: 9].
- **Consequências:** Painel de gestão funcional e auditoria consistente.

## Plano de Implementação
1. **ViewModel:** Criar `GestaoEventosViewModel`.
    - Adicionar função `fetchEventos()` para listar todos os eventos.
    - Adicionar função `deletarEvento(id: Long)` vinculada à lógica de deleção do Supabase.
2. **UI:** Criar `TelaGestaoEventos.kt`.
    - Header: Título "Gestão de Eventos" + Contador real de itens.
    - Listagem: Cards exibindo `titulo`, `dataInicio` (formatada), `tipo` e botões de `Editar` (Lápis) e `Excluir` (Lixeira).
    - FAB: Botão "+" para `RF32` (Criação).
3. **Persistência:** Validar se a exclusão via `RF30` está integrada ao contexto de eventos, conforme o mapeamento em `mapeamento_entidades_9.md`[cite: 6].
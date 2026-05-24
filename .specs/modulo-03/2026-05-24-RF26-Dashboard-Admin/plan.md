# ADR 006 - Implementação e Integração do Dashboard Admin (RF26)

- **Contexto:** A tela de Dashboard (RF26) está em estado ❌ (Não funcional) quanto à exibição de métricas e atividades recentes, servindo apenas como mockup estático.
- **Decisão Arquitetural:**
    - **Data Fetching:** Implementar um `AdminDashboardViewModel` que utiliza o `SupabaseRepository` para realizar consultas agregadas (COUNT) nas tabelas `livros`, `usuarios`, `resenhas` e `obras_autorais` (`supabase_schema_4.md`)[cite: 10].
    - **Visualização:** Utilizar um Grid (Row/Column) para os 4 cards de resumo numérico. Cada card deve herdar a estilização de `design_system_4.md`, com o card de "Resenhas Pendentes" forçando a cor `#D32F2F` (Erro/Destrutivo) e "Obras Pendentes" `#2E7D32` (Sucesso).
    - **Histórico:** Implementar uma `LazyColumn` para listar `AtividadeAdmin` (conforme `mapeamento_entidades_4.md`), ordenando por `data_atividade` DESC[cite: 6].
- **Motivação:** Garantir visibilidade operacional para o administrador (RF26.3, RF26.4)[cite: 9].
- **Consequências:** O sistema passa a ter um feedback visual real da saúde dos dados.

## Plano de Implementação
1. **ViewModel:** Criar `AdminDashboardViewModel` com `StateFlow<DashboardUiState>`.
2. **Consultas:** Executar `SELECT COUNT(*)` nas tabelas de origem para popular o resumo (RF26.3).
3. **UI:**
    - Implementar componentes `DashboardCard` reutilizáveis.
    - Implementar `AtividadeItem` com ícone dinâmico baseado em `icone_referencia` (`AtividadeAdmin`)[cite: 6].
4. **Navegação:** Garantir que o botão engrenagem leve para `RF40` e o sino para notificações.
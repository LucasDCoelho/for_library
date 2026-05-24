# ADR 013 - Hub de Moderação e Centralização de Rotas (RF33)

- **Contexto:** RF33 é o *Hub Central* para todas as operações de moderação (RF34, RF36, RF38). A navegação deve ser segura e centralizada.
- **Decisão Arquitetural:**
    - **Navegação:** O `HubModeracaoViewModel` não deve conter lógica de negócio pesada, servindo apenas como um mediador de estado para a navegação (`NavHost` do Admin).
    - **Segurança:** O acesso às rotas filhas (`Moderação de Usuários`, `Obras`, `Resenhas`) deve ser validado via RLS (Row Level Security) diretamente no banco de dados Supabase, garantindo que usuários com `tipo = ALUNO` não consigam acessar essas rotas, mesmo que tentem forçar a navegação via URL (deep link).
- **Motivação:** Centralizar o ponto de entrada administrativo, facilitando a auditoria de acesso e garantindo um *UX* consistente.
- **Consequências:** Isolamento das telas de moderação do fluxo principal do aluno, aumentando a segurança do sistema.

## Plano de Implementação
1. **ViewModel:** Criar `ModeraçãoViewModel` (vazio no momento, mas pronto para escalar).
2. **UI:** Criar `TelaModeração.kt`.
    - **Componentes:** Utilizar `Card` customizado para cada sub-módulo. Cada card deve conter ícone, título e indicação de quantidade (ex: "X Resenhas pendentes").
    - **Layout:** `Column` vertical com espaçamento de 16dp entre os cards, conforme `design_system_12.md`.
3. **Integração:** Adicionar as rotas `ModeraçãoUsuarios`, `ModeraçãoObras`, `ModeraçãoResenhas` ao `NavHost` do Admin.
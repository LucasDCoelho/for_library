# Validação do Requisito RF31

## Critérios de Aceitação (Baseado em `requirements_9.md`)

1. **Header (RF31.1):**
    - [ ] Título "Gestão de Eventos" exibido corretamente.
    - [ ] Contador de eventos reflete o número real de registros na tabela `eventos` (corrigindo o bug atual).
2. **Listagem e Ações (RF31.2):**
    - [ ] Cada card exibe: Título, Data, Horário e Tipo (`TipoEvento` da entidade `Evento` em `mapeamento_entidades_9.md`)[cite: 6].
    - [ ] Ícone de lápis redireciona para a tela de edição (`RF32`).
    - [ ] Ícone de lixeira redireciona para popup de exclusão (`RF30`).
3. **Navegação (RF31.2):**
    - [ ] FAB "+" (Adição) redireciona corretamente para a tela de criação (`RF32`).
4. **Design System:**
    - [ ] Coerência visual com os cards de moderação em `design_system_9.md`[cite: 6].
    - [ ] Bottom Bar do Administrador persistente.
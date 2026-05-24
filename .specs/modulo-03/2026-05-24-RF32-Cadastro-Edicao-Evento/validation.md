# Validação do Requisito RF32

## Critérios de Aceitação (Baseado em `requirements_10.md`)

1. **Validação de Campos (RF32.1):**
    - [ ] Campos obrigatórios (`titulo`, `data`, `descricao`) validados.
    - [ ] Feedback de erro (`TextFormField` com `isError`) ativado.
2. **Calendário e Datas (RF32.2):**
    - [ ] `MaterialDatePicker` restrito para não permitir datas passadas (para novos eventos).
    - [ ] Conversão correta de formato de data (`ISO-8601`) para o banco de dados.
3. **Persistência e Fluxo (RF32.3):**
    - [ ] Botão "Salvar" bloqueado enquanto houver campos vazios ou erros.
    - [ ] Confirmação visual ("Evento criado com sucesso" ou "Evento atualizado") após persistência.
4. **Design System:**
    - [ ] Uso de `OutlinedTextField` com espaçamentos conforme `design_system_10.md`.
    - [ ] Coerência com a paleta de cores para estados de erro e sucesso.
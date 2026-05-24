# Plano de Validação: RF36

## Critérios de Aceitação
- [ ] **Renderização:** A lista exibe corretamente obras com status 'Pendente'.
- [ ] **Badges:** O status 'Pendente' deve aparecer com a cor de fundo amarela, conforme o feedback da equipe[cite: 9].
- [ ] **Navegação:** O botão "Revisar" deve passar o `obra_id` via parâmetro de navegação para a tela RF37.
- [ ] **Info:** O card "TOTAL PENDENTE" deve atualizar dinamicamente com o tamanho da lista.

## Portões de Qualidade (Quality Gates)
- **Gate 1:** A consulta ao banco deve respeitar a RLS (Role Admin)[cite: 10].
- **Gate 2:** Não deve haver dependência direta com o módulo de aluno[cite: 4].
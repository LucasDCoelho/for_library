# Validação do Requisito RF27

## Critérios de Aceitação (Baseado em `requirements_5.md`)[cite: 9]

1. **Header e Ações (RF27.1, RF27.2):**
    - [ ] Título "Gestão de Acervo" exibido (sem botão de voltar redundante, conforme ADR 007).
    - [ ] Campo de busca funcional, filtrando o acervo em tempo real.
    - [ ] Exibição da quantidade total de livros encontrados.
2. **Listagem (RF27.3):**
    - [ ] Cada item exibe Capa, Título e Autor corretamente.
    - [ ] Ícones de "Editar" e "Excluir" renderizados.
    - [ ] Ação de excluir dispara o popup `RF30`.
3. **Fluxos de Navegação (RF27.4, RF27.5):**
    - [ ] Ação "Editar" redireciona para `RF29` (Passando o ID do livro).
    - [ ] FAB "+" redireciona para `RF28`.
4. **Design System:**
    - [ ] Utilização de tokens de espaçamento do `design_system_5.md` (múltiplos de 4dp)[cite: 6].
    - [ ] Bottom Bar ativa e correta para o perfil Admin.
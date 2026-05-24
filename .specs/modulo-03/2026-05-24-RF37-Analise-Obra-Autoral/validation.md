# Plano de Validação: RF37

## Critérios de Aceitação
- [ ] **Aprovação:** Obra deve ser movida da tabela `obras_autorais` para `livros` no banco de dados (atomicidade necessária)[cite: 10].
- [ ] **Rejeição:** Obra deve permanecer em `obras_autorais` com status `REJEITADA` e motivo populado; usuário deve receber notificação[cite: 6, 10].
- [ ] **Interface:** O lembrete em azul deve estar posicionado acima dos botões de ação conforme especificado[cite: 8].
- [ ] **Fluxo:** Botão de voltar deve retornar à `Tela de Moderação de Obras Autorais` (RF36)[cite: 8].

## Portões de Qualidade (Quality Gates)
- **Gate 1:** A aprovação deve disparar um processo de auditoria em `atividades_admin`[cite: 6].
- **Gate 2:** Validação de segurança: apenas usuários com perfil `ADMIN` podem executar ações nesta tela[cite: 3, 10].
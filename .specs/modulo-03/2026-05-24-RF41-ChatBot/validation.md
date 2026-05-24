# Plano de Validação: RF41

## Critérios de Aceitação
- [ ] **Restrição de Acesso:** Ao tentar acessar a rota via URL ou atalho, administradores devem ser redirecionados para o Dashboard Admin (RF26).
- [ ] **Fluxo de Conversa:** O sistema deve manter o histórico da sessão ativa (RF41.8) enquanto o chat estiver aberto.
- [ ] **UI:** Balões de chat devem seguir o token de cor definido no `design_system_6.md` (distinguindo remetentes)[cite: 5].
- [ ] **Interação:** O botão "enviar" deve consumir o texto do campo e atualizar a lista de mensagens instantaneamente.

## Portões de Qualidade (Quality Gates)
- **Gate 1:** A lógica de validação de `TipoUsuario` deve ser centralizada em um `RouterGuard` ou dentro do `NavHost` para evitar repetição[cite: 4].
- **Gate 2:** O chat não deve expor dados sensíveis do banco via log, seguindo RNF01 (segurança)[cite: 8].
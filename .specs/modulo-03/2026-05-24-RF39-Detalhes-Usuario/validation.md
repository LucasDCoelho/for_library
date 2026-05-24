# Plano de Validação: RF39

## Critérios de Aceitação
- [ ] **Bloqueio:** Ao ativar o toggle "Bloquear Acesso", o estado no banco de dados deve refletir `StatusUsuario.BLOQUEADO`[cite: 6].
- [ ] **Login (Regra de Negócio):** O sistema deve verificar o status no `auth_supabase_4.md` e impedir o login de usuários bloqueados[cite: 3].
- [ ] **Alerta:** Card vermelho deve aparecer apenas se `resenhas_inadequadas > 0`. Mensagem de alerta de limite deve ser exibida se `resenhas_inadequadas >= 3`[cite: 8].
- [ ] **Fechamento:** Botão "Fechar" deve desmontar o componente do Compose corretamente[cite: 8].

## Portões de Qualidade (Quality Gates)
- **Gate 1:** A mudança de estado deve ser persistida via Supabase com tratamento de exceção (UI feedback).
- **Gate 2:** O componente deve ser isolado (separado de `feature/aluno`) para evitar vazamento de lógica administrativa[cite: 4].
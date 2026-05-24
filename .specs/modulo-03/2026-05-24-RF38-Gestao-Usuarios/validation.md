# Plano de Validação: RF38

## Critérios de Aceitação
- [ ] **Integração de Dados:** A listagem deve refletir precisamente os campos da tabela `usuarios` (`matricula`, `nome`, `status`)[cite: 10].
- [ ] **Performance:** O carregamento da lista deve seguir o RNF02.2 (< 2 segundos)[cite: 8].
- [ ] **UX:** A `SearchBar` deve atualizar a lista de forma fluida conforme o input.
- [ ] **Acesso:** Validar se apenas usuários com `tipo = ADMIN` conseguem acessar este módulo (`auth_supabase_3.md` + RLS)[cite: 3, 10].

## Portões de Qualidade (Quality Gates)
- **Gate 1:** O status 'Bloqueado' deve ser exibido visualmente de forma distinta (ex: cor vermelha para alerta), conforme o padrão de estados semânticos do `design_system_3.md`[cite: 5].
- **Gate 2:** O contador de usuários deve ser preciso e atualizado após cada busca ou remoção de filtro.
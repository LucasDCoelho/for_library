# Plano de Validação: RF40

## Critérios de Aceitação
- [ ] **Persistência:** A alteração do valor de gamificação (RF40.2) deve ser refletida instantaneamente no banco (`configuracoes_sistema`)[cite: 8, 10].
- [ ] **Logout:** Botão "SAIR" deve encerrar todas as sessões ativas do usuário e invalidar os tokens de segurança no `auth_supabase_5.md`[cite: 3].
- [ ] **UI:** O layout deve respeitar os tokens de cor (`AzulPrimario`) e a tipografia padrão do projeto[cite: 5].

## Portões de Qualidade (Quality Gates)
- **Gate 1:** A chave de configuração (`pontos_por_livro`) deve ser validada antes de ser salva, evitando strings inválidas ou caracteres especiais (RNF05)[cite: 8].
- **Gate 2:** O botão "SAIR" deve apresentar um diálogo de confirmação antes de destruir a sessão (RF25)[cite: 8].
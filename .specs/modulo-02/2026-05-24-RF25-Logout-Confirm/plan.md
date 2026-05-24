# ADR 005 - Implementação do Fluxo de Logout Seguro (RF25)

- **Contexto:** Necessidade de confirmar o encerramento da sessão do usuário, garantindo a limpeza segura dos tokens de autenticação antes do redirecionamento para o login (RF25.1, RF25.3).
- **Decisão Arquitetural:**
    - **UI:** Implementar um `AlertDialog` do Material 3, seguindo o padrão visual definido em `design_system_3.md`[cite: 6].
    - **Lógica de Autenticação:** Invocar o método `supabase.gotrue.logout()` conforme especificado em `auth_supabase_3.md`, garantindo a invalidação dos tokens persistidos no `EncryptedSharedPreferences`.
- **Motivação:** Prevenir vazamento de dados de sessão e assegurar que o estado do usuário (`Usuario`, `ProgressoLeitura`) seja limpo da memória antes do redirecionamento.
- **Consequências:** Usuário é forçado a re-autenticar (RF25.2)[cite: 9], mantendo a integridade do acesso institucional.

## Plano de Implementação
1. **Componente:** Criar `PopupLogout.kt` no diretório de componentes globais ou de `feature/auth/`.
2. **UI:**
    - Título: "Confirmar Exclusão" (Nota: O texto deve ser ajustado para "Tem certeza que deseja desconectar sua conta?" conforme `requirements_3.md`)[cite: 9].
    - Texto explicativo: "Você precisará informar seu e-mail e senha novamente..." (RF25.2)[cite: 9].
    - Botões: "Cancelar" e "Sim, Sair" (RF25.3)[cite: 9].
3. **Integração:** O botão "Sim, Sair" deve disparar a corrotina de logout do Supabase antes de navegar para `TelaLogin`[cite: 3].
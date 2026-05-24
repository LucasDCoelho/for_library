# ADR 004 - Implementação do Módulo de Configurações (RF24)

- **Contexto:** A tela de Configurações (RF24) é o painel de controle do usuário, lidando com persistência local de preferências (Tema/Push) e governança de conta (Sair/Privacidade)[cite: 8].
- **Decisão Arquitetural:**
    - **Persistência:** Utilizar `DataStore` (Preferences) para persistir o estado dos *Switches* (`Tema Escuro`, `Notificações Push`) de forma reativa, evitando acesso direto a `SharedPreferences` legadas[cite: 4].
    - **Navegação:** Implementar rotas isoladas (`SettingsRoute`) no `NavHost` principal, garantindo que o retorno (seta de voltar) utilize a pilha de navegação do Compose[cite: 4].
    - **Segurança:** O botão "Sair" (RF24.4) deve disparar obrigatoriamente o cleanup da sessão via `supabase.gotrue` antes de redirecionar para o Login[cite: 3].

## Plano de Implementação
1. **Estrutura:** Criar `feature/aluno/configuracoes/TelaConfiguracoes.kt`.
2. **Componentes:**
    - Header com Título e Seta (conforme padrão em `design_system_2.md`).
    - Lista vertical (`LazyColumn`) para separar grupos "Geral", "Sobre o app" e "Conta".
    - `Switch` component do Material 3 para as preferências[cite: 4].
3. **Lógica:**
    - ViewModel (`ConfigViewModel`) gerenciando os estados de persistência via DataStore.
    - Integração do botão Logout com o fluxo de autenticação definido em `auth_supabase_2.md`[cite: 3].
# ADR XXX - Implementação e Acesso Restrito: Chat Bot (RF41)

## Contexto
O assistente virtual (`ChatBot`) é uma ferramenta de suporte exclusiva para o aluno (`João Augusto`). O administrador não deve ter acesso a esta funcionalidade no painel administrativo, garantindo a separação de escopos de usuários[cite: 8].

## Especificação (SDD)
- **RF41.1:** Cabeçalho azul "Assistente ForLibrary" (Status Online).
- **RF41.6 / 41.7:** Campo de entrada e botão de envio (avião de papel)[cite: 8].
- **RF41.8 / 41.9:** Implementação de estado para interações consecutivas e balões distintos (aluno vs bot)[cite: 8].
- **Segurança:** O acesso à rota `TelaChatBot` deve ser verificado no `NavHost` (`ForLibraryApp.kt`) para permitir apenas `TipoUsuario.ALUNO`[cite: 4, 6].

## Plano de Execução
1. **Roteamento:** Adicionar guarda de rota no `NavHost` para validar `Usuario.tipo` antes de navegar para `TelaChatBot`.
2. **ViewModel (`ChatBotViewModel`):** Gerenciar `StateFlow<List<Mensagem>>` para suportar interações consecutivas (RF41.8)[cite: 8].
3. **UI (`TelaChatBot`):**
    - Utilizar `LazyColumn` para o histórico de conversas (RF41.5)[cite: 8].
    - Implementar balões de cores distintas (RF41.9) usando `design_system_6.md`.
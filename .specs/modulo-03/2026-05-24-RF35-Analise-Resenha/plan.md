# ADR XXX - Implementação da Tela de Análise de Resenha (RF35)

## Contexto
O administrador precisa de um fluxo para moderar as resenhas criadas pelos alunos antes que se tornem públicas. O status da resenha deve transitar de 'PENDENTE' para 'APROVADA' ou 'REJEITADA'.

## Especificação (SDD)
- **RF35.1:** Header com seta de retorno e título "Analisar Resenha".
- **RF35.2:** Exibir corpo completo, autor, data e nota[cite: 8].
- **RF35.3:** Aviso de diretrizes de comunidade (azul)[cite: 8].
- **RF35.4/RF35.5:** Ações de Aprovação (Green) / Rejeição (Red) com atualização no Supabase[cite: 3, 10].
- **RF35.6:** Motivo opcional na rejeição.

## Plano de Execução
1. **ViewModel (`AnaliseResenhaViewModel`):** Gerenciar estado da resenha e eventos de moderação (approve/reject).
2. **Repository:** Integrar com `ResenhaRepository` para atualizar `status` e `motivo_rejeicao` na tabela `resenhas`[cite: 6, 10].
3. **UI (`TelaAnaliseResenha`):**
    - Compor elementos conforme `design_system.md`[cite: 5].
    - Utilizar botões com cores semânticas (Sucesso/Erro)[cite: 5].
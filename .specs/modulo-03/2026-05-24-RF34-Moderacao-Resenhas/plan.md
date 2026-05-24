# ADR 014 - Internacionalização e Paginação no Fluxo de Moderação (RF34)

- **Contexto:** RF34 apresenta inconsistências de linguagem (strings em inglês) e falta de funcionalidade na paginação (carregar mais).
- **Decisão Arquitetural:**
    - **Internacionalização (i18n):** Migrar todas as strings estáticas da UI (`"Reviews pendentes"`, `"Pendente"`) para o arquivo `strings.xml` (ou `Strings.kt` seguindo o design system), garantindo a conformidade com o idioma do projeto (`pt-BR`).
    - **Paginação:** Implementar paginação baseada em cursor no `ResenhasRepository` consultando a tabela `resenhas` no `supabase_schema_13.md`[cite: 10]. O botão "Carregar mais" deve disparar uma nova query utilizando `limit` e `offset` (ou cursor baseado em data).
    - **Filtro (RF34.3):** Definir a estrutura do filtro no `ViewModel` para atuar diretamente na query SQL do Supabase (`WHERE status = 'Pendente'`), garantindo precisão na visão de moderação[cite: 10].
- **Motivação:** Restaurar a usabilidade e garantir que o administrador tenha controle total sobre o fluxo de resenhas.
- **Consequências:** Conformidade com a documentação do projeto e eliminação de erros de localização na UI.

## Plano de Implementação
1. **ViewModel:** Refatorar `ModeracaoResenhasViewModel` para gerenciar o estado da lista paginada e o filtro de status.
2. **UI:** Ajustar `TelaModeraçãoResenhas.kt`.
    - Substituir textos hardcoded por chaves de tradução.
    - Implementar `Paging` ou carregamento incremental no `LazyColumn` ao clicar no botão "Carregar mais".
3. **Persistência:** Atualizar a query do `ResenhaRepository` para suportar o filtro de status e paginação, seguindo o `mapeamento_entidades_13.md`[cite: 6].
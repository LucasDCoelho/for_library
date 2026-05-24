# ADR XXX - Implementação da Tela de Moderação de Obras (RF36)

## Contexto
O administrador deve ter acesso a uma lista de obras submetidas pelos alunos (RF15) para revisão editorial antes da publicação no acervo.

## Especificação (SDD)
- **RF36.1:** Header com título "Moderação de Obras" e botão de retorno.
- **RF36.3:** Listagem via `ObraAutoralRepository` consultando a tabela `obras_autorais` (status 'Pendente')[cite: 6, 10].
- **RF36.4:** Cards devem exibir: Título, Gênero, Autor, Matrícula e Badge "Pendente".
- **RF36.5:** Botão "Revisar" (azul) redirecionando para RF37.
- **RF36.6:** Card informativo com "TOTAL PENDENTE".

## Plano de Execução
1. **Repository:** Implementar `getObrasPendentes()` que consulta `obras_autorais` filtrando por status.
2. **ViewModel:** `ModeracaoObrasViewModel` para gerenciar a lista e o estado de carregamento.
3. **UI (`TelaModeracaoObras`):**
    - Utilizar `LazyColumn` para a lista de obras.
    - Aplicar badge "Pendente" com fundo amarelo (conforme nota de status atualizada).
    - Bottom Bar padrão `AdminBottomBar`[cite: 5].
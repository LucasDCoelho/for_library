# Plano de Implementação - RF18 (Notificações)

## 1. Localização
- **Módulo:** `feature/aluno`
- **Arquivo Alvo:** `app/src/main/java/com/br/unifor/for_library/feature/aluno/presentation/notificacoes/TelaNotificacoes.kt`

## 2. Lógica (NotificacoesViewModel)
- **Estado:** `StateFlow<List<Notificacao>>`.
- **Ações:**
    - `carregarNotificacoes()`: `SELECT * FROM notificacoes WHERE usuario_id = auth.uid() ORDER BY data_envio DESC`.
    - `marcarComoLida(notificacaoId: Long)`: `UPDATE notificacoes SET lido = true WHERE id = notificacaoId`.
    - **Transformação:** Agrupar lista em `Map<String, List<Notificacao>>` usando chaves "Hoje", "Ontem", "Anteriores" com base no `LocalDateTime`.

## 3. Estrutura da Interface (RF18.3, RF18.5, RF18.6)
- **Scaffold:** Com `TopAppBar` (título "Notificações", seta voltar) e `TopAppBar` ação "Marcar todas como lidas" (RF18.2).
- **LazyColumn:**
    - Iterar sobre o mapa de grupos temporais.
    - **Item (Card):**
        - `Modifier.background` dinâmico: Se `!lido` -> `Color(0xFFE3F2FD)` (azul claro); se `lido` -> `White`.
        - **Row:**
            - Ícone de Círculo Azul (RF18.5): `Canvas(modifier = size(8.dp))` se `!lido`.
            - **Column:** Título (Bold), Mensagem (Cinza), Horário (labelSmall).
        - **Evento de Click:** `onNotificacaoClick(id)` que chama `viewModel.marcarComoLida(id)`.
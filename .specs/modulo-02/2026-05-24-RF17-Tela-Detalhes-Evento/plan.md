# Plano de Implementação - RF17 (Tela de Detalhes do Evento)

## 1. Localização
- **Módulo:** `feature/aluno`
- **Arquivo Alvo:** `app/src/main/java/com/br/unifor/for_library/feature/aluno/presentation/eventos/TelaDetalhesEvento.kt`
- **Navegação:** `detalhes_evento/{eventoId}`

## 2. Estrutura Visual
- **TopAppBar:** Seta de voltar, Título do Evento em destaque.
- **Banner:** `AsyncImage` (Coil) ocupando a largura total, altura `200.dp`.
- **Conteúdo (`Column` + `LazyColumn`):**
    - Título/Filtro (estilo `headlineSmall`).
    - **Grid de Info:**
        - `Row` (Ícone Data + Texto): Horários de início e fim.
        - `Row` (Ícone Local + Texto): Endereço e Complemento.
    - **Sobre:** "Sobre o evento" (`titleMedium`, `Bold`) seguido do texto descritivo.
- **Botão Calendário (RF17.5):**
    - `Button(onClick = { adicionarEventoCalendario(...) })`.
    - Label: "Adicionar ao Meu Calendário".
    - Estilo: `AzulPrimario` com ícone de calendário.

## 3. Lógica de Calendário (Android Native)
- Utilizar `android.provider.CalendarContract`.
- Ao clicar no botão:
    1. Construir `Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI)`.
    2. Injetar dados: `TITLE`, `DESCRIPTION`, `EVENT_LOCATION`, `DTSTART`, `DTEND`.
    3. Context.startActivity(intent).
    4. **Feedback (Correção do Bug):** Após o retorno da Intent, verificar se a ação foi concluída e disparar `SnackbarHostState.showSnackbar("Evento adicionado ao calendário!")` em verde.
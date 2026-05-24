# Plano de Implementação - RF11 (Popup de Fim de Leitura)

## 1. Localização e Escopo
- **Módulo:** `feature/aluno`
- **Arquivo Alvo:** `app/src/main/java/com/br/unifor/for_library/feature/aluno/presentation/leitor/components/PopupFimLeitura.kt`
- **Contexto:** Injetado dentro de `TelaLeitorDigital.kt` usando a tag condicional `if (uiState.showFimLeituraPopup)`.

## 2. Estrutura do Componente (Compose)
- **Assinatura:**
  `@Composable fun PopupFimLeitura(pontosGanhos: Int, onAvaliarClick: () -> Unit, onFecharClick: () -> Unit)`
- **Contêiner (Design System):**
    - Componente: `Dialog` do pacote `androidx.compose.ui.window`.
    - Base: `Surface` com `shape = RoundedCornerShape(16.dp)`, fundo branco e elevação de `6.dp` para se destacar da opacidade escura do overlay.
- **Layout Interno (`Column` centralizada):**
    - **Ícone (RF11.2):** `Icon` com `Icons.Filled.AutoStories` ou `MenuBook`, com tamanho grande (ex: `48.dp`) e `tint = AzulPrimario`.
    - **Label (RF11.5):** Text "Leitura finalizada" com estilo `labelLarge` ou `titleSmall`, cor `CinzaTexto`, posicionado no topo superior.
    - **Mensagem (RF11.2 - CORREÇÃO DE BUG):** Text "Parabéns! Você concluiu a leitura e ganhou $pontosGanhos pontos." usando `titleMedium`, peso `Bold`, centralizado (`TextAlign.Center`).
    - **Spacer:** de `24.dp` antes das ações.

## 3. Ações (Botões)
- Uma `Row` ou `Column` de botões:
    - **Botão Primário (RF11.3):** `Button` preenchido em `AzulPrimario`. Label: "Avaliar Livro". Gatilho: `onAvaliarClick`.
    - **Botão Secundário (RF11.4):** `TextButton` ou `OutlinedButton` com `CinzaTexto`. Label: "Fechar". Gatilho: `onFecharClick`.

## 4. Integração do Gatilho na TelaLeitorDigital (RF11.1)
- O estado `showFimLeituraPopup` deve se tornar `true` quando `pagerState.currentPage == pagerState.pageCount - 1`.
- A lógica do `LeitorViewModel` intercepta isso, atualiza a entidade `ProgressoLeitura` com status `CONCLUIDO` no Supabase e lança a transação no `historico_pontos`. Retorna para a UI o valor dos pontos resgatado de `ConfiguracaoSistema` (padrão de 50).
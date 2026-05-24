# Plano de Implementação - RF14 (Popup de Avaliação e Resenha)

## 1. Localização
- **Módulo:** `feature/aluno`
- **Arquivo:** `app/src/main/java/com/br/unifor/for_library/feature/aluno/presentation/leitor/components/PopupAvaliacaoResenha.kt`

## 2. Estrutura Visual
- **Dialog:** Utilizar `Dialog(onDismissRequest = onFecharClick)`.
- **Surface:** `RoundedCornerShape(16.dp)`, fundo branco, `padding` de `16.dp`.
- **Header:** "Avaliação" (Título) e "X" (Botão fechar).
- **Livro Card (RF14.3):** `Row` compacta com `CapaLivro(livro.capaUrl)` (tamanho reduzido, ex: `64.dp`) + Título/Autor.
- **Formulário:**
    - Estrelas (RF14.4): Componente `Row` com 5 `IconButton` usando `Icons.Filled.Star`. Estado: `notaSelecionada`.
    - Label: "SUA RESENHA" (RF14.5).
    - `OutlinedTextField`: `maxLines = 5`, `charLimit = 500`.

## 3. Lógica (ViewModel)
- `enviarAvaliacao(nota: Int, texto: String?)`:
    - Se `texto` não for nulo e >= 20 chars:
        - Insert em `resenhas` com `status = PENDENTE`.
        - UI exibe: "Enviado para moderação!".
    - Se `texto` for nulo ou < 20 chars:
        - Insert em `resenhas` com `status = APROVADA` (ou AVALIACAO_REGISTRADA direta, conforme necessidade).
        - UI exibe: "Avaliação registrada!".
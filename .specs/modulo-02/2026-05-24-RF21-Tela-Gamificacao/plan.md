# Plano de Implementação - RF21 (Gamificação e Pontos)

## 1. Localização e Escopo
- **Módulo:** `feature/aluno`
- **Arquivo Alvo:** `app/src/main/java/com/br/unifor/for_library/feature/aluno/presentation/gamificacao/TelaGamificacao.kt`
- **ViewModel:** `GamificacaoViewModel.kt`

## 2. Lógica de Domínio (ViewModel)
- **Estados (StateFlows):**
    - `saldoPontos`: Inteiro.
    - `nivelAtual`: Inteiro (1 a 5).
    - `historico`: `List<HistoricoPontos>`.
- **Ações:**
    - `carregarGamificacao()`:
        1. Busca paralela (Coroutines async):
            - *Deferred 1:* `supabase.from("usuarios").select { filter { eq("auth_user_id", auth.uid) } }` (Extrai pontos e nível).
            - *Deferred 2:* `supabase.from("historico_pontos").select { filter { eq("usuario_id", db_user_id) }; order("data_ganho", Order.DESCENDING) }`.

## 3. Estrutura da Interface (Compose)
- **TopAppBar (RF21.1):** Título "Meus Pontos" e `IconButton` de `ArrowBack` para o `navController.popBackStack()`.
- **Medidor de Nível (RF21.2):**
    - `Card` superior com elevação.
    - `Text` com total de pontos gigantesco (Ex: `displayLarge`, `AzulPrimario`).
    - Função auxiliar para mapear o nível: `getTituloNivel(nivel: Int): String` (1 -> Leitor Iniciante, ..., 5 -> Leitor Mestre).
    - `LinearProgressIndicator` (opcional: calcular próximo nível se houver regra de xp definida, caso contrário, exibir a representação visual do nível cheia).
- **Regras (RF21.4):**
    - `Card` informativo com fundo azul claro (`#E8F4FD`) e ícone de `Info`. Texto explicando: "Ler um livro: 50 pts | Resenha aprovada: 15 pts".
- **Histórico (RF21.3):**
    - `Text` "Histórico de Pontos" (`titleMedium`, `SemiBold`, padding `16.dp`).
    - `LazyColumn` iterando o estado de histórico.
    - **Componente de Item:** `Row` simples.
        - Esquerda: Ícone de estrela ou check.
        - Centro: `Column` -> `Text` (descrição, `bodyMedium`), `Text` (data formatada, `labelSmall`, `CinzaTexto`).
        - Direita: `Text` ("+ ${pontos} pts", `SemiBold`, cor semântica de Sucesso `#2E7D32` ou `#28A745`).
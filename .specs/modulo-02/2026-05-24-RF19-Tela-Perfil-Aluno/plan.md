# Plano de Implementação - RF19 (Tela Perfil do Aluno)

## 1. Localização e Escopo
- **Módulo:** `feature/aluno`
- **Arquivo Principal:** `app/src/main/java/com/br/unifor/for_library/feature/aluno/presentation/perfil/TelaPerfil.kt`
- **Componentes:** `HeaderPerfil.kt`, `SecaoEstatisticas.kt`, `ItemBotaoPerfil.kt`.

## 2. Lógica de Domínio (PerfilViewModel)
- **Estado:** `StateFlow<Usuario?>`.
- **Ações:**
    - `loadUsuario()`: Busca dados atualizados da tabela `usuarios` no Supabase via `auth.uid()`.
    - `logout()`: `supabase.gotrue.logout()`. Limpa cache e navega para `rota_login`.
- **Integridade:** As estatísticas de "Livros lidos" e "Resenhas aprovadas" devem ser computadas a partir de contadores nas tabelas `progresso_leitura` e `resenhas` (via join ou query agregada), conforme `supabase_schema_11.md`.

## 3. Estrutura da Interface (Compose)
- **Scaffold:** Inclui a `ForLibraryBottomBar` fixa (RF19.5).
- **HeaderPerfil (RF19.2):**
    - `Image` (foto de perfil, shape `CircleShape`).
    - `Text` (Nome, `headlineSmall`) e `Text` (Matrícula, `bodyMedium`).
- **SecaoEstatisticas (RF19.3):**
    - `Row` com três blocos (Livros Lidos, Resenhas, Pontos).
    - Cada bloco: `Text` (valor, `titleLarge`) + `Text` (legenda, `labelSmall`).
- **Lista de Ações (RF19.4):**
    - `Column` contendo `ItemBotaoPerfil` para cada uma das 5 ações.
    - Cada item: `Row` com ícone (esquerda) + texto + seta indicativa (direita).
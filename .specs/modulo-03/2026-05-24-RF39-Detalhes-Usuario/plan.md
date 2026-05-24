# ADR XXX - Implementação e Correção: Pop-up de Detalhes do Usuário (RF39)

## Contexto
O administrador precisa gerenciar o acesso de alunos (bloqueio/desbloqueio) e visualizar métricas de comportamento (resenhas inadequadas) diretamente no painel administrativo[cite: 8].

## Especificação (SDD)
- **RF39.1:** Dialog/Popup sobreposto à `TelaGestaoUsuarios` (RF38)[cite: 8].
- **RF39.3:** Toggle "Bloquear Acesso":
    - Se `true` → `Usuario.status` = `StatusUsuario.BLOQUEADO`.
    - Lógica: Atualizar tabela `usuarios` no Supabase[cite: 6, 10].
- **RF39.5:** Card de alerta vermelho (exibir contagem `resenhas_inadequadas`). Se >= 3, exibir aviso de limite automático[cite: 8].

## Plano de Execução
1. **Repository (`UsuarioRepository`):** Garantir método `updateUsuarioStatus(usuarioId: Long, status: StatusUsuario)`.
2. **ViewModel (`GestaoUsuariosViewModel`):** Adicionar função `toggleBloqueioUsuario(usuarioId: Long, bloquear: Boolean)`[cite: 8].
3. **UI (`PopupDetalhesUsuario`):**
    - Criar componente `AlertDialog` ou `Dialog` seguindo `design_system_4.md`.
    - Implementar a lógica condicional para o card vermelho de alerta (RF39.5)[cite: 8].
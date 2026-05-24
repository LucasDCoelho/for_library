# ADR XXX - Revisão de Conformidade: Gestão de Usuários (RF38)

## Contexto
A tela de gestão de usuários permite que administradores visualizem a base de alunos, filtrem por nome/matrícula e verifiquem o status de conta (Ativo/Bloqueado), conforme definido em `requirements_3.md`.

## Especificação (SDD)
- **RF38.1:** Header padronizado ("Gestão de Usuários" + seta de retorno).
- **RF38.2:** SearchBar dinâmica para filtragem por `nome` ou `matricula`.
- **RF38.3:** Listagem com contador de resultados ("x Usuários encontrados").
- **RF38.4:** Cards individuais contendo `foto_perfil`, `nome`, `matricula` e `status` (extraídos de `Usuario` em `mapeamento_entidades_3.md`).
- **RF38.5:** Navegação integrada ao Popup de Detalhes (RF39).

## Plano de Revisão e Execução
1. **Repository:** Validar `UsuarioRepository` contra a tabela `usuarios` no `supabase_schema_3.md`[cite: 10].
2. **ViewModel (`GestaoUsuariosViewModel`):**
    - Implementar lógica de busca local ou via query Supabase (`supabase_schema_3.md`).
3. **UI (`TelaGestaoUsuarios`):**
    - Revisar alinhamento com `design_system_3.md` (tokens de cor e espaçamentos)[cite: 5].
    - Garantir a renderização da `foto_perfil` via `CapaLivro` (ou componente similar de avatar/imagem)[cite: 5].
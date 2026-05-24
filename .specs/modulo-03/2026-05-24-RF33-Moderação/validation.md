# Validação do Requisito RF33

## Critérios de Aceitação (Baseado em `requirements_12.md`)

1. **Hub de Acesso (RF33.1 - RF33.6):**
    - [ ] Card "Moderação de Usuários" visível e navegável (RF33.2).
    - [ ] Card "Moderação de Obras" visível e navegável (RF33.4).
    - [ ] Card "Moderação de Resenhas" visível e navegável (RF33.6).
2. **Navegação (RF33.7):**
    - [ ] `BottomNavigationBar` do Administrador apresenta o item "Moderação" como ativo (RF33.8).
3. **Segurança (RNF01):**
    - [ ] Bloqueio de acesso via RLS (testar com perfil Aluno).
4. **Design System:**
    - [ ] Uso de ícones consistentes (Material Icons).
    - [ ] Cards seguem a elevação e raios de canto de 12dp conforme `design_system_12.md`.
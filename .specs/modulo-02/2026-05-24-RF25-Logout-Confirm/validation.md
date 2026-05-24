# Validação do Requisito RF25

## Critérios de Aceitação (Baseado em `requirements.md`)[cite: 9]

1. **Gatilho (RF25.1):**
    - [ ] O popup é acionado corretamente ao clicar em "Sair" em `RF19` (Perfil) ou `RF24` (Configurações).

2. **UI e Conteúdo (RF25.2):**
    - [ ] Título "Tem certeza que deseja desconectar sua conta?" exibido.
    - [ ] Texto explicativo exibido corretamente.

3. **Ações (RF25.3):**
    - [ ] **Botão "Cancelar":** Retorna à tela anterior sem encerrar a sessão.
    - [ ] **Botão "Sim, Sair":**
        - [ ] Executa `supabase.gotrue.logout()`.
        - [ ] Limpa o estado local de usuário.
        - [ ] Redireciona o usuário para `RF02` (Tela de Login).

4. **Design System:**
    - [ ] O componente segue as diretrizes de `design.md` e usa tokens de design `design_system.md` para cores e formas[cite: 5, 6].
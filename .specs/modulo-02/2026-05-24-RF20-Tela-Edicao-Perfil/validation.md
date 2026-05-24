# Protocolo de Validação - RF20

## Casos de Teste

1. **Validação de Bloqueio (RF20.4)**
    - **Ação:** Tentar editar o campo de Matrícula ou E-mail.
    - **Espera-se:** O teclado não deve abrir e o campo deve ignorar qualquer tentativa de input. O ícone de cadeado deve estar visível.

2. **Validação de Atualização (RF20.5)**
    - **Ação:** Alterar biografia e clicar em "Salvar Alterações".
    - **Espera-se:** O app deve mostrar indicador de carregamento. Após, a mensagem "Alterações salvas!" deve ser exibida e a tela anterior (Perfil) deve refletir a nova biografia (via re-fetch dos dados).

3. **Validação de Upload (RF20.2)**
    - **Ação:** Selecionar nova foto e salvar.
    - **Espera-se:** O Supabase Storage deve conter a nova imagem e o `usuarios.foto_perfil` deve apontar para o novo caminho.
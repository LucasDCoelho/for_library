# Validação do Requisito RF28

## Critérios de Aceitação (Baseado em `requirements_6.md`)

1. **Campos Obrigatórios (RF28.1):**
    - [ ] Validação de campo vazio (Título, Autor, ISBN, Categoria).
    - [ ] Mensagens de erro visíveis abaixo do campo (cor `#D32F2F`).
2. **Upload de Capa (RF28.2):**
    - [ ] Seleção de imagem da galeria funcional.
    - [ ] Preview da imagem carregada visível no formulário.
    - [ ] Feedback de sucesso/falha no upload.
3. **Persistência (RF28.3):**
    - [ ] Botão "Cadastrar" salva os dados corretamente no Supabase.
    - [ ] Limpeza do formulário após sucesso.
    - [ ] Feedback visual (SnackBar ou Toast) de "Livro cadastrado com sucesso".
4. **Navegação:**
    - [ ] Redirecionamento automático de volta para o `RF27` (Gestão de Acervo) após a persistência bem-sucedida.

## Checklist de Design
- [ ] Seguir tokens de `design_system_6.md` para botões primários.
- [ ] Espaçamento de 16dp entre campos de formulário.
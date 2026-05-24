# Validação do Requisito RF29

## Critérios de Aceitação (Baseado em `requirements_6.md`)

1. **Carregamento (RF29.3):**
    - [ ] Dados do livro (Título, Autor, Gênero, etc.) carregam automaticamente ao abrir a tela.
    - [ ] Campo de capa exibe a imagem atual do livro.
2. **Edição e Upload (RF29.2, RF29.4):**
    - [ ] Botão "Trocar Capa" abre o gerenciador de arquivos e atualiza o preview.
    - [ ] Ícone de lixeira remove o arquivo atual (PDF) e permite novo upload.
3. **Persistência (RF29.5):**
    - [ ] Botão "Atualizar Obra" aplica as mudanças no banco de dados.
    - [ ] Toast de "Obra atualizada com sucesso" exibido.
    - [ ] Navegação de retorno (`popBackStack`) acionada após sucesso.
4. **Design System (design_system_6.md):**
    - [ ] Coerência com cores e espaçamentos definidos[cite: 5].
    - [ ] Componentes de campo (`OutlinedTextField`) mantêm o estado de foco e erro corretos.
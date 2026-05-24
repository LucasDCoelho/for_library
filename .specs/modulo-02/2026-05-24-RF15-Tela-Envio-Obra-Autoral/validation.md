# Protocolo de Validação - RF15

## Casos de Teste

1. **Validação de Upload de Arquivo (RF15.5, RF15.6)**
    - **Ação:** Tocar no botão "Anexar PDF" e selecionar um arquivo PDF válido.
    - **Espera-se:** O seletor de arquivos deve abrir sem falhas. Após a seleção, a tela deve exibir o nome do arquivo e o ícone de lixeira. Clicar na lixeira deve remover o arquivo do estado visual da tela.

2. **Validação de Estado de Submissão (RF15.7)**
    - **Ação:** Preencher campos obrigatórios e clicar em "Enviar Obra".
    - **Espera-se:** O app deve mostrar um indicador de loading (`CircularProgressIndicator`). Após o processamento no Supabase, a mensagem "Obra enviada para análise" deve aparecer de forma estável (via `Snackbar` ou texto fixo), sem desaparecer instantaneamente como um "pop up estúpido".

3. **Auditoria de Banco (RF15.3, RF15.4)**
    - **Ação:** Verificar tabela `obras_autorais` no Supabase após envio.
    - **Espera-se:** A linha criada deve conter o título, gênero, sinopse, link do storage e `status = 'PENDENTE'`. Se o status não for 'PENDENTE', a lógica de persistência está violando as regras de moderação do sistema.
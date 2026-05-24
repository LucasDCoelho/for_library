# ADR XXX - Revisão de Conformidade: Configurações do Sistema (RF40)

## Contexto
O administrador necessita gerenciar variáveis globais de gamificação e o estado da sessão. Os valores devem ser persistidos na tabela `configuracoes_sistema`.

## Especificação (SDD)
- **RF40.1:** Header padronizado ("Configurações do Sistema" + seta de retorno).
- **RF40.2:** Campo de gamificação para editar `pontos_por_livro` (ex: 15 para 20).
- **RF40.3:** Seção "SESSÃO" com aviso de encerramento e botão "SAIR"[cite: 8].

## Plano de Revisão
1. **Repository (`ConfiguracaoRepository`):** Validar se o método `updateConfig(chave: String, valor: String)` está persistindo corretamente na tabela `configuracoes_sistema`[cite: 10].
2. **ViewModel (`ConfiguracoesAdminViewModel`):** Verificar a observabilidade dos valores das chaves (`sistema_versao`, `pontos_por_resenha`, etc.)[cite: 10].
3. **UI (`TelaConfiguracoesAdmin`):**
    - Garantir consistência visual com `design_system_5.md` (campos de input, espaçamentos)[cite: 5].
    - Validar se o botão "SAIR" encerra o `Supabase Auth` e redireciona para Login[cite: 3].
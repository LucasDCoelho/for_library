# Protocolo de Validação - RF21

## Casos de Teste

1. **Validação Mapeamento de Nível (RF21.2)**
    - **Ação:** Injetar artificialmente no Supabase (via SQL no dashboard) o `nivel_gamificacao = 4` e `pontos_gamificacao = 450` no usuário atual.
    - **Espera-se:** A tela deve carregar reativamente exibindo o saldo de 450 e a label "Leitor Especialista". Qualquer erro de formatação barra o pull request.

2. **Validação de Ordem do Histórico (RF21.3)**
    - **Ação:** Realizar fluxos separados (ler um livro, enviar uma resenha e ter a resenha aprovada pelo admin - gerando 2 triggers na tabela `historico_pontos`). Acessar a tela "Meus Pontos".
    - **Espera-se:** O histórico de transações deve aparecer obrigatoriamente do evento mais novo no topo para o mais antigo na base. A cor dos pontos ganhos deve ser o verde semântico definido.

3. **Validação de Interface (RF21.4)**
    - **Ação:** Inspecionar visualmente o bloco de Regras.
    - **Espera-se:** A presença fixa e clara da instrução informando o ganho de 50 pontos e 15 pontos, confirmando o cumprimento da spec.
# ADR XXX - Implementação da Tela de Análise de Obra Autoral (RF37)

## Contexto
O administrador necessita avaliar o conteúdo submetido via RF15 (Envio de Obra Autoral) para garantir a qualidade do acervo. A ação de aprovação deve promover o conteúdo para o catálogo de `livros`, enquanto a rejeição deve notificar o usuário com o motivo[cite: 6, 8].

## Especificação (SDD)
- **RF37.1:** Header com título "Analisar Obra" e botão de retorno.
- **RF37.2:** Card com detalhes da `ObraAutoral`: Capa, Título, Gênero, Autor e Sinopse[cite: 6, 8].
- **RF37.3:** Acesso ao arquivo PDF (botão de download/visualização)[cite: 8].
- **RF37.4:** Lógica de `Aprovar` (persistir em `livros`, atualizar status) e `Rejeitar` (status 'REJEITADA' + notificação)[cite: 6, 8, 10].
- **RF37.5:** Aviso de diretrizes de moderação (azul)[cite: 8].
- **RF37.6:** Campo opcional para motivo da rejeição[cite: 8].

## Plano de Execução
1. **ViewModel (`AnaliseObraViewModel`):** Gerenciar o estado da submissão.
2. **Repository:**
    - `ObraAutoralRepository`: atualizar status.
    - `LivroRepository`: criar novo registro em `livros` caso aprovado[cite: 6, 10].
    - `NotificacaoRepository`: registrar notificação em caso de rejeição.
3. **UI (`TelaAnaliseObra`):**
    - Utilizar componentes de card e botões de ação conforme `design_system_2.md`[cite: 5].
# Protocolo de Validação - RF16

## Casos de Teste

1. **Validação da Badge de Data (RF16.4):**
    - **Ação:** Verificar o posicionamento da data no card.
    - **Espera-se:** A data deve estar obrigatoriamente no canto superior esquerdo (sobreposto ao banner). O uso do `Surface` com `topStart` arredondado deve ser validado para alinhar com o design institucional.

2. **Validação de Filtros (RF16.2):**
    - **Ação:** Clicar nos chips de filtro (ex: "Workshops").
    - **Espera-se:** A `LazyColumn` deve ser recomposta instantaneamente exibindo apenas os eventos do tipo selecionado. O estado de carregamento deve ser rápido (`RNF02.2`).

3. **Validação de Navegação (RF16.5):**
    - **Ação:** Clicar na seta azul em qualquer card.
    - **Espera-se:** Navegar para a tela `RF17` (Detalhes do Evento) correspondente. O ID do evento deve estar correto na rota de navegação.

4. **Persistência de Bottom Bar (RF16.6):**
    - **Ação:** Verificar a presença e funcionalidade da Bottom Navigation Bar.
    - **Espera-se:** A Bottom Bar deve permanecer fixa na parte inferior e todos os botões (Home, Acervo, Estante, Eventos, Perfil) devem rotear corretamente para suas respectivas telas sem falhas.
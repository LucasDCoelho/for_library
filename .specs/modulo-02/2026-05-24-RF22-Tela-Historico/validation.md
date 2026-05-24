# Protocolo de Validação - RF22

## Casos de Teste Implacáveis

### 1. Auditoria Funcional da Lupa (RF22.1)
- **Ação:** Tocar no ícone da lupa no canto superior direito.
- **Espera-se:** O título "Histórico de Leitura" deve sumir, abrindo um campo de digitação de texto com o teclado do dispositivo levantando automaticamente. A "Lupa morta" relatada na spec 14.md será oficialmente erradicada.

### 2. Auditoria do Filtro Reativo
- **Ação:** Digitar fragmentos de título (Ex: "senhor", "Dom") ou autor presentes no histórico.
- **Espera-se:** A lista deve reagir em tempo real (instantaneamente na digitação), escondendo os itens que não combinam com a pesquisa. Limpar a busca pelo "X" deve restaurar a lista original completa e fechar o teclado.

### 3. Integridade da Ordem Cronológica (RF22.2)
- **Ação:** Inspecionar a lista quando não há pesquisa ativa.
- **Espera-se:** Os itens DEVERÃO estar organizados pelo campo `dataConclusao` do registro `ProgressoLeitura` de forma descendente (do concluído mais recentemente no topo para o mais antigo na base).
# Protocolo de Validação - RF19

## Pré-requisitos
- Usuário autenticado.
- Base de dados contendo estatísticas (livros lidos, resenhas aprovadas) vinculadas ao `auth.uid()`.

## Casos de Teste Implacáveis

### 1. Auditoria de Dados (RF19.2, RF19.3)
- **Ação:** Acessar a tela de Perfil.
- **Espera-se:** O nome e matrícula exibidos devem coincidir exatamente com os dados do Supabase. A seção de estatísticas DEVE exibir os números reais (Ex: "12 Livros lidos", "5 Resenhas"). Se exibir "0" ou carregar indefinidamente, a integração com o banco está falha.

### 2. Validação de Ações (RF19.4)
- **Ação:** Clicar em cada um dos 5 botões.
- **Espera-se:**
    - Editar Perfil -> Navega para `RF20`.
    - Histórico de Leitura -> Navega para `RF22`.
    - Dúvidas (FAQ) -> Navega para `RF23`.
    - Configurações -> Navega para `RF24`.
    - Sair -> Abre o popup de confirmação de logout (`RF25`).

### 3. Integridade de Logout (RF25)
- **Ação:** Clicar em "Sair" e confirmar no popup.
- **Espera-se:** O token de autenticação deve ser removido do armazenamento local e a tela de login (RF02) deve ser o novo root da pilha de navegação, impedindo o "botão voltar" de retornar ao perfil.
# ForLibrary

Aplicativo Android de gerenciamento de biblioteca universitária desenvolvido para a **Unifor**, permitindo que alunos consultem o acervo digital, gerenciem sua estante pessoal, participem de eventos literários e acompanhem suas leituras.

## 📋 Descrição

O **ForLibrary** é um app Android nativo construído inteiramente com **Jetpack Compose** e arquitetura **MVVM + Clean Architecture**. O sistema é dividido em duas áreas principais: a **Área do Aluno**, com acesso ao acervo, leitor digital, estante, eventos e perfil; e a **Área do ADM**, destinada à gestão administrativa da biblioteca (em desenvolvimento).

## 🚀 Como Executar o Projeto

### Pré-requisitos

- **Android Studio** Ladybug ou superior
- **JDK 17** ou superior
- **Android SDK** (API 28+)
- Dispositivo físico com Modo Depuração ativado **ou** Emulador configurado

### Passos para Instalação

1. **Clonar o repositório:**
   ```bash
   git clone https://github.com/seu-usuario/ForLibrary.git
   ```

2. **Abrir no Android Studio:**
   - `File > Open` → selecione a pasta do projeto

3. **Sincronizar o Gradle:**
   - O Android Studio sincroniza automaticamente. Caso necessário, clique em **Sync Project with Gradle Files**

4. **Executar:**
   - Selecione o dispositivo/emulador e clique em **Run** (`Shift + F10`)

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Uso |
|---|---|
| **Kotlin** | Linguagem principal |
| **Jetpack Compose** | Interface declarativa |
| **Navigation Compose** | Navegação entre telas |
| **Coil** | Carregamento de capas de livros via Open Library |
| **Material 3** | Design system |
| **Gradle KTS** | Build system |
| **MVVM + Clean Architecture** | Arquitetura do projeto |

## 📂 Estrutura do Projeto

```
app/src/main/java/com/br/unifor/for_library/
│
├── ForLibraryApp.kt              # Ponto central: NavHost + rotas de toda a aplicação
├── MainActivity.kt               # Activity principal que hospeda o Compose
│
├── core/                         # Código compartilhado entre todas as features
│   ├── components/               # Componentes de UI reutilizáveis globalmente
│   │   ├── CapaLivro.kt          # Composable que carrega a capa do livro via ISBN (Open Library)
│   │   └── FiltroAvancado.kt     # Bottom sheet de filtro avançado (gênero + ordenação)
│   │
│   ├── data/                     # Dados e estado compartilhados
│   │   └── LivrosSalvosState.kt  # Estado global de livros salvos + catálogo unificado
│   │
│   ├── designsystem/             # Tokens visuais centralizados
│   │   └── Color.kt              # Paleta de cores (AzulPrimario, CinzaTexto, coresFallback…)
│   │
│   └── navigation/               # Configuração de navegação global
│       ├── Rotas.kt              # Sealed class com todas as rotas do app
│       └── ForLibraryBottomBar.kt # Barra de navegação inferior (Bottom Navigation)
│
└── feature/                      # Features divididas por perfil de usuário
    │
    ├── auth/                     # Fluxo de autenticação (compartilhado entre perfis)
    │   └── ui/
    │       ├── TelaSplash.kt         # Tela de splash/abertura
    │       ├── TelaLoginPlaceholder.kt # Tela de login
    │       └── RecuperarSenha.kt     # Tela de recuperação de senha
    │
    ├── aluno/                    # Área exclusiva do aluno
    │   │
    │   ├── home/ui/
    │   │   └── TelaHomeAluno.kt      # Dashboard principal: leitura em andamento + destaques do acervo
    │   │
    │   ├── acervo/ui/
    │   │   ├── TelaAcervoDigital.kt  # Grade de livros com busca, filtro por categoria e filtro avançado
    │   │   └── BuscaVaziaPlaceholder.kt # Tela de estado vazio quando nenhum livro é encontrado
    │   │
    │   ├── livro/ui/
    │   │   ├── TelaDetalhesLivro.kt  # Detalhes do livro: sinopse, avaliações, botão Ler Agora
    │   │   ├── TelaLeitorDigital.kt  # Leitor digital com paginação horizontal e controles animados
    │   │   ├── TelaFimLeitura.kt     # Popup de conclusão de leitura com pontuação
    │   │   └── TelaAvaliacaoResenha.kt # Formulário de avaliação e resenha do livro
    │   │
    │   ├── estante/ui/
    │   │   └── TelaEstante.kt        # Estante pessoal: aba "Lendo" (progresso) + aba "Favoritos"
    │   │
    │   ├── eventos/ui/
    │   │   └── TelaEventos.kt        # Lista de eventos literários com filtro por tipo
    │   │
    │   ├── notificacao/ui/
    │   │   └── TelaNotificacoes.kt   # Central de notificações agrupadas por data
    │   │
    │   └── perfil/ui/
    │       ├── TelaPerfil.kt         # Perfil do aluno com estatísticas e menu de ações
    │       ├── TelaEditarPerfil.kt   # Edição de nome, biografia e foto de perfil
    │       └── TelaDuvidas.kt        # FAQ com perguntas e respostas em acordeão
    │
    └── adm/                      # Área exclusiva do administrador (em desenvolvimento)
        └── home/ui/              # Home do ADM — telas serão adicionadas aqui
```

## 🗺️ Arquitetura

O projeto segue **MVVM + Clean Architecture** com separação por papel de usuário:

```
ForLibraryApp (NavHost)
      │
      ├── feature/auth/       → login, splash (sem bottom bar)
      ├── feature/aluno/      → todas as telas do aluno (com bottom bar)
      └── feature/adm/        → telas administrativas (em construção)
```

- **`core/`** — sem dependência de features; pode ser importado por qualquer módulo  
- **`feature/aluno/`** — depende apenas de `core/`  
- **`feature/adm/`** — depende apenas de `core/`  
- **`feature/auth/`** — depende apenas de `core/`

## 👥 Equipe

Desenvolvido por **Lucas Dian Coelho, João Francisco Morais Paz, João Gabriel Rinaldi Menezes, João Miguel Viana Silva e Luigi Nilton Lima Craveiro**

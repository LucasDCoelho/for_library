# ForLibrary - Documento de Design e Arquitetura (design.md)

## 1. Visão Geral da Arquitetura Estrutural
O sistema ForLibrary foi projetado com uma topologia fundamentalmente baseada no padrão **MVVM (Model-View-ViewModel)** em conjunto com a **Clean Architecture**. Essa abordagem garante um alto nível de separação de responsabilidades entre as lógicas de apresentação (UI), regras de negócios inerentes ao ambiente universitário e o acesso aos dados.

O sistema também é isolado modularmente pelo papel dos atores (Aluno e Administrador), garantindo que as expansões de escopo não causem ramificações e falhas inesperadas na base de código.

## 2. Pilha de Tecnologias (Tech Stack)
As escolhas tecnológicas priorizam componentes modernos nativos, pautando a construção em um ambiente puramente declarativo:
* **Linguagem Principal:** Kotlin.
* **Interface de Usuário (UI):** Jetpack Compose aliado aos componentes do Material Design 3.
* **Navegação:** Navigation Compose, substituindo o antigo roteamento via Intents isoladas.
* **Processamento e Imagens:** Biblioteca assíncrona Coil para a renderização limpa e otimizada de capas de livros.
* **Build System:** Gradle KTS, garantindo scripts de compilação em Kotlin.
* **Ferramentas Base:** Android SDK (API 28+) e JDK 17+.

## 3. Topologia e Estrutura de Diretórios
O código-fonte segue a estrita separação em pastas, garantindo limites arquiteturais claros em `app/src/main/java/com/br/unifor/for_library/`:

* **`core/`**: Núcleo independente da aplicação. Nenhuma *feature* deve ser importada aqui. Abriga componentes globais (`CapaLivro.kt`, `FiltroAvancado.kt`), gerenciadores de estados (`LivrosSalvosState.kt`), paleta central de design (`Color.kt`) e a infraestrutura unificada da barra de roteamento principal (`Rotas.kt`, `ForLibraryBottomBar.kt`).
* **`feature/auth/`**: Roteamento inicial. Processa a lógica das telas de Splash, autenticação institucional (`TelaLoginPlaceholder.kt`) e recuperação de credenciais antes do usuário ter a Bottom Bar injetada.
* **`feature/aluno/`**: Componentes exclusivos do ator João Augusto (o aluno). Reúne e encapsula toda a jornada acadêmica: `home` (dashboard principal), `acervo` (buscas e filtros), `livro` (detalhamento e leitura do PDF), `estante` (status das obras) e `eventos`.
* **`feature/adm/`**: Componente exclusivo para operações corporativas, restringindo a dashboard administrativa de moderação e métricas numéricas.

## 4. Registro de Decisões de Arquitetura (ADRs)
[cite_start]Baseando-se nos princípios de SDD, documentamos o subjacente contínuo submetido do "como" e "por que" ocorreu cada decisão lógica para prevenir regressões de desvio arquitetural futuro[cite: 584, 585]:

* **ADR 001 - Adoção Única do Jetpack Compose (Supressão do XML)** * **Contexto:** O projeto exige a renderização complexa de carrosséis de acervo, filtros em tempo real e leitura digital paginada.
    * **Decisão:** Centralizar 100% da visualização no Jetpack Compose.
    * **Motivação:** Extingue o overhead de inflação de *layouts* XML e permite a adaptação orgânica das telas aos estados declarativos manipulados dentro do ViewModel.

* **ADR 002 - Roteamento de Módulos Dependentes Somente de Core**
    * **Contexto:** Evitar dependências circulares entre a área de moderação (Admin) e o consumo de acervo (Aluno).
    * **Decisão:** O módulo `feature/aluno`, `feature/adm` e `feature/auth` herdam exclusivamente as tipagens contidas no `core/`.
    * **Motivação:** Cria um isolamento total; o código do ambiente de submissão do administrador jamais conhecerá o código interno da interface do aluno, apenas compartilhará da infraestrutura `core`.

* **ADR 003 - Carregamento Aberto de Metadados via Open Library**
    * **Contexto:** Dificuldade de armazenar nativamente centenas de recursos visuais de alta fidelidade sem estourar o banco de dados interno ou os limites da aplicação.
    * **Decisão:** Implementação da biblioteca Coil consumindo capas via Open Library baseadas na identificação padrão (ISBN).
    * **Motivação:** Mitiga o consumo de espaço no banco e delega o cache e carregamento assíncrono para o próprio ambiente do dispositivo nativo de forma responsiva.

## 5. Fluxos de Interação Operacional
1. A raiz do controle repousa no artefato `ForLibraryApp.kt`, o `NavHost` operante que atua como orquestrador supremo.
2. Na inicialização (`TelaSplash.kt`), o verificador de sessão consulta em *background* a credencial.
3. Mediante a autenticação, a `Navigation Compose` isola a injeção da `ForLibraryBottomBar.kt` contendo apenas as rotas condizentes com a ramificação estipulada pelo tipo de usuário.
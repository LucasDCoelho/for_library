# Design System - ForLibrary (estado atual do codigo)

> Documento gerado por leitura do projeto inteiro (`app/src/main`) sem alterar arquivos da aplicacao.
> Foco: tokens visuais, padroes de composicao e comportamento de UI realmente implementados hoje.

## 1) Visao geral

- Plataforma: Android (Jetpack Compose + Material 3).
- Tema base centralizado em `core/designsystem/Theme.kt` com `lightColorScheme` custom.
- O app usa **mistura de dois estilos**:
  - **Estilo tokenizado** (MaterialTheme + cores do `core/designsystem`), mais presente em Home e componentes compartilhados.
  - **Estilo por tela** com valores hardcoded (`Color(0x...)`, `fontSize = ...sp`, `RoundedCornerShape(...)`), muito presente em telas de Auth, Perfil, Eventos e Admin.
- Resultado atual: identidade visual consistente em azul/branco/cinza, mas com variacao de tons, tamanhos e espacamentos entre telas.

## 2) Fundacao visual (tokens oficiais)

### 2.1 Color scheme global (`core/designsystem`)

Arquivo: `app/src/main/java/com/br/unifor/for_library/core/designsystem/Color.kt`

- `AzulPrimario = #1565C0`
- `AzulChip = #1E88E5`
- `CinzaTexto = #616161`
- `FundoTela = #F2F4F8`
- `coresFallback` para capas sem imagem:
  - `#1565C0`, `#283593`, `#4527A0`, `#00695C`, `#558B2F`

Arquivo: `app/src/main/java/com/br/unifor/for_library/core/designsystem/Theme.kt`

- `primary`: `AzulPrimario`
- `secondary`: `AzulChip`
- `onSurfaceVariant`: `CinzaTexto`
- `background`: `FundoTela`
- `surface`: `White`

### 2.2 Tema XML Android (base)

- `Theme.Material3.DayNight.NoActionBar` em `res/values/themes.xml`.
- Sem customizacoes relevantes de cor no XML (a identidade vem da camada Compose).

## 3) Paleta real usada na aplicacao

Abaixo estao os grupos de cor mais recorrentes no codigo:

### 3.1 Cores principais e de marca

- Azul primario recorrente: `#1565C0`
- Variantes de azul usadas em telas:
  - `#1E88E5`, `#1B65F6`, `#1C64F2`, `#1E54FA`, `#1976D2`
- Fundo claro principal:
  - `#FFFFFF`, `#F5F7FA`, `#F2F4F8`

### 3.2 Neutros (texto, borda, superficie)

- Textos fortes: `#212121`, `#1A1A2E`, `#424242`
- Textos secundarios: `#616161`, `#757575`, `#9E9E9E`, `#BDBDBD`
- Divisores e bordas: `#E0E0E0`, `#EEEEEE`, `#EBEBEB`
- Superficies de apoio: `#F5F5F5`, `#F7F7F7`, `#F9F9F9`, `#FAFAFA`

### 3.3 Cores semanticas de estado

- Sucesso:
  - `#2E7D32`, `#1B5E20`, `#4CAF50`, `#28A745`
- Erro / destrutivo:
  - `#D32F2F`, `#DC3545`, `#B71C1C`
- Alerta / pendencia:
  - `#FFF3CD`, `#856404`
- Info:
  - `#E8F4FD`, `#E8F0FE`, `#E3F2FD`, `#1A237E`

### 3.4 Bottom bar

Arquivos:
- `app/src/main/java/com/br/unifor/for_library/core/navigation/ForLibraryBottomBar.kt`
- `app/src/main/java/com/br/unifor/for_library/core/navigation/AdminBottomBar.kt`

Padrao:
- Fundo: `White`
- Item selecionado: `#1565C0`
- Item nao selecionado: `#9E9E9E`
- Indicador: `#E3EEF9`
- Label: `10sp`

## 4) Tipografia

## 4.1 Abordagem atual

- Nao existe arquivo de tipografia custom (`Typography.kt`) com escala centralizada.
- A app usa combinacao de:
  - `MaterialTheme.typography.*` (ex: `headlineLarge`, `titleMedium`, `labelSmall`, etc.)
  - tamanhos hardcoded por tela (`fontSize = 9.sp .. 52.sp`)

### 4.2 Faixas de tamanho observadas

- **Micro/labels**: `9sp`, `10sp`, `11sp`, `12sp`
- **Texto base**: `13sp`, `14sp`, `15sp`, `16sp`
- **Titulos**: `17sp`, `18sp`, `20sp`, `22sp`, `24sp`, `28sp`, `32sp`
- **Destaques grandes**: `52sp` (pontuacao)

### 4.3 Pesos de fonte usados

- `Normal`, `Medium`, `SemiBold`, `Bold`, `ExtraBold`
- Uso comum:
  - titulos e CTAs: `Bold`/`SemiBold`
  - metadados e secoes: `SemiBold` + `letterSpacing`

### 4.4 Ritmo tipografico

- `lineHeight` frequente entre `16sp` e `22sp` para blocos explicativos.
- `letterSpacing` usado em labels de secao (`0.4sp` a `1sp`) para aspecto "caps/metadata".

## 5) Espacamento e grid

### 5.1 Escala de espacamento mais usada

- Base recorrente em multiplos de 4dp:
  - `2, 4, 6, 8, 10, 12, 14, 16, 20, 24, 28, 32dp`
- Paddings de tela comuns:
  - horizontal: `16dp`, `20dp`, `24dp`
- Alturas de controles comuns:
  - `48dp`, `50dp`, `52dp`, `56dp`

### 5.2 Listas e cards

- Espacamento entre itens geralmente `8dp` a `16dp`.
- `LazyColumn` e `LazyVerticalGrid` com `contentPadding` tipico de `16dp`.

## 6) Forma, borda e elevacao

### 6.1 Raio de canto (shape)

Valores mais recorrentes:
- `4dp`: campos, tags, botoes compactos
- `6dp`: cards pequenos e controles secundarios
- `8dp`: cards principais e botoes
- `10dp`: formularios (Auth/Perfil/Envio)
- `12dp`: cards destacados
- `16dp`: dialogs e cards de destaque
- `20dp`: topo de bottom sheet (`FiltroAvancado`)

### 6.2 Borda

- Bordas finas `1dp` em campos e cards neutros (`#E0E0E0`/`#BDBDBD`).
- Acoes destrutivas usam borda vermelha (`#D32F2F`).

### 6.3 Elevacao

- Muitas superficies com elevacao baixa (`0dp` a `2dp`).
- Dialogs e overlays usam elevacao maior (`4dp` a `8dp` no `Surface`).

## 7) Componentes e padroes de UI

### 7.1 Componentes compartilhados

- `CapaLivro` (`core/components/CapaLivro.kt`):
  - carrega capa via OpenLibrary (ISBN)
  - placeholder cinza no loading
  - fallback com iniciais em fundo colorido (`coresFallback`)
- `FiltroAvancadoBottomSheet` (`core/components/FiltroAvancado.kt`):
  - bottom sheet com secoes em caps
  - chips de genero selecionaveis
  - radio para ordenacao
  - botoes "Limpar" e "Aplicar"

### 7.2 Padroes recorrentes

- Header com avatar/circulo + titulo + acao de sino/config.
- Cards com metadados e CTA no rodape.
- Badges para status (`PENDENTE`, `EM BREVE`, etc.).
- Dialogs de confirmacao para logout/exclusao.
- Bottom sheets para detalhes (ex: usuario moderacao).

### 7.3 Campos de formulario

- Predominio de `OutlinedTextField` com:
  - forma entre `4dp` e `10dp`
  - foco azul
  - fundo branco ou cinza muito claro

## 8) Iconografia e imagem

- Biblioteca: `Icons.Filled`, `Icons.Outlined`, `Icons.AutoMirrored`.
- Uso funcional de icones:
  - navegacao, status, feedback, acoes de card, estados de favorito.
- Imagens:
  - capas por ISBN (OpenLibrary)
  - banners de eventos via URL (Unsplash em mock)
  - gradientes para fallback de banner/legibilidade de texto.

## 9) Navegacao e estrutura visual global

- Host principal: `ForLibraryApp.kt` com `Scaffold` + BottomBar.
- Dois contextos visuais coexistem:
  - Aluno (`ForLibraryBottomBar`)
  - Admin (`AdminBottomBar`)
- Ambos compartilham mesma assinatura visual de bottom bar (azul/cinza/indicador claro).

## 10) Estados de feedback e UX visual

- Sucesso: snackbars verdes, chips verdes e mensagens positivas.
- Erro/destrutivo: vermelho forte em botoes e icones.
- Informativo: cards azul claro com icone de info.
- Vazio: placeholder ilustrado em Acervo (documento + texto orientativo + CTA).

## 11) Acessibilidade visual (o que ja existe)

- Alguns fluxos usam area de toque minima melhorada (`IconButton`, estrelas 48dp em avaliacao).
- Uso de `AutoMirrored` em varios icones de navegacao.
- Contrastes geralmente bons em CTAs principais (azul sobre branco; branco sobre azul).
- Ponto de atencao: ha muitos tamanhos pequenos (`9sp`, `10sp`) em labels/metadados.

## 12) Inconsistencias atuais do design

1. **Tokenizacao parcial**
   - Parte relevante da UI usa cores/tamanhos hardcoded em vez de tokens globais.

2. **Variacao de azul de marca**
   - `#1565C0`, `#1E88E5`, `#1B65F6`, `#1C64F2`, `#1E54FA`, `#1976D2` aparecem com papeis semelhantes.

3. **Tipografia nao centralizada**
   - Sem escala unificada de `Typography`; fontes por tela variam bastante.

4. **Raio de canto heterogeneo**
   - Formas de `4dp` a `16dp` para componentes equivalentes (ex: botoes principais).

5. **Background por tela sem padrao unico**
   - Algumas telas usam `White`, outras `#F5F7FA`, outras `MaterialTheme.colorScheme.background`.

## 13) Resumo objetivo

- A identidade visual do ForLibrary hoje e **moderna, limpa e baseada em azul institucional**, com amplo uso de branco e cinzas.
- O design ja tem bons padroes de produto (cards, filtros, feedback semantico, navegacao por contexto).
- O principal gap tecnico de design system e a **falta de centralizacao completa** de tipografia, escala de espacamento e tokens semanticos de cor.
- Mesmo assim, visualmente, a aplicacao e coerente para MVP/prototipo e comunica bem os fluxos de Aluno e Admin.


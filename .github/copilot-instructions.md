# Copilot instructions for ForLibrary

## Build, test, and lint commands

- This is a single-module Android app (`:app`) built with the Gradle wrapper on Windows. Use `.\gradlew.bat` from the repository root.
- The README expects **JDK 17+**. In CLI sessions, make sure `JAVA_HOME` is configured before running Gradle.
- Common commands:
  - `.\gradlew.bat assembleDebug` - build the debug APK
  - `.\gradlew.bat testDebugUnitTest` - run local JVM unit tests
  - `.\gradlew.bat connectedDebugAndroidTest` - run instrumentation tests on a connected device/emulator
  - `.\gradlew.bat lintDebug` - run Android lint for the debug variant
- Run a single unit test:
  - `.\gradlew.bat testDebugUnitTest --tests "com.br.unifor.for_library.ExampleUnitTest"`
  - Or target a single test method: `.\gradlew.bat testDebugUnitTest --tests "com.br.unifor.for_library.ExampleUnitTest.addition_isCorrect"`
- Run a single instrumentation test:
  - `.\gradlew.bat connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.br.unifor.for_library.ExampleInstrumentedTest`
  - Or target a single test method: `.\gradlew.bat connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.br.unifor.for_library.ExampleInstrumentedTest.useAppContext`

## High-level architecture

- The current app is a **single Compose application module**. `MainActivity` only sets `ForLibraryTheme { ForLibraryApp() }`; almost all app wiring lives in `ForLibraryApp.kt`.
- `ForLibraryApp.kt` is the real integration point for the app:
  - owns the single `NavController`
  - declares the full `NavHost`
  - decides whether the **student** or **admin** bottom bar is shown
  - applies scaffold padding only for routes listed in `rotasComPadding`
  - handles shared flow behavior like logout popup and some route cleanup (`popUpTo`, `Uri.encode`)
- There are two major navigation contexts inside the same app:
  - **Aluno** flow: `Home`, `Acervo`, `Estante`, `Eventos`, `Perfil`
  - **Admin** flow: `Dashboard`, `Acervo`, `Moderação`, `Eventos`
- Shared UI and state live under `core/`:
  - `core/navigation/` contains the route model (`Rota`) and both bottom bars
  - `core/designsystem/` contains the app theme and shared colors/fallback palette
  - `core/components/` contains reusable Compose UI like `CapaLivro` and the advanced filter bottom sheet
  - `core/data/LivrosSalvosState.kt` is an in-memory shared state object for saved/favorite books plus a small shared catalog used by multiple screens
- The README says MVVM + Clean Architecture, but the current implementation is still closer to a **prototype UI layer**:
  - many screens keep `mock...` data in the same file
  - most state is local Compose state (`remember`, `rememberSaveable`, `derivedStateOf`)
  - navigation is passed in through callbacks instead of view models or repositories

## Key conventions

- **Route changes are multi-file changes.** When adding or renaming a screen, check all of these together:
  - `core/navigation/Rotas.kt`
  - `ForLibraryApp.kt`
  - `core/navigation/ForLibraryBottomBar.kt` or `core/navigation/AdminBottomBar.kt`
  - `rotasComPadding` / `rotasAdmin` in `ForLibraryApp.kt` if the new screen should keep bottom-bar layout behavior
- **Route names use Portuguese labels and snake_case path strings.** Parameterized routes are modeled as `object` entries in `Rota` with a `criarRota(...)` helper.
- **Screen/file naming is also Portuguese-first.** New screens usually use the `Tela...` prefix, and user-facing copy is written in Portuguese.
- **Cross-screen book favorites are not stored per screen.** Reuse `LivrosSalvosState` and `catalogoGlobal` instead of introducing duplicate local bookmark state.
- **Book covers should go through `CapaLivro`.** It resolves Open Library covers from ISBN and falls back to initials using `coresFallback`; several screens depend on this shared behavior.
- **This codebase favors stateful composables over separate state holders right now.** Existing screens commonly use `rememberSaveable` for form/search state and `derivedStateOf` for filtered lists; follow that pattern unless you are intentionally moving a flow to a real data layer.
- **Bug-fix annotations are kept inline.** Many files contain comments like `Bug 3` or `RF27.3`; preserve them when editing related flows because they document behavior that spans navigation and UI.

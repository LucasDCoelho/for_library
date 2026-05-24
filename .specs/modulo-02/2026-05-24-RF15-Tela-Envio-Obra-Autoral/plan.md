# Plano de Implementação - RF15 (Envio de Obra Autoral)

## 1. Localização e Escopo
- **Módulo:** `feature/aluno`
- **Arquivo:** `app/src/main/java/com/br/unifor/for_library/feature/aluno/presentation/obra/TelaEnvioObra.kt`
- **ViewModel:** `EnvioObraViewModel.kt`

## 2. Estrutura de Interface (Compose)
- **TopAppBar:** Título "Envio de Obra" e seta de navegação (`onBackClick`).
- **Layout (Column):**
    - **Ícone:** `Icons.Filled.CloudUpload` (`AzulPrimario`).
    - **Campos (RF15.3, RF15.4):**
        - `OutlinedTextField` para Título, Sinopse (multiline).
        - `ExposedDropdownMenuBox` para Gênero (FICÇÃO, ACADÊMICO, TECNOLOGIA, BIOGRAFIA, HISTÓRIA, DESIGN).
    - **Seletor de Arquivo (RF15.5, RF15.6):**
        - `Button` "Anexar PDF" usando `launcher.launch("application/pdf")`.
        - Se `uri != null`: Exibir `Text(arquivo.name)` + `IconButton(Icons.Filled.Delete)` para resetar o estado.
- **Botão Enviar (RF15.7):**
    - `enabled = isFormValid`.
    - Ao clicar: `viewModel.enviarObra(dados)`.

## 3. Lógica de Envio (ViewModel)
- **Validação:** Checar se os campos obrigatórios estão preenchidos e se a `uri` do PDF não é nula.
- **Fluxo Assíncrono:**
    1. `state = Loading`.
    2. `val uploadResult = supabase.storage["obras"].upload(fileName, fileBytes)`.
    3. `supabase.from("obras_autorais").insert(...)` (com `status = 'PENDENTE'`).
    4. `state = Success`. UI exibe mensagem persistente.
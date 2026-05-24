# Plano de Implementação - RF20 (Tela de Edição de Perfil)

## 1. Localização
- **Módulo:** `feature/aluno`
- **Arquivo Alvo:** `app/src/main/java/com/br/unifor/for_library/feature/aluno/presentation/perfil/TelaEdicaoPerfil.kt`
- **ViewModel:** `EdicaoPerfilViewModel`

## 2. Estrutura da Interface (RF20.3, RF20.4)
- **Campos Editáveis:** `OutlinedTextField` para "Nome de exibição" e "Biografia curta".
- **Campos Readonly:** `OutlinedTextField` (com `readOnly = true`) para Matrícula e E-mail, contendo um `trailingIcon` com `Icons.Filled.Lock`.
- **Foto (RF20.2):**
    - `Box` com `Image` (foto atual) + `IconButton` (câmera) posicionado no `BottomEnd`.
    - O clique no ícone dispara `launcher.launch("image/*")`.

## 3. Lógica de Persistência (ViewModel)
- **Upload de Imagem:**
    - Caso `novaFotoUri != null`:
        1. Upload para o bucket `avatars` no Supabase Storage.
        2. Obter a nova URL pública.
        3. Incluir URL no `updateMap`.
- **Persistência de Perfil:**
    - `supabase.from("usuarios").update({ ...updateMap }) { filter { eq("auth_user_id", auth.uid()) } }`.
- **Feedback:** Após sucesso, disparar `Snackbar("Alterações salvas!")` e retornar via `navController.popBackStack()`.
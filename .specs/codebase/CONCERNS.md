# CONCERNS.md — Débitos Técnicos e Riscos do ForLibrary

> Última sincronização com o código: 2026-05-29 (branch `lucasdev`).
> Severidade: 🔴 alta · 🟠 média · 🟡 baixa.

## Riscos ativos

### 🔴 C-01 — Credenciais do Supabase hardcoded e versionadas
`MainActivity.kt` define `SUPABASE_URL` e `SUPABASE_KEY` como constantes no código, commitadas no repositório. Mesmo sendo a *publishable key* (anon), credenciais não deveriam estar versionadas.
**Ação:** mover para `local.properties` → `BuildConfig` (ou `gradle.properties` não versionado). Garantir que a segurança real dependa de **RLS**, não do segredo da chave.

### 🔴 C-02 — Sem camada de testes
Apenas os exemplos gerados pelo template. Zero cobertura de ViewModels e regras. Ver `TESTING.md`.

### 🟠 C-03 — Ausência de camada Repository
Todos os ViewModels acessam o singleton global `supabase` diretamente (`feature/.../*ViewModel.kt`). Consequências: forte acoplamento ao SDK, ViewModels praticamente não testáveis (singleton de topo não mockável), e **DTOs duplicados** por feature (várias `data class` `@Serializable` representando `livros`/`usuarios`/`eventos`).
**Ação:** introduzir repositórios por domínio e DTOs/mapeadores compartilhados.

### 🟠 C-04 — Estado mock global em memória (`core/data/LivrosSalvosState`)
`catalogoGlobal` (catálogo hardcoded de 9 ISBNs) e `isbnsSalvos` (livros "salvos") vivem em um `object` em memória, com valores iniciais fixos, **sem persistência no Supabase**. "Salvos/favoritos" não sincronizam entre dispositivos e se perdem ao fechar o app.
**Ação:** migrar para a tabela `favoritos` (já prevista no schema).

### 🟠 C-05 — ChatBot (RF41) sem IA
`ChatBotViewModel.quandoPergunta()` responde por palavra-chave hardcoded com `delay(1500)` simulando digitação. Não há integração com nenhuma API de IA (era o BLOQ-02). Ver `INTEGRATIONS.md`.

### 🟠 C-06 — Exceções silenciadas
Vários `catch (e: Exception) {}` vazios engolem erros — ex.: `AcervoViewModel.carregarDadosIniciais`, `signOut` em `ForLibraryApp.kt`/`TelaConfiguracoesSistema`, parsing em `EventosViewModel`. Falhas ficam invisíveis ao usuário e ao log.

### 🟡 C-07 — Bugs conhecidos marcados no código
`ForLibraryApp.kt` tem comentários `// Bug 3` (notificações reusando contexto admin via `NotificacoesAdmin`) e `// Bug 7` (config admin). São issues conhecidos ainda não resolvidos; há também `TODO`s de navegação ("Continue lendo", "Ver todas atividades").

### 🟡 C-08 — Arquivos fora do pacote correto
`TelaGestaoEventos.kt`, `TelaAdicionarEvento.kt` e `TelaExclusaoObra.kt` estão no pacote raiz `feature` em vez de `feature/adm/eventos`. Quebra a organização "1 feature = 1 pacote".

### 🟡 C-09 — Nome de arquivo ≠ nome da função/classe
Quebra a convenção `Tela<Nome>.kt`: `TelaConfigAdm.kt`→`TelaConfiguracoesSistema`, `TelaSplash.kt`→`TelaSplashScreen`, `RecuperarSenha.kt`→`TelaRecuperarSenha`, `AdicionarLivro.kt`→`TelaAdicionarObra`. Também a rota `AvalicaoLivro` tem grafia incorreta.

### 🟡 C-10 — Telas/nomenclatura duplicadas
Dois `TelaConfiguracoes.kt` (`aluno/perfil/ui/` e `aluno/presentation/configuracoes/`); o do `perfil` não é usado no `NavHost`. Convivem `PopupLogout` e `TelaConfirmacaoLogout` para a mesma finalidade. Coexistência de `presentation/` e `ui/+viewmodel/` como organização.

### 🟡 C-11 — Sem injeção de dependência
Sem Hilt/Koin; ViewModels instanciados pelo `viewModel()` padrão acessando o singleton global. Aceitável no estágio atual, mas limita testabilidade (ligado a C-03).

## Resolvidos desde o último mapeamento (remover da lista de bloqueios)

- ✅ **BLOQ-01 — Supabase Storage**: integrado e em uso (buckets `imagens_livros`, `arquivos_livros`, `avatars`, `obras`).
- ✅ **File picker não funcional**: agora funcional nos fluxos de upload (cadastro/edição de livro, envio de obra, edição de perfil).
- ✅ **Gestão de Acervo admin sem backend**: implementada com Supabase (commit `dbf0b9b`) — cadastro, edição e listagem reais.
- ✅ **Acervo/Eventos do aluno em mock**: agora carregam dados reais do Supabase (`AcervoViewModel`, `EventosViewModel`) com filtros/ordenação server-side.
- ✅ **Tratamento de usuário bloqueado e mensagens de erro de auth**: centralizado em `AuthUtils.traduzirErroAuth` + `UsuarioBloqueadoException` (commit `29276a1`).

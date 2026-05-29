# ROADMAP.md — ForLibrary

## Milestones

### M1 — Autenticação e Acesso ✅ ~85%
**RF01–RF04 | Branch:** concluído

| Feature | Status | Pendências |
|---|---|---|
| Splash Screen (RF01) | ✔️ | RF01.3: verificação de sessão incerta |
| Login (RF02) | ⚠️ | RF02.3/2.4: labels erradas; RF02.8: erro não exibido; login ADM a revisar |
| Recuperação de Senha (RF03) | ⚠️ | RF03.3: textos do campo errados |
| Cadastro de Aluno (RF04) | ⚠️ | RF04.4: validação email sem lógica; RF04.8: redirect pós-cadastro incerto |

---

### M2 — Área do Aluno ⚠️ ~60%
**RF05–RF25 | Branch:** Ricardo (módulo-02)

| Feature | Status | Pendências críticas (P1) |
|---|---|---|
| Home (RF05) | ⚠️ | RF05.3: botão "Continue Lendo" sem redirect |
| Acervo Digital (RF06) | ⚠️ | RF07.4: filtros não aplicados; RF07.1: falta ícone X — RF06.7 ✅ corrigido |
| Filtros Avançados (RF07) | ⚠️ | RF07.4: filtros não aplicados; RF07.1: falta ícone X |
| Busca Vazia (RF08) | ⚠️ | RF08.4: texto bugado |
| Detalhes do Livro (RF09) | ⚠️ | RF10.5: progresso não salvo — bugs visuais ✅ corrigidos; ADR-008 ✅ aplicado |
| Leitor Digital (RF10) | ⚠️ | RF10.5: progresso não salvo; livro errado sendo exibido |
| Fim de Leitura (RF11) | ⚠️ | Textos bugados |
| Minha Estante (RF12) | ✔️ | — |
| Favoritos (RF13) | ⚠️ | ADR-008 ✅ aplicado — ícone bookmark + semântica "salvo" |
| Avaliação/Resenha (RF14) | ⚠️ | RF14.6: comportamento de envio bizarro |
| Envio Obra Autoral (RF15) | ⚠️ | RF15.5: PDF não abre; RF15.7: popup rápido demais |
| Eventos (RF16–RF17) | ✔️ | RF17.5: falta feedback de calendário |
| Notificações (RF18) | ❌ | Estrutura ok, nada funcional |
| Perfil (RF19) | ⚠️ | RF19.3: estatísticas não funcionais |
| Edição de Perfil (RF20) | ⚠️ | RF20.2: foto não funcional; RF20.5: save não funcional |
| Gamificação (RF21) | ⚠️ | RF21.2/21.3: não funcionais |
| Histórico Leitura (RF22) | ⚠️ | RF22.1: lupa não funcional |
| FAQ (RF23) | ⚠️ | Respostas faltando |
| Configurações Aluno (RF24) | ⚠️ | RF24.2: toggles não funcionais |
| Logout (RF25) | ✔️ | — |

---

### M3 — Área do Administrador ⚠️ ~45%
**RF26–RF41 | Branch:** lucasdev (em andamento)

| Feature | Status | Pendências críticas (P1) |
|---|---|---|
| Dashboard Admin (RF26) | ⚠️ | RF26.3/26.4/26.5: cards e atividades não funcionais |
| Gestão Acervo (RF27) | 🔄 **EM ANDAMENTO** | ViewModel sendo criado |
| Adição de Livro (RF28) | ✅ **Storage integrado** | RF28.2, RF28.5: upload funcional via Supabase Storage |
| Edição de Livro (RF29) | ✅ **Storage integrado** | RF29.3, RF29.4: upload de capa e PDF funcional |
| Exclusão Livro (RF30) | ✅ | RF30.3 implementado — popup + delete Supabase + Snackbar |
| Gestão Eventos (RF31) | ⚠️ | RF31.2: cards não funcionais |
| Criação/Edição Evento (RF32) | ⚠️ | RF32.4: DatePicker; RF32.6: upload capa; RF32.8: publicação |
| Moderação (RF33) | ✔️ | Navegação ok |
| Moderação Resenhas (RF34) | ⚠️ | Textos em inglês; aprovação/rejeição não funcionais |
| Análise Resenha (RF35) | ⚠️ | Aprovar/Rejeitar não funcionais |
| Moderação Obras (RF36) | ✔️ | RF36.2: texto a remover do app |
| Análise Obra (RF37) | ⚠️ | Download, aprovação, rejeição não funcionais |
| Gestão Usuários (RF38) | ✔️ | — |
| Detalhes Usuário (RF39) | ⚠️ | RF39.3: block não funcional; RF39.5: alerta não funcional |
| Configurações Sistema (RF40) | ✔️ | — |
| ChatBot (RF41) | ⚠️ | RF41.7/41.8/41.9: chat não funcional (IA não conectada) |

---

## Próximos Passos Imediatos (Sprint Atual)

**Foco: Execute — RF27/RF28/RF29 (ViewModels + Persistência Supabase)**

1. ✅ Criar `GestaoAcervoViewModel` com busca dinâmica e contagem
2. ✅ Criar `CadastroLivroViewModel` com validação e save no Supabase
3. ✅ Criar `EdicaoLivroViewModel` com carregamento e atualização
4. ✅ Conectar upload de capa (JPG/PNG) ao Supabase Storage (`imagens_livros`)
5. ✅ Conectar upload de PDF ao Supabase Storage (`arquivos_livros`)
6. ✅ Exclusão real (RF30.3) — `GestaoAcervoViewModel.deletarLivro()` + `PopupExclusaoObra` conectados; lista reativa + Snackbar de feedback

**RF27–RF30 concluídos. Próxima sprint:** Integrações de dados pendentes — gamificação (RF21), notificações (RF18), aprovar/rejeitar resenhas (RF35.4/RF35.5).

---

## Legenda

| Símbolo | Significado |
|---|---|
| ✔️ | Implementado e funcional |
| ⚠️ | Estrutura pronta, parcialmente funcional |
| ❌ | Não funcional / requer implementação |
| 🔄 | Em andamento na sprint atual |

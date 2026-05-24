# ForLibrary - Documento de Requisitos (requirements.md)

*Este documento foi gerado com base no levantamento minucioso da equipe ForLibrary.*
*Legenda de status da equipe: ✔️ (Implementado/Ok) | ❌ (Falta/Bug) | ❓ (Dúvida/Revisar)*

## MÓDULO 1: AUTENTICAÇÃO E ACESSO

| Cód | Requisito | Status / Obs | Prioridade |
|---|---|---|---|
| **RF01** | **Tela de Splash (Splash Screen)** | ✔️ | |
| RF01.1 | A tela deve exibir a logomarca do aplicativo (ForLibrary) centralizada. | ✔️ | 3 |
| RF01.2 | A tela deve ser exibida por exatamente 3 segundos antes de redirecionar o usuário. | ✔️ | 3 |
| RF01.3 | O sistema deve verificar em segundo plano se há uma sessão ativa; se sim, redirecionar para a Home correspondente; se não, redirecionar para a Tela de Login. | ❓ FALTA | 1 |
| **RF02** | **Tela de Login** | ✔️❓ Revisar login ADM | |
| RF02.1 | A tela deve possuir a logo do aplicativo em seu canto superior esquerdo. | ✔️ | 3 |
| RF02.2 | A tela deve possuir o título ForliBrary no topo da tela e logo abaixo um texto explicativo dizendo: “Acesse sua conta institucional para continuar.” | ✔️ | 3 |
| RF02.3 | A tela deve conter um campo de entrada com a mensagem “Matrícula ou e-mail institucional" e texto explicativo "Matrícula ou Email". | ❌ FALTA INSERIR INSTITUCIONAL | 1 |
| RF02.4 | A tela deve conter um campo de entrada com o texto “senha” logo acima e texto oculto "Senha" com ícone de "olho". | ❌ FALTA INSERIR SENHA ACIMA | 1 |
| RF02.5 | A tela deve conter um botão primário "Entrar". | ✔️ | 1 |
| RF02.6 | A tela deve conter um botão "Esqueceu a senha?" que redireciona para a Tela de Recuperação (RF03). | ✔️ | 2 |
| RF02.7 | A tela deve conter um botão "Primeiro Acesso? Cadastre-se" que redireciona para a Tela de Cadastro (RF04). | ✔️ | 2 |
| RF02.8 | O sistema deve exibir a mensagem de erro "Credenciais inválidas" em vermelho caso a autenticação falhe. | ❌ não implementado | 1 |
| **RF03** | **Tela de Recuperação de Senha** | ✔️ | |
| RF03.1 | A tela deve conter uma seta de voltar no canto superior esquerdo que redireciona para a tela de login. | ✔️ | 3 |
| RF03.2 | A tela deve possuir o título “Recuperar Senha” e texto explicativo: "Digite seu email institucional para receber o link..." | ✔️ | 2 |
| RF03.3 | A tela deve conter um campo de entrada para o Email institucional contendo o texto “Email institucional” logo acima e texto explicativo “Email” | ❌ FALTA ARRUMAR TEXTOS | 1 |
| RF03.4 | A tela deve conter um botão "Enviar Link". | ✔️ | 1 |
| RF03.5 | Ao validar o envio, o sistema deve exibir um Toast/Snackbar: "Link enviado com sucesso!" e retornar à Tela de Login. | ✔️ | 2 |
| **RF04** | **Tela de Cadastro de Aluno** | ✔️ | |
| RF04.1 | A tela deve conter uma seta de voltar no canto superior esquerdo para a tela de login. | ✔️ | 3 |
| RF04.2 | A tela deve conter o título “Crie sua conta” e texto explicativo: “Preencha os dados abaixo para acessar a biblioteca digital”. | ✔️ | 2 |
| RF04.3 | Campos para: Nome Completo, Matrícula, Email Institucional, Senha e Confirmar Senha. E-mail com texto explicativo. | ✔️ | 1 |
| RF04.4 | Validar se “Email” é institucional ou já existe. Mostrar: “Email já em uso” ou “Utilize um email institucional”. | ❓ NÃO SEGUE LÓGICA | 1 |
| RF04.5 | A tela deve conter um campo de entrada oculto para "Senha" e “Confirmar senha”, com ícone de "olho". | ✔️ | 2 |
| RF04.6 | A tela deve possuir um aviso confirmando termos de uso e política de privacidade de dados acadêmicos. | ✔️ | 2 |
| RF04.7 | A tela deve validar se "Senha" e "Confirmar Senha" coincidem. | ✔️ | 1 |
| RF04.8 | Botão "Cadastrar" que, em caso de sucesso, redireciona para o Login. | ✔️❓ | 1 |
| RF04.9 | Label: “Já possui uma conta? Fazer login”, redirecionando para Login. | ✔️ | 2 |

## MÓDULO 2: ÁREA DO ALUNO

| Cód | Requisito | Status / Obs | Prioridade |
|---|---|---|---|
| **RF05** | **Tela Home do Aluno** | | |
| RF05.1 | Cabeçalho com miniatura do perfil, "Olá, [Nome]!" e pontos de gamificação (redireciona para RF21). | ✔️ | 2 |
| RF05.2 | Símbolo de sino que redireciona para tela de notificações. | ✔️ | 2 |
| RF05.3 | Card "Continue Lendo" (capa, título, autor, progresso). Ao clicar, vai pro Leitor (RF10). | ❌ BOTÃO não implement. | 1 |
| RF05.4 | Carrossel "Destaques do Acervo". Texto “ver todos” redireciona para Acervo (RF06). | ✔️ | 1 |
| RF05.5 | Ícone de “bookmark” nos livros (Salvo). | ❌ DISCUTIR SE FAVORITOS | 2 |
| RF05.6 | Card "LIVROS LIDOS" com a quantidade de livros lidos. | ✔️ | 2 |
| RF05.7 | Card "TEMPO TOTAL" com tempo de leitura em horas. | ✔️ | 2 |
| RF05.8 | Botão com ícone de robô para PopUp do Chat Bot (RF41). | ✔️ | 1 |
| RF05.9 | Bottom Navigation Bar: Home, Acervo, Estante, Eventos e Perfil. | ✔️ | 1 |
| **RF06** | **Tela de Acervo Digital (Busca)** | ✔️ | |
| RF06.1 | Cabeçalho com miniatura, título "Acervo Digital" e sino de notificações. | ✔️ | 3 |
| RF06.2 | Search Bar no topo: "Busque por título ou autor". | ✔️❓ RESOLVER BUG | 1 |
| RF06.3 | Botão de "Filtros" que abre o Popup de Filtros Avançados (RF07). | ✔️ | 2 |
| RF06.4 | Filtros genéricos (Ex: Tudo, Ficção, Tecnologia, História e Design). | ✔️ | 2 |
| RF06.5 | Listagem de livros em Grid (2 colunas) com Capa, título e autor. | ✔️ | 1 |
| RF06.6 | Label “NOVO” para livros inseridos recentemente. | ❌ | 3 |
| RF06.7 | Ao clicar no livro, ir para Detalhes do Livro (RF09). | ❌ ERRO DE REFERÊNCIA | 1 |
| RF06.8 | Bottom Navigation Bar fixa. | ✔️ | 1 |
| **RF07** | **Popup (Bottom Sheet) de Filtros Avançados** | ✔️ | |
| RF07.1 | Título “Filtros Avançados”. Fechamento por swipe ou "X". | ❌ FALTA ÍCONE DE X | 3 |
| RF07.2 | Seção "GÊNERO LITERÁRIO" com Chips. | ✔️ | 1 |
| RF07.3 | Ordenação: "A-Z", "Z-A", "Mais Recentes" e "Melhor Avaliados". | ✔️ | 1 |
| RF07.4 | Botão "Aplicar Filtros" atualizando a lista. | ❌ FALTA APLICAR FILTRO | 1 |
| RF07.5 | Botão "Limpar" para resetar seleções. | ✔️ | 2 |
| **RF08** | **Tela de Resultados de Busca Vazia** | ✔️ | |
| RF08.1 | Cabeçalho com perfil, título e sino. | ✔️ | 3 |
| RF08.2 | Substitui o Grid se a busca não retornar resultados. | ✔️ | 2 |
| RF08.3 | Ilustração amigável de um livro vazio. | ✔️ | 3 |
| RF08.4 | Texto: "Ops! Silêncio na biblioteca... Nenhum livro encontrado". | ✔️❓ MIGUEL: TÁ BUGADO | 3 |
| RF08.5 | Texto explicativo: “Tente usar palavras-chave...”. | ✔️ | 3 |
| RF08.6 | Botão "Limpar filtros" que redireciona ao RF06. | ✔️ | 2 |
| RF08.7 | Bottom Navigation Bar fixa. | ✔️ | 1 |
| **RF09** | **Tela de Detalhes do Livro** | ❓ TELA BUGADA | |
| RF09.1 | Cabeçalho "Detalhes do livro", seta de voltar e sino. | ✔️❓ | 3 |
| RF09.2 | Capa ampliada, Título, Autor, Gênero e Ano. | ✔️ | 1 |
| RF09.3 | Nota Média (estrelas) e quantidade de avaliações. | ✔️ | 2 |
| RF09.4 | Botão "Ler Agora" que abre o Leitor (RF10). | ✔️ | 1 |
| RF09.5 | Botão secundário de ícone (coração) para "Adicionar aos Favoritos". | ❌ SUBSTITUIR ÍCONE | 1 |
| RF09.6 | Sinopse expansível ("ler mais"). | ✔️ | 2 |
| RF09.7 | "Avaliações de Usuários" listando resenhas e data relativa. | ✔️ (TEXTOS BUGADOS) | 2 |
| **RF10** | **Tela de Leitor Digital (PDF/ePub)** | ❓ LIVRO ERRADO | |
| RF10.1 | Seta de voltar e título do livro. | ✔️ | 3 |
| RF10.2 | Renderizar o arquivo em tela cheia. | ✔️❓ Renderiza? | 1 |
| RF10.3 | Virar páginas via swipe horizontal. | ✔️❓ Viram mesmo? | 1 |
| RF10.4 | Slider de paginação inferior ao tocar no centro (páginas e total). | ✔️ | 1 |
| RF10.5 | Salvar automaticamente a página atual ao sair. | ❓ IMPLEMENTAR | 1 |
| **RF11** | **Popup de Fim de Leitura** | ✔️ | |
| RF11.1 | Acionado automaticamente ao atingir a última página. | ✔️ | 2 |
| RF11.2 | Ícone e mensagem "Parabéns! Você concluiu a leitura e ganhou X pontos." | ✔️ (TEXTOS BUGADOS) | 3 |
| RF11.3 | Botão "Avaliar Livro" (Redireciona para RF14). | ✔️ | 2 |
| RF11.4 | Botão "Fechar" retornando aos detalhes. | ✔️ | 2 |
| RF11.5 | Label "Leitura finalizada". | ✔️ | 3 |
| **RF12** | **Tela "Minha Estante"** | ✔️ | |
| RF12.1 | Título "Minha Estante", atalho Histórico (RF22) e lupa para pesquisar. | ✔️ | 2 |
| RF12.2 | Abas "Lendo" e "Favoritos". | ✔️ | 1 |
| RF12.3 | Aba "Lendo": Lista vertical com barra de progresso (páginas/%). | ✔️ | 1 |
| RF12.4 | Bottom Navigation Bar fixa. | ✔️ | 1 |
| **RF13** | **Tela de Favoritos (Aba da Estante)** | ✔️ | |
| RF13.1 | Título "Minha Estante" e atalhos. | ❌ TIRAR LUPA | 2 |
| RF13.2 | Lista em Grid de livros curtidos. | ❌ MUDAR ÍCONE | 1 |
| RF13.3 | Desmarcar favorito no ícone do coração. | ❌ MUDAR ÍCONE | 1 |
| RF13.4 | Bottom Navigation Bar fixa. | ✔️ | 1 |
| **RF14** | **Tela/Popup de Avaliação e Resenha** | ✔️ | |
| RF14.1 | Título "Avaliação" e nome do livro no topo. | ✔️ (TEXTO BUGADO) | 3 |
| RF14.2 | Ícone de “X” para fechar popup. | ✔️ | 3 |
| RF14.3 | Card do livro (capa, título, autor, ano). | ✔️ (FALTA CAPA EXATA) | 3 |
| RF14.4 | 5 estrelas clicáveis (nota obrigatória). | ✔️ | 1 |
| RF14.5 | Campo texto "SUA RESENHA" (até 500 caracteres, opcional). | ✔️ | 2 |
| RF14.6 | Botão "Enviar Resenha" com mensagens de status. | ❌ TA BIZARRO NO APP | 1 |
| RF14.7 | Botão "Cancelar" para voltar ao fim de leitura. | ✔️ | 2 |
| **RF15** | **Tela de Envio de Obra Autoral** | ✔️ | |
| RF15.1 | Título “Envio de Obra” e seta de voltar. | ✔️ | 3 |
| RF15.2 | Ícone de nuvem e texto sobre envio ao acervo. | ✔️ | 3 |
| RF15.3 | Campos: Título, Gênero e Sinopse Curta (com placeholders). | ✔️ | 1 |
| RF15.4 | Dropdown de gêneros. | ✔️ | 1 |
| RF15.5 | Botão "Anexar PDF" abrindo gerenciador do celular. | ❌ NÃO ABRE | 1 |
| RF15.6 | Exibir arquivo anexado com ícone de lixeira. | ✔️ | 2 |
| RF15.7 | Botão "Enviar Obra" (valida e envia mensagem). | ❌ POP UP RÁPIDO | 1 |
| **RF16** | **Tela de Eventos Literários** | ✔️ | |
| RF16.1 | Título “Eventos Literários”, perfil e sino. | ✔️ | 3 |
| RF16.2 | Filtros horizontais por tipo (Todos, Workshops, etc). | ✔️ | 2 |
| RF16.3 | Lista em cards verticais. | ✔️ | 1 |
| RF16.4 | Card exibe banner, data, título, local e botão seta. | ✔️ | 1 |
| RF16.5 | Seta abre Detalhes do Evento (RF17). | ✔️ | 1 |
| RF16.6 | Bottom Navigation Bar fixa. | ✔️ | 1 |
| **RF17** | **Tela de Detalhes do Evento** | ✔️ | |
| RF17.1 | Nome do evento, filtro, imagem e seta de voltar. | ✔️ | 3 |
| RF17.2 | Data, horário de início/término e fuso horário. | ✔️ | 1 |
| RF17.3 | Localização com endereço e complemento. | ✔️ | 1 |
| RF17.4 | Seção "Sobre o evento". | ✔️ | 2 |
| RF17.5 | Botão "Adicionar ao Meu Calendário" (nativo Android). | ❌ FALTA MSG VERDINHA | 2 |
| **RF18** | **Tela de Notificações** | ✔️ | |
| RF18.1 | Título “Notificações” e seta de voltar. | ✔️ | 3 |
| RF18.2 | Botão "Marcar todas como lidas". | ❌ NÃO FUNCIONAL | 2 |
| RF18.3 | Listar em ordem cronológica com horário e contexto. | ❌ NÃO FUNCIONAL | 1 |
| RF18.4 | Labels temporais (“Hoje”, “Ontem”, “Anteriores”). | ❌ NÃO FUNCIONAL | 2 |
| RF18.5 | Notificações não lidas com fundo destacado e círculo azul. | ❌ NÃO FUNCIONAL | 3 |
| RF18.6 | Marcar como lida ao clicar (muda UI). | ❌ NÃO FUNCIONAL | 1 |
| **RF19** | **Tela de Perfil do Aluno** | ✔️ | |
| RF19.1 | Título “Perfil do Aluno”. | ✔️ | 3 |
| RF19.2 | Foto de perfil, nome completo e matrícula. | ✔️ | 1 |
| RF19.3 | Seção de livros lidos, resenhas e pontos. | ❌ NÃO FUNCIONAL | 2 |
| RF19.4 | 5 botões de utilidades ("Editar Perfil", "Envio", "Dúvidas", "Configurações", "Sair"). | ✔️ | 1 |
| RF19.5 | Bottom Navigation Bar fixa. | ✔️ | 1 |
| **RF20** | **Tela de Edição de Perfil** | ✔️ | |
| RF20.1 | Título “Editar Perfil” e seta de voltar. | ✔️ | 3 |
| RF20.2 | Foto com ícone de alterar foto (abre galeria). | ❌ NÃO FUNCIONAL | 1 |
| RF20.3 | Campos editáveis de nome e biografia. | ✔️ | 1 |
| RF20.4 | Matrícula e E-mail bloqueados (readonly) com cadeado. | ✔️ | 2 |
| RF20.5 | Botão "Salvar Alterações" no DB com Toast. | ❌ NÃO FUNCIONAL | 1 |
| **RF21** | **Tela de Gamificação (Meus Pontos)** | ✔️ | |
| RF21.1 | Título “Meus Pontos” e seta de voltar. | ✔️ | 3 |
| RF21.2 | Medidor de pontos totais e níveis (1 a 5). | ❌ NÃO FUNCIONAL | 1 |
| RF21.3 | Histórico de pontos com data. | ❌ NÃO FUNCIONAL | 2 |
| RF21.4 | Seção de explicação das regras de pontos. | ✔️ | 3 |
| **RF22** | **Tela de Histórico de Leitura** | | |
| RF22.1 | Título, voltar e lupa de busca de obras no histórico. | ❌ LUPA NÃO FUNCIONAL | 2 |
| RF22.2 | Lista de "Livros Concluidos" ordenados por data. | ✔️ | 1 |
| **RF23** | **Tela de FAQ / Dúvidas** | | |
| RF23.1 | Título “Dúvidas Frequentes” e voltar. | ✔️ (bugs de texto) | 3 |
| RF23.2 | Texto introdutório. | ✔️ | 3 |
| RF23.3 | Lista expansível (accordion) de perguntas/respostas. | ✔️ (FALTA RESPOSTAS) | 2 |
| **RF24** | **Tela de Configurações (Aluno)** | | |
| RF24.1 | Título "Configurações" e voltar. | ✔️ | 3 |
| RF24.2 | Switches para "Notificações Push" e "Tema Escuro". | ❌ NÃO FUNCIONAL | 2 |
| RF24.3 | Botões "Termos de Uso" e "Política de Privacidade". | ✔️ (FALTA TEXTO) | 2 |
| RF24.4 | Botão "Sair" (Abre RF25). | ✔️ | 1 |
| **RF25** | **Popup de Confirmação de Logout** | | |
| RF25.1 | Acionado ao clicar "Sair". | ✔️ | 2 |
| RF25.2 | Mensagem de confirmação e texto explicativo. | ✔️ | 3 |
| RF25.3 | Botões "Cancelar" e "Sim, Sair". Limpa sessão. | ✔️ | 1 |

## MÓDULO 3: ÁREA DO ADMINISTRADOR (Gestão)

| Cód | Requisito | Status / Obs | Prioridade |
|---|---|---|---|
| **RF26** | **Tela Dashboard (Home) do Administrador** | | |
| RF26.1 | Saudação "Olá, Admin!", sino de notificações e engrenagem. | ✔️ | 2 |
| RF26.2 | Título e subtítulo "Visão geral do sistema e métricas...". | ✔️ | 3 |
| RF26.3 | 4 Cards: "Livros", "Alunos", "Resenhas" (vermelho) e "Obras" (verde). | ❌ NÃO FUNCIONAL | 2 |
| RF26.4 | Feed das 4 atividades recentes do admin. | ❌ NÃO FUNCIONAL | 2 |
| RF26.5 | Botão "Ver todas" para listar atividades. | ❌ NÃO FUNCIONAL | 2 |
| RF26.6 | Bottom Navigation Bar: Dashboard, Acervo, Moderação, Eventos. | ✔️ | 1 |
| **RF27** | **Tela de Gestão de Acervo (Listagem Admin)** | | |
| RF27.1 | Título "Gestão de Acervo" e voltar. | ❌ NÃO FAZ SENTIDO SETA | 3 |
| RF27.2 | Busca por título/autor e total de livros. | ✔️ | 1 |
| RF27.3 | Lista com Capa, Título, Autor, "Editar" e "Excluir" (abre RF30). | ✔️ | 1 |
| RF27.4 | Botão Editar abre RF29. | ✔️ | 1 |
| RF27.5 | FAB (+) inferior direito para novo livro (RF28). | ✔️ | 1 |
| RF27.6 | Bottom Navigation Bar. | ✔️ | 1 |
| **RF28** | **Tela de Adição de Novo Livro** | | |
| RF28.1 | Título "Adição de Obra" e voltar. | ✔️ | 3 |
| RF28.2 | Botão pontilhado para upload de Capa (JPG/PNG). | ❌ NÃO FUNCIONAL | 1 |
| RF28.3 | Campos: Título, Autor, Gênero, Ano, Páginas, Sinopse. | ✔️ | 1 |
| RF28.4 | Dropdown de gênero e multiline de sinopse. | ❌ CONSERTAR GÊNEROS | 1 |
| RF28.5 | Botão "Anexar Arquivo (PDF/ePub)". | ❌ NÃO FUNCIONAL | 1 |
| RF28.6 | Botões "Cancelar" e "Salvar Obra" (atualiza BD). | ❌ NÃO FUNCIONAL | 1 |
| RF28.7 | Bottom Navigation Bar. | ✔️ | 1 |
| **RF29** | **Tela de Edição de Livro** | | |
| RF29.1 | Título "Editar Obra" e voltar. | ✔️ | 3 |
| RF29.2 | Exibe capa, título e botão "Trocar capa". | ❌ IGUAL RF29.4 | 1 |
| RF29.3 | Campos pré-preenchidos, com opção de trocar PDF. | ❌ NÃO FUNCIONAL | 1 |
| RF29.4 | Botão "Trocar Capa". | ❌ NÃO FUNCIONAL | 2 |
| RF29.5 | Botões "Cancelar" e "Atualizar Obra". | ❌ NÃO FUNCIONAL | 1 |
| RF29.6 | Bottom Navigation Bar. | ✔️ | 1 |
| **RF30** | **Popup de Confirmação de Exclusão de Livro** | | |
| RF30.1 | Acionado ao clicar na lixeira no RF27. | ✔️ | 2 |
| RF30.2 | Título e mensagem permanente de exclusão. | ✔️ | 3 |
| RF30.3 | Botões "Cancelar" e "Excluir" (vermelho) removendo do DB. | ❌ NÃO FUNCIONAL | 1 |
| **RF31** | **Tela de Gestão de Eventos** | | |
| RF31.1 | Título e total de eventos. | ✔️ (QTD BUGADA) | 3 |
| RF31.2 | Card de evento com editar e excluir. | ❌ NÃO FUNCIONAL | 1 |
| RF31.3 | FAB (+) para novo evento (RF32). | ✔️ | 1 |
| **RF32** | **Tela de Criação/Edição de Evento** | | |
| RF32.1 | Título "Criar evento". | ✔️ | 3 |
| RF32.2 | Subtítulo e texto descritivo. | ✔️ | 3 |
| RF32.3 | Campos de Título e Descrição. | ✔️ | 1 |
| RF32.4 | DatePicker e TimePicker para Data e Horário. | ❌ NÃO FUNCIONAL | 1 |
| RF32.5 | Campo para "Local" ou "Link". | ✔️ | 1 |
| RF32.6 | Upload de "Imagem de Capa (Opcional)". | ❌ NÃO FUNCIONAL | 2 |
| RF32.7 | Botão "Cancelar". | ✔️ | 2 |
| RF32.8 | Botão "Publicar Evento" e notificar alunos. | ❌ NÃO FUNCIONAL | 1 |
| **RF33** | **Tela de Moderação** | | |
| RF33.1 - 6 | Cards e atalhos para Usuários, Obras e Resenhas. | ✔️ | 1/2 |
| RF33.7 - 8 | Bottom Navigation Bar com destaque em "Moderação". | ✔️ | 1/3 |
| **RF34** | **Tela de Moderação de Resenhas** | | |
| RF34.1 | Título "Moderação de resenhas" e voltar. | ✔️ | 3 |
| RF34.2 | "X Reviews pendentes". | ❌ EM INGLÊS | 2 |
| RF34.3 | Filtro no lado direito. | ❌ NÃO ENTENDIDO | 2 |
| RF34.4 | Lista de resenhas "Pendentes". | ❌ EM INGLÊS | 1 |
| RF34.5 | Card mostra aluno, livro, nota e trecho da resenha. | ✔️ | 2 |
| RF34.6 | Clique redireciona para RF35. | ✔️ | 1 |
| RF34.7 | Botão "Carregar mais resenhas". | ❌ NÃO FUNCIONAL | 2 |
| RF34.8 | Bottom Navigation Bar. | ✔️ | 1 |
| **RF35** | **Tela de Análise de Resenha** | | |
| RF35.1 | Título "Analisar Resenha" e voltar. | ✔️ | 3 |
| RF35.2 | Texto completo, foto do aluno, data, hora, nota. | ✔️ | 1 |
| RF35.3 | Lembrete de diretrizes em azul. | ✔️ | 3 |
| RF35.4 | Botão verde "Aprovar Resenha" (Dá pontos, publica). | ❌ NÃO FUNCIONAL | 1 |
| RF35.5 | Botão vermelho "Rejeitar Resenha". | ❌ NÃO FUNCIONAL | 1 |
| RF35.6 | Motivo da rejeição opcional. | ❌ CÓDIGO DESCONHEC. | 2 |
| **RF36** | **Tela de Moderação de Obras Autorais** | | |
| RF36.1 | Título "Moderação de Obras" e voltar. | ✔️ | 3 |
| RF36.2 | Título e explicação (Aguardando revisão editorial). | ❌ TIRAR DO APP | 3 |
| RF36.3 | Lista das obras submetidas (RF15). | ✔️ | 1 |
| RF36.4 | Card com Título, Gênero, Autor, Matrícula e status. | ✔️ | 1 |
| RF36.5 | Botão "Revisar" que abre RF37. | ✔️ | 1 |
| RF36.6 | Card de "TOTAL PENDENTE". | ✔️ | 2 |
| RF36.7 | Bottom Navigation Bar. | ✔️ | 1 |
| **RF37** | **Tela de Análise de Obra Autoral** | | |
| RF37.1 | Título "Analisar Obra" e voltar. | ✔️ | 3 |
| RF37.2 | Card do livro (capa, título, gênero, sinopse). | ✔️ | 1 |
| RF37.3 | Botão "Baixar PDF para Análise". | ❌ NÃO FUNCIONAL | 1 |
| RF37.4 | Botões "Aprovar" e "Rejeitar" (Publica ou inativa). | ❌ NÃO FUNCIONAL | 1 |
| RF37.5 | Lembrete das diretrizes editoriais. | ✔️ | 3 |
| RF37.6 | Motivo de rejeição opcional. | ❌ NÃO FUNCIONAL | 2 |
| **RF38** | **Tela de Gestão de Usuários (Alunos)** | | |
| RF38.1 | Título "Gestão de Usuários" e voltar. | ✔️ | 3 |
| RF38.2 | SearchBar por Nome/Matrícula. | ✔️ | 1 |
| RF38.3 | Lista de alunos e "X Usuários encontrados". | ✔️ | 1 |
| RF38.4 | Card com Foto, Nome, Matrícula e Status. | ✔️ | 1 |
| RF38.5 | Clique no aluno abre RF39. | ✔️ | 1 |
| RF38.6 | Bottom Navigation Bar. | ✔️ | 1 |
| **RF39** | **Pop-up de Detalhes do Usuário** | | |
| RF39.1 | Abre acima de RF38. | ✔️ | 2 |
| RF39.2 | Título e "Dados pessoais" completos. | ✔️ | 1 |
| RF39.3 | Toggle para "Bloquear Acesso" impedindo login. | ❌ NÃO FUNCIONAL | 1 |
| RF39.4 | Botão "Fechar". | ✔️ | 2 |
| RF39.5 | Alerta vermelho de resenhas inadequadas. | ❌ NÃO FUNCIONAL | 1 |
| **RF40** | **Tela de Configurações do Sistema (Admin)** | | |
| RF40.1 | Título "Configurações do Sistema" e voltar. | ✔️ | 3 |
| RF40.2 | Campo de "Gamificação" para editar pontos globais. | ✔️ | 1 |
| RF40.3 | Botão de Logout "SAIR" com aviso. | ✔️ | 1 |
| **RF41** | **Tela de Chat Bot** | | |
| RF41.1 | Cabeçalho azul "Assistente ForLibrary". | ✔️ | 2 |
| RF41.2 | Ícone "X" para fechar chat. | ✔️ | 2 |
| RF41.3 | Foto/Ícone do assistente. | ✔️ | 3 |
| RF41.4 | Mensagem inicial automática. | ✔️ | 1 |
| RF41.5 | Área de conversa (balões). | ✔️ | 1 |
| RF41.6 | Campo de entrada de texto. | ✔️ | 1 |
| RF41.7 | Botão de envio (avião de papel). | ❌ NÃO FUNCIONAL | 1 |
| RF41.8 | Múltiplas interações sem reload. | ❌ NÃO FUNCIONAL | 1 |
| RF41.9 | Balões de cores distintas. | ❌ NÃO FUNCIONAL | 1 |

## REQUISITOS NÃO FUNCIONAIS (RNF)

| Cód | Requisito | Prioridade |
|---|---|---|
| **RNF01** | **O sistema deve garantir a segurança dos dados dos usuários.** | 1 |
| RNF01.1 | Proteção conforme LGPD. | 1 |
| RNF01.2 | Permitir exclusão de dados pessoais. | 2 |
| **RNF02** | **O sistema deve apresentar bom desempenho nas interações.** | 1 |
| RNF02.1 | Tempo de resposta < 3s em 90% das interações. | 1 |
| RNF02.2 | Carregamento de listas em < 2s. | 1 |
| RNF02.3 | Splash Screen visível por exatamente 3s. | 2 |
| RNF02.4 | Suportar min 300 usuários simultâneos. | 2 |
| **RNF03** | **Compatibilidade Android.** | 1 |
| RNF03.1 | Desenvolvido em Kotlin / Android Studio. | 1 |
| RNF03.2 | Compatível com API 26 (Android 8.0) ou superior. | 1 |
| RNF03.3 | Responsividade em telas diversas. | 2 |
| **RNF04** | **Usabilidade.** | 1 |
| RNF04.1 | Padrões de design consistentes. | 1 |
| RNF04.2 | Feedback visual (erros, loadings). | 1 |
| RNF04.3 | Acessibilidade e navegação clara. | 2 |
| **RNF05** | **Manutenção e código estruturado.** | 1 |
| RNF05.1/2 | Boas práticas e padrões. | 1 |
| **RNF06** | **Integração Externa (APIs para DB e Autenticação).** | 1 |
| **RNF07** | **Suporte a Notificações Push atempadas.** | 2 |
| **RNF08** | **Armazenamento.** | 1 |
| RNF08.1 | Salvar localmente o progresso de leitura. | 1 |
| RNF08.2 | Limitar armazenamento local a 200MB. | 2 |
| **RNF09** | **Consumo otimizado de bateria e processamento.** | 2 |
| **RNF10** | **Resiliência de Rede (Tolerância Offline e Auto-reconnect).** | 1 |
| **RNF11** | **Escalabilidade (Suporte ao aumento de usuários).** | 1 |
| **RNF12** | **Uso eficiente de dados móveis e Cache.** | 1 |
| **RNF13** | **Usabilidade (Realizar tarefa em 3 passos, s/ treinamento).** | 1 |
MÓDULO 1: AUTENTICAÇÃO E ACESSO





Cód

RF01

RF01.1

RF01.2





Requisito ✔️

Tela de Splash (Splash Screen) ✔️

A tela deve exibir a logomarca do aplicativo (ForLibrary) centralizada. ✔️

A tela deve ser exibida por exatamente 3 segundos antes de redirecionar o usuário. ✔️





Prioridade



3

3





Cód

Requisito ✔️

Prioridade

RF01.3

O sistema deve verificar em segundo plano se há uma sessão ativa; se sim, redirecionar para a Home correspondente (Aluno ou Admin); se não, redirecionar para a Tela de Login. ❓  FALTA

1



RF02

RF02.1





Tela de Login ✔️❓ (como ficou a parte do login ADM? Revisar amanhã)

A tela deve possuir a logo do aplicativo em seu canto superior esquerdo. ✔️







3



RF02.2

A tela deve possuir o título ForliBrary no topo da tela e logo abaixo um texto explicativo dizendo: “Acesse sua conta institucional para continuar.” ✔️

3

RF02.3

A tela deve conter um campo de entrada com a mensagem “Matrícula ou e-mail institucional" logo acima do campo e o texto explicativo no campo dizendo "Matrícula ou Email". ❌ FALTA INSERIR INSTITUCIONAL NO TEXTO EXPLICATIVO

1

RF02.4

A tela deve conter um campo de entrada com o texto “senha” logo acima e o texto oculto para o texto explicativo "Senha" dentro do campo, com um ícone de "olho" para alternar a visibilidade da senha. ❌ FALTA INSERIR SENHA NO TEXTO ACIMA DO CAMPO

1



RF02.5

RF02.6

RF02.7





A tela deve conter um botão primário "Entrar". ✔️

A tela deve conter um botão "Esqueceu a senha?" que redireciona para a Tela de Recuperação (RF03). ✔️

A tela deve conter um botão "Primeiro Acesso? Cadastre-se" que redireciona para a Tela de Cadastro (RF04). ✔️ (Apenas cadastre-se é o botão)





1

2

2



RF02.8

O sistema deve exibir a mensagem de erro "Credenciais inválidas" em vermelho abaixo dos campos caso a autenticação falhe. ❌ não implementado no app

1

RF03

Tela de Recuperação de Senha ✔️







Cód

RF03.1





Requisito ✔️

A tela deve conter uma seta de voltar no canto superior esquerdo que, ao ser clicada, redireciona para a tela de login. ✔️





Prioridade

3



RF03.2

A tela deve possuir o título “Recuperar Senha” e logo abaixo um texto explicativo dizendo: "Digite seu email institucional para receber o link de recuperação." ✔️

2



RF03.3

RF03.4

RF03.5

RF04

RF04.1





A tela deve conter um campo de entrada para o Email institucional contendo o texto “Email institucional” logo acima do campo e o texto explicativo “Email” ❌FALTA COLOCAR O TEXTO EM CIMA E O TEXTO EXPLICATIVO DENTRO DO CAMPO

A tela deve conter um botão "Enviar Link". ✔️

Ao validar o envio, o sistema deve exibir um Toast/Snackbar: "Link enviado com sucesso!" e retornar à Tela de Login. ✔️

Tela de Cadastro de Aluno ✔️

A tela deve conter uma seta de voltar no canto superior esquerdo que, ao ser clicada, redireciona para a tela de login. ✔️





1

1

2



3



RF04.2

A tela deve conter o título “Crie sua conta” e logo abaixo um texto explicativo dizendo: “Preencha os dados abaixo para acessar a biblioteca digital”. ✔️

2

RF04.3

A tela deve conter campos para: Nome Completo, Matrícula, Email Institucional, Senha e Confirmar Senha. O campo de e-mail possui um texto explicativo logo abaixo dele dizendo: “Utilize um email institucional” ✔️

1

RF04.4

A tela deve validar se “Email” é institucional ou se já existe. Caso não esteja validado ou não exista, aparecerão mensagens dizendo: “Email já em uso” ou “Utilize um email institucional”. ❓ TEM NO APP PORÉM NÃO SEGUE NENHUMA LOGICA

1

Cód

Requisito ✔️

Prioridade

RF04.5

A tela deve conter um campo de entrada de texto oculto para "Senha" e “Confirmar senha”, com um ícone de "olho" para alternar a visibilidade da senha. ✔️

2

RF04.6

A tela deve possuir um aviso que indica a confirmação de termos de uso e política de privacidade de dados acadêmicos. ✔️

2

RF04.7

A tela deve validar se "Senha" e "Confirmar Senha" coincidem. Caso não coincidam, aparecerá a mensagem “Senhas não coincide”. ✔️

1

RF04.8

A tela deve possuir um botão "Cadastrar" que, em caso de sucesso, redireciona para o Login. ✔️❓ (motivo da interrogação: não sei se redireciona ou não)

1

RF04.9

A tela deve possuir uma label dizendo: “Já possui uma conta? Fazer login”, caso o usuario clique no “Fazer Login”, será redirecionado para Login. ✔️ (apenas fazer login é o botão)

2





MÓDULO 2: ÁREA DO ALUNO (Ricardo)







Cód

RF05





Requisito





Prioridade







Tela Home do Aluno.



RF05.1

A tela deve exibir um cabeçalho com a foto em miniatura do perfil, o texto “bem vindo” e logo abaixo dele a saudação "Olá, [Nome]!" e a quantidade de pontos atuais de gamificação que, ao serem clicados, redirecionam o usuário para Meus Pontos (RF21). ✔️

2

RF05.2

A tela deve exibir um símbolo de sino que, ao ser clicado, redireciona o usuário para a tela de notificações. ✔️

2



Cód

Requisito ✔️

Prioridade

RF05.3

A tela deve conter um card "Continue Lendo" exibindo a capa, título do livro, nome do autor, capítulo atual e uma barra de progresso (em %) do último livro aberto. Ao clicar, redireciona para o Leitor (RF10). ❌ BOTÃO não implementado

1

RF05.4

A tela deve exibir um carrossel horizontal (RecyclerView) com o título "Destaques do Acervo" mostrando a capa, título e autor de cada um dos livros. O carrossel possui um texto “ver todos” que, ao ser clicado, redireciona o usuário para a tela RF06 (acervo digital). ✔️

1

RF05.5

Os livros mostrados no carrossel devem possuir um ícone de “bookmark” em seu canto superior direito, indicando que o livro está marcado como “Salvo”. ❌ TEM QUE DISCUTIR SE A GNT VAI TER SALVOS OU FAVORITOS.

2

RF05.6

A tela deve exibir um card de livros lidos, contedo, um icone de livro, o título “LIVROS LIDOS” e logo abaixo informando quantos livros o usuário já leu. ✔️

2

RF05.7

A tela deve exibir um card de tempo total, contedo, um icone de relogio, o título “TEMPO TOTAL” e logo abaixo informando o tempo de leitura do usuário em horas no aplicativo. ✔️

2

RF05.8

A tela deve conter um botão com um ícone amigável de robô que, ao ser clicado, acione o PopUp do Chat Bot (RF41). ✔️





RF05.9

RF06





A tela deve possuir uma Bottom Navigation Bar fixa com ícones para: Home, Acervo, Estante, Eventos e Perfil. ✔️

Tela de Acervo Digital (Busca). ✔️





1





RF06.1

A tela deve exibir um cabeçalho com a foto em miniatura do perfil, o nome da tela “Acervo Digital” e ao lado um símbolo de sino que, ao ser clicado, redireciona o usuário para a tela de notificações. ✔️️

3





Cód

RF06.2

RF06.3

RF06.4

RF06.5

RF06.6

RF06.7

RF06.8

RF07





Requisito ✔️

A tela deve conter uma Search Bar no topo com o placeholder "Busque por título ou autor". ✔️❓RESOLVER ESSE BUG QUE O TCHÊ COMENTOU

A tela deve conter um botão com um ícone de "Filtros" ao lado da barra de busca que abre o Popup de Filtros Avançados (RF07). ✔️

A tela deve conter, logo abaixo da search bar, os filtros mais genéricos. Ex: Tudo, Ficção, Tecnologia, História e Design. ✔️

O corpo da tela deve listar os livros em formato de Grid (2 colunas) exibindo Capa, título e nome do autor. ✔️

Caso o livro tenha sido inserido recentemente no acervo, deverá conter uma label “NOVO”, no canto inferior direito. ❌

Ao clicar em um livro, o sistema deve redirecionar para a Tela de Detalhes do Livro (RF09). ❌ ( FALTA FAZER QUE O DETALHES SE REFIRA AO LIVRO CLICADO E NÃO AO PLACEHOLDER GENERICO)

A tela deve possuir uma Bottom Navigation Bar fixa com ícones para: Home, Acervo, Estante, Eventos e Perfil. ✔️ (No app esta certo é so printar e colocar no pdf dps)

Popup (Bottom Sheet) de Filtros Avançados. ✔️





Prioridade

1

2

2

1

3

1

1





RF07.1

O PopUp deve conter o título “Filtros Avançados”. O fechamento do PopUp deve ocorrer por meio de um gesto de deslizar para baixo ou clicando no ícone de “X” no canto superior direito da tela, fazendo com que o usuário volte para a tela de acervo. ❌ FALTA O ICONE DE X

3

RF07.2

O popup deve conter uma seção com o título “GÊNERO LITERÁRIO” e um agrupamento de Chips (botões selecionáveis) para "Gênero Literário" (ex: Ficção, Acadêmico, Tecnologia, Biografia, História e Design). ✔️

1

RF07.3

O popup deve conter botões de rádio para ordenação: "A-Z", "Z-A", "Mais Recentes" e "Melhor Avaliados". ✔️

1





Cód

RF07.4

RF07.5

RF08





Requisito ✔️

O popup deve conter um botão "Aplicar Filtros" que fecha o popup, redirecionando o usuário para a tela de acervo, e atualiza a listagem da Tela de Acervo. ❌ FALTA APLICAR OS FILTROS

O popup deve conter um botão "Limpar" para resetar as seleções. ✔️

Tela de Resultados de Busca Vazia (Placeholder). ✔️





Prioridade

1

2





RF08.1

A tela deve exibir um cabeçalho com a foto em miniatura do perfil, o nome da tela “Acervo Digital” ao lado e um símbolo de sino que, ao ser clicado, redireciona o usuário para a tela de notificações. ✔️

3



RF08.2

RF08.3

RF08.4

RF08.5

RF08.5

RF08.6

RF09

RF09.1





Caso a busca no RF06 (acervo digital) não retorne resultados, esta tela deve substituir o Grid. ✔️

A tela deve exibir uma ilustração (imagem) amigável de um livro vazio. ✔️

A tela deve exibir o texto: "Ops! Silêncio na biblioteca. Nenhum livro encontrado para esta pesquisa”. ✔️❓ (motivo da interrogação: o tchê disse que tava bugado no app) // MIGUEL: TA BUGADO

A tela deve conter um texto explicativo dizendo: “Tente usar palavras-chave diferentes ou verifique a ortografia.” ✔️

A tela deve conter um botão de Limpar filtros que, ao ser clicado, redireciona o usuário à tela RF06 (Acervo digital). ✔️

A tela deve possuir uma Bottom Navigation Bar fixa com ícones para: Home, Acervo, Estante, Eventos e Perfil. ✔️ (esta certo no app)

Tela de Detalhes do Livro. ✔️

A tela deve exibir um cabeçalho com o título “Detalhes do livro”, uma seta para voltar a tela anterior (ACERVO DIGITAL) e um símbolo de





2

3

3

3

2

1



3







Cód





Requisito ❓resumo: tela toda bugada

sino que, ao ser clicado, redireciona o usuário para a tela de notificações. ✔️ (motivo da interrogação: não sei se redireciona ou não e o tchê disse que a tela tava bugada)

A tela deve exibir a Capa ampliada, Título, Autor, Gênero e Ano de Publicação. ✔️

A tela deve exibir a Nota Média em ícones de estrelas (1 a 5) e a quantidade de avaliações ✔️

A tela deve conter um botão flutuante ou de destaque primário com um ícone de livro e o texto "Ler Agora" que, ao ser clicado, abre a Tela do Leitor (RF10). ✔️

A tela deve conter um botão secundário de ícone (coração) para "Adicionar aos Favoritos" ❌SUBSTITUIR OS ÍCONES DE CORAÇÃO PELO OUTRO QUE O TCHÊ INDICOU





Prioridade



Group 169763, Grouped object

Group 169765, Grouped object



RF09.2

RF09.3

RF09.4

RF09.5





1

2

1

1



RF09.6

A tela deve conter um texto sobre a sinopse do livro e um botão expansível de “ler mais”, que ao ser clicado, expande a sinopse, apresentando o texto completo. ✔️

2

RF09.7

A parte inferior deve conter um título “Avaliações de

Usuários” e listar resenhas de outros alunos(Nome, Estrelas, Texto e foto de perfil), além de mostrar o período que foi publicada. Ex: 1 dia atrás, dois dias atrás, etc. ✔️ (TEXTOS BUGADOS)

2



RF10

RF10.1

RF10.2

RF10.3





Tela de Leitor Digital (PDF/ePub). ✔️️❓Informações corretas, porém, livro errado. Precisamos só ajeitar isso

A tela deve conter uma seta para voltar para a tela de detalhes do livro e deve conter o título do livro selecionado. ✔️️

A tela deve renderizar o arquivo do livro em tela cheia. ✔️️❓Renderiza?

A tela deve permitir virar as páginas através de swipe (deslizar) horizontal. ✔️❓Elas viram mesmo?️







3

1

1





Cód

Requisito

Prioridade

RF10.4

Ao tocar no centro da tela, deve surgir uma barra superior (RF10.1) e uma barra inferior (com slider de paginação). O slider contém dois ícones de seta, com uma apontando para a esquerda e outra para a direita. A seta da esquerda deve voltar uma página e a da direita avançar uma página. Além disso, deve ser informado o número da página atual e o número total de páginas. ✔️️

1



RF10.5

RF11

RF11.1





A tela deve salvar automaticamente o número da página atual no banco de dados quando o usuário sair da tela. ❓Precisamos implementar ainda

Popup de Fim de Leitura. ✔️

O popup deve ser acionado automaticamente quando o leitor (RF10) atingir a última página do livro. ✔️





1



2



RF11.2

O popup deve exibir uma imagem de um ícone de livro genérico e a mensagem: "Parabéns! Você concluiu a leitura e ganhou X pontos." ✔️ (TEXTOS BUGADOS)

3



RF11.3

RF11.4

RF11.5

RF12





O popup deve conter um botão "Avaliar Livro" que redireciona para a Tela de Resenha (RF14). ✔️

O popup deve conter um botão "Fechar" que retorna aos Detalhes do Livro. ✔️️

O popup deve ter uma label dizendo “Leitura finalizada”. ✔️

Tela "Minha Estante". ✔️





2

2

3





RF12.1

A tela deve conter o título “Minha Estante”, um ícone de seta circular com um relógio ao centro que redireciona o usuário para o Histórico de Leitura(RF 22) e um ícone de lupa que, ao ser clicado, permita pesquisar livros que estejam sendo lidos ou que tenham sido favoritados pelo usuário. ✔️

2





Cód

RF12.2





Requisito ✔️

A tela deve conter abas superiores (TabLayout) com as opções: "Lendo" e "Favoritos". ✔️





Prioridade

1



RF12.3

A aba "Lendo" deve listar livros em formato de lista vertical, com barra de progresso para cada um, indicando a capa, título, autor, quantidade de páginas (Atual e Total) e a porcentagem de conclusão do livro. ✔️

1



RF12.4

RF13





A tela deve possuir uma Bottom Navigation Bar fixa com ícones para: Home, Acervo, Estante, Eventos e Perfil. ✔️

Tela de Favoritos (Aba da Estante). ✔️





1





RF13.1

A tela deve conter o título “Minha Estante”, um ícone de seta circular com um relógio ao centro que redireciona o usuário para o Histórico de Leitura e um ícone de lupa que, ao ser clicado, permita pesquisar livros que estejam sendo lidos ou que tenham sido favoritados pelo usuário. ❌ tira esse ícone de lupa, nós não criamos uma tela pra isso.

2

RF13.2

A tela deve listar em formato Grid apenas os livros marcados com coração pelo usuário. Os livros apresentados possuem capa, título e nome do autor. ❌mudar o ícone de coração pelo outro lá // Miguel: Averiguar e decidir isso e as implicações que isso traz.

1



RF13.3

RF13.4

RF14

RF14.1





O usuário deve poder desmarcar o favorito clicando no ícone do coração que está no canto superior direito de cada livro. ❌mudar o ícone de coração pelo outro lá

A tela deve possuir uma Bottom Navigation Bar fixa com ícones para: Home, Acervo, Estante, Eventos e Perfil. ✔️

Tela/Popup de Avaliação e Resenha. ✔️

A tela deve exibir o título “Avaliação” e o nome do livro no topo. ✔️ (TEXTO BUGADO)





1

1



3







Cód

RF14.2

RF14.3





Requisito ✔️

A tela deve conter um ícone de “X” que ao ser clicado fecha o popup. ✔️

A tela deve exibir um card do livro contendo sua capa, titulo, autor e ano de publicação ✔️ (FALTA COLOCAR A CAPA EXATA PRA CADA LIVRO NO APP)





Prioridade

3

3



RF14.4

A tela deve exibir um texto dizendo: “Toque nas estrelas para avaliar” e 5 estrelas clicáveis para o usuário definir a nota obrigatória. ✔️

1

RF14.5

A tela deve conter um campo de texto (multiline) opcional com o placeholder "Escreva sua resenha sobre o livro...", contendo um texto explicativo logo abaixo dizendo: “mínimo 20 caracteres” e um limite de 0 a 500 caracteres, Logo acima do campo deve conter o texto “SUA RESENHA” . ✔️

2

RF14.6

A tela deve possuir um botão "Enviar Resenha". Se enviada com texto, exibir a mensagem: "Enviado para moderação!". Se apenas nota, exibir: "Avaliação registrada!" ❌ TA MEIO BIZARRO essa porra no app. Tem que olhar.

1



RF14.7

RF15

RF15.1





A tela deve possuir um botão "Cancelar". Se apertado deverá voltar para o PopUp de fim de leitura. ✔️

Tela de Envio de Obra Autoral. ✔️

A tela deve conter o título “Envio de Obra” e um botão em formato de seta para voltar a tela anterior. ✔️





2



3



RF15.2

A tela deve conter um ícone de nuvem remetendo a um upload e um texto explicativo dizendo: “Submeta sua obra autoral para avaliação e inclusão no acervo digital”. ✔️

3

RF15.3

A tela deve conter campos para: Título da Obra, Gênero e Sinopse Curta.Os campos Título, gênero e sinopse devem conter um texto explicativo dentro do campo insinuando o que deve ser colocado. Os textos explicativos são, respectivamente: “Ex: A jornada dos Algoritmos”, “Selecione um gênero” e “Breve descrição sobre o conteúdo da sua obra…”. ✔️

1  



RF15.4

A tela deve conter no campo de Gênero um Dropdown, onde haverá uma lista de gêneros (FICÇÃO, ACADÊMICO,

TECNOLOGIA, BIOGRAFIA, HISTÓRIA E DESIGN). ✔️

1



RF15.5

RF15.6

RF15.7

RF16





A tela deve conter um botão "Anexar PDF", que abre o gerenciador de arquivos do celular. ❌ NÃO ABRE

Após anexar, deve exibir o nome do arquivo anexado e um ícone de lixeira para removê-lo. ✔️

A tela deve conter o botão "Enviar Obra", que valida os campos e exibe "Obra enviada para análise". ✔️ ❌ o pop up ta aparecendo uma velocidade ESTUPIDAMENTE rapida

Tela de Eventos Literários. ✔️





1

2

1





RF16.1

A tela deve conter, no topo, o título “Eventos Literários”, a foto de perfil do usuário e um botão com o símbolo de sino que, ao ser clicado, redireciona o usuário para a tela de notificações. ✔️

3

RF16.2

A tela deve exibir filtros em disposição horizontal que remetem ao tipo de evento que será exibido nos cards verticais. Exemplo: Todos, Workshops, palestras e lançamentos. ✔️

2

RF16.3

A tela deve listar os eventos cadastrados em cards verticais. ✔️

1

RF16.4

Cada card deve exibir a imagem/banner do evento, data (em destaque no canto superior esquerdo), título, local, um texto com uma pequena descrição do evento e um botão em formato de seta azul que redireciona o usuário para a tela de eventos. ✔️

1

RF16.5

Ao clicar no botão em formato de seta, deve abrir o Tela de Detalhes do Evento (RF17). ✔️

1



RF16.6

A tela deve possuir uma Navigation Bar com os seguintes botões: home, acervo, estante, eventos, perfil. Os botões são responsáveis por redirecionar o usuário para a tela especificada em seu nome. ✔️

1

RF17

Tela de Detalhes do Evento. ✔️



RF17.1

A tela deve exibir, no topo, o nome do evento, incluindo o filtro em que o evento está classificado, uma imagem referente ao evento e um ícone de Seta que, ao ser clicado, retorna para a aba de eventos (RF16). ✔️

3

RF17.2

A tela deve conter a data e hora, mostrando exatamente o horário de início e o horário de encerramento do evento e, entre parênteses, o fuso horário referente. ✔️

1

RF17.3

A tela deve informar a localização do evento, especificando o endereço e um complemento que ajude a auxiliar o processo de localização. Texto de complemento usado como exemplo:

“Biblioteca Central Municipal”. ✔️

1

RF17.4

A tela deve exibir um texto com o título em negrito “Sobre o evento”, que tem como função informar ao usuário qual o objetivo do evento selecionado, seu tema e informações essenciais. ✔️

2



RF17.5

RF18





A tela deve conter um botão "Adicionar ao Meu Calendário" (que interage com a agenda nativa do Android). ❌ ***indicação; aparecer aquela mensagem verdinha dizendo que adicionar ao calendário deu certo

Tela de Notificações. ✔️





2





RF18.1

A tela deve conter o título “Notificações” e um botão em formato de seta que, ao ser clicado, retorna para a tela anterior. ✔️

3



RF18.2

A tela deve possuir um botão "Marcar todas como lidas" no canto superior direito. Caso o botão seja clicado, todas as notificações serão marcadas como lidas. ❌ NÃO ESTA FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

2

RF18.3

A tela deve listar notificações in-app em ordem cronológica (mais recentes -> menos recentes), com o horário que a notificação foi enviada, um texto explicativo contextualizando a notificação e um título informando que a notificação foi recebida. Ex título: “Sua resenha foi aprovada!”. Ex texto explicativo: “Parabéns! Sua análise de "Dom Casmurro" agora está visível para a comunidade.” Ex horário: 10:45. ❌ NÃO ESTA FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

1

RF18.4

A tela deve listar se a notificação foi enviada no dia, ontem ou anteriormente a esses dois. Com as labels “Hoje”, “Ontem” e “Anteriores”, respectivamente. ❌ NÃO ESTA FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

2

RF18.5

Notificações não lidas devem ter um fundo levemente destacado e um ícone de círculo azul ao lado. ❌esse ícone de círculo azul não aparece na imagem,  NÃO ESTA FUNCIONAL

3

RF18.6

Ao tocar na notificação, ela deve ser marcada como lida no banco de dados, com um ícone de círculo azul desaparecendo e o fundo, antes transparente, ficando esbranquiçado. ❌ NÃO ESTA FUNCIONAL

1



RF19

RF19.1

RF19.2





Tela de Perfil do Aluno. ✔️

A tela deve conter o título “Perfil do Aluno”. ✔️

A tela deve exibir a foto de perfil do aluno, nome completo e matrícula. ✔️







3

1



RF19.3

A tela deve exibir uma seção central de Livros lidos, resenhas aprovadas e pontos (gamificação), indicando, respectivamente, a quantidade de cada uma dessas estatísticas. ️ ❌ NÃO ESTA FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

2

RF19.4

A tela deve conter uma lista de 5 botões: "Editar Perfil",

"Envio de obra", "Dúvidas (FAQ)", "Configurações" e

"Sair", que é responsável pelo logout do usuário. Cada botão,

1  



Group 154221, Grouped object



Requisito

caso seja clicado, levará, respectivamente, às telas RF20, RF22, RF23, RF24 e RF25. ✔️



Group 154222, Grouped object

RF19.5

A tela deve possuir uma Navigation Bar com os seguintes botões: home, acervo, estante, eventos, perfil. Os botões são responsáveis por redirecionar o usuário para a tela especificada em seu nome. ✔️

1

RF20

Tela de Edição de Perfil. ✔️



RF20.1

A tela deve conter o título “Editar Perfil”, juntamente com um botão em formato de seta, que tem como objetivo retornar à tela anterior. ✔️

3

RF20.2

A tela deve possuir a foto atual do usuário e um ícone de camera em seu canto inferior direito, sendo um botão que, ao ser clicado, ativa a opção "Alterar Foto" (abre galeria/câmera). ❌️  NÃO ESTA FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

1

RF20.3

A tela deve possuir campos editáveis para nome de exibição e biografia curta. ✔️

1

RF20.4

O campo matrícula e o e-mail institucional devem aparecer bloqueados (readonly), com um aviso visual em sua lateral (ícone de cadeado). ✔️

2

RF20.5

A tela deve ter o botão "Salvar Alterações" que, ao ser clicado, valida as alterações, atualizando-as no banco de dados. Ademais, aparece uma mensagem dizendo: “Alterações salvas!”. ❌ NÃO ESTA FUNCIONAL, POREM A ESTRUTURA ESTA CERTA

1

RF21

Tela de Gamificação (Meus Pontos). ✔️



RF21.1

A tela deve conter o título “Meus Pontos”, juntamente com um botão em formato de seta, que tem como objetivo retornar à tela anterior. ✔️

3

Requisito

RF21.2

A tela deve exibir um medidor com o total de pontos do aluno, além de exibir o seu nível, que vai de 1 a 5 (Leitor Iniciante, Leitor Casual, Leitor Assíduo, Leitor Especialista e Leitor Mestre). ❌  NÃO ESTA FUNCIONAL, POREM A ESTRUTURA ESTA CERTA

1

RF21.3

A tela deve listar o histórico de pontos, referente aos pontos recentes adquiridos pelo usuário (ex: "+50 pontos por concluir livro X", "+15 pontos por resenha aprovada"). Ademais, cada histórico deve conter a sua respectiva data. ❌ NÃO ESTA FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

2

RF21.4

A tela deve exibir uma seção especificando que o usuário obtém 50 pontos ao ler um livro e mais 15 ter a resenha aprovada. ✔️

3

RF22

Tela de Histórico de Leitura.



RF22.1

A tela deve conter o título “Histórico de Leitura”, juntamente com um botão em formato de seta, que tem como objetivo retornar à tela anterior. Deve conter também um botão em formato de lupa, que tem como objetivo permitir que o usuário pesquise uma obra literária em seu histórico.  ❌FALTA IMPLEMENTAR A FUNCIONALIDADE DA LUPA

2

RF22.2

A tela deve conter um texto “Livros Concluidos” e logo abaixo a listagem de todos os livros lidos, ordenados por data de conclusão, em uma lista, com capa do livro, o título do livro e o nome do autor . ✔️

1

RF23

Tela de FAQ / Dúvidas.



RF23.1

A tela deve conter o título “Dúvidas Frenquentes”, juntamente com um botão em formato de seta, que tem como objetivo retornar à tela anterior. ✔️ (bugs de texto)

3

RF.23.2

A tela deve possuir um texto introdutório explicando a sua finalidade ao usuário. EX: “Encontre respostas para as dúvidas mais comuns sobre o uso da ForLibrary.” ✔️

3

RF23.3

A tela deve listar perguntas frequentes em componentes expansíveis (accordion) que, ao serem clicados pelo usuário, exibem o texto respondendo à dúvida selecionada. ✔️ (FALTA AS RESPOSTAS)

2

RF24

Tela de Configurações (Aluno).



RF24.1

A tela deve conter, no topo, o título que remeta ao objetivo principal da tela, no caso, “Configurações”, juntamente com um botão em formato de seta, que tem como objetivo retornar à tela anterior. ✔️

3

RF24.2

A tela deve conter opções (switches/toggles) em uma lista vertical chamada “Geral”, que são responsáveis por habilitar/desabilitar: "Notificações Push" e "Tema Escuro". ❌ NÃO ESTA FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

2

RF24.3

A tela deve conter botões para acessar os "Termos de Uso" e a "Política de Privacidade", disponíveis em uma lista vertical “Sobre o app”. ✔️ (FALTA O TEXTO)

2

RF24.4

A tela deve conter um botão "Sair", contido em uma lista vertical chamada “Conta”, que é responsável por fazer o logout do usuário, redirecionando-o ao popup de confirmação de logout. ✔️

1



RF25

RF25.1





Popup de Confirmação de Logout.

Acionado ao clicar em "Sair" noRF19 ou no RF24. ✔️







2



RF25.2

O popup deve perguntar: "Tem certeza que deseja desconectar sua conta?" e, além disso,logo abaixo deve conter um texto explicativo dizendo “Você precisará informar seu e-mail e senha novamente para acessar sua biblioteca”. ✔️

3

RF25.3

O popup deve conter os botões "Cancelar" e "Sim, Sair". O botão "Sair" apaga a sessão local e vai para RF02, enquanto o botão "Cancelar" retorna à tela anterior. ✔️

1  



MÓDULO 3: ÁREA DO ADMINISTRADOR (Gestão)



Cód

RF26





Requisito

Tela Dashboard (Home) do Administrador.





Prioridade





RF26.1

A tela deve exibir uma saudação e o cargo ("Olá, Admin!"), junto de um botão com o símbolo de sino que, ao ser clicado, redireciona o usuário para a tela de notificações. A tela deve conter também um botão com símbolo de engrenagem que redireciona o admin para a tela de configurações do sistema (RF 40) ✔️

2

RF26.2

A tela deve exibir o titulo da tela e o que ela apresenta

("Dashboard", "Visão geral do sistema e métricas principais") ✔️

3

RF26.3

A tela deve exibir 4 Cards de resumo numérico: "Livros no

Acervo", "Alunos Ativos", "Resenhas Pendentes" e "Obras

Pendentes" e suas respectivas quantidades, além disso, card de

“Resenhas Pendentes” deverá ser indicado com a cor vermelha e o card de “Obras Pendentes” deverá ser indicado com a cor verde . ❌ NÃO ESTA FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

2

RF26.4

A tela deve exibir as 4 atividades mais recentes do administrador e o que foi feito com título da atividade, ícone de referência, nome do livro, autor, e tempo de publicação. ❌ NÃO ESTA FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

2

RF26.5

A tela, ao lado do título para as 4 atividades recentes, deve possuir a opção "Ver todas" as atividades, para então o admin ver suas atividades por ordem cronológica ❌ NÃO ESTA FUNCIONAL

2



RF26.3

RF27





A tela deve possuir uma Bottom Navigation Bar com: Dashboard, Acervo, Moderação, Eventos. ✔️

Tela de Gestão de Acervo (Listagem Admin).





1







RF27.1

A tela deve exibir o título da tela "Gestão de Acervo" e um botão em formato de seta, que tem como objetivo retornar à tela anterior ❌ ESSE  BOTÃO SETA NÃO FAZ O MENOR SENTIDO PRA UMA TELA QUE TEM O NAV BAR

3

RF27.2

A tela deve listar todos os livros cadastrados, contendo um campo de busca com um placeholder “Busque por titulo ou autor” e os livros colocados pelo administrador. Além disso, abaixo do placeholder deve haver um informe da quantidade de livros. ✔️ (NÃO SEI SE TA FUNCIONAL A QUANTIDADE DE LIVROS)

1

RF27.3

Cada item da lista deve exibir a Capa, Título, Autor e ícones rápidos de ação: "Editar" (lápis) e "Excluir" (lixeira). Caso o ícone de excluir seja clicado, o usuário será redirecionado para a tela de confirmação de exclusão (RF30) ✔️

1

RF27.4

A tela de edição de livro (RF29) deve ser carregada ao selecionar a opção de "Editar"(lápis) ✔️

1

RF27.5

A tela deve possuir um botão Flutuante (FAB) "+" no canto inferior direito para adicionar um novo livro (Redireciona para RF28). ✔️

1



RF27.6

RF28





A tela deve possuir uma Bottom Navigation Bar com: Dashboard, Acervo, Moderação, Eventos ✔️

Tela de Adição de Novo Livro.





1





RF28.1

A tela deve exibir o título da tela "Adição de Obra" e um botão em formato de seta, que tem como objetivo retornar à tela anterior (RF27). ✔️

3

RF28.2

O envio deve conter um botão/área pontilhada com um simbolo de nuvem remetendo a upload e contendo um texto explicativo dizendo: "Fazer upload da Capa (JPG/PNG)".  ❌ NÃO ESTA FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

1

RF28.3

A tela deve conter campos obrigatórios para: Título, Autor, Gênero (Dropdown), Ano de lançamento, Páginas e sinopse.

Todos os campos devem conter um texto explicativo que ❌ VERIFICAR OBRIGATORIDADE,

1



Group 167862, Grouped object



Requisito

contextualize o tipo de informação a ser inserida. Ex de texto para título: “Ex: O alquimista”. ✔️



Group 167863, Grouped object

RF28.4

O campo de gênero (DropDown) terá uma lista de gêneros (FICÇÃO, ACADÊMICO, TECNOLOGIA, BIOGRAFIA, HISTÓRIA E DESIGN). ❌ CONCERTAR OS GENEROS.

1

RF28.4

A tela deve conter um campo multiline para Sinopse. ✔️

1

RF28.5

A tela deve conter um botão "Anexar Arquivo do Livro (PDF/ePub)" que, ao ser clicado, abre o gerenciador de arquivos do android. ❌ NÃO ESTA FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

1

RF28.6

A tela deve conter os botões "Cancelar" e "Salvar Obra". O botão de cancelar, ao ser clicado, encerra o processo e volta para a tela de gestão de acervo(RF27). O botão de salvar, ao ser clicado, salva o livro no banco de dados e o adiciona ao acervo digital. ❌ NÃO ESTA FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

1



RF28.7

RF29

RF29.1

RF29.2





A tela deve possuir uma Bottom Navigation Bar com: Dashboard, Acervo, Moderação, Eventos. ✔️

Tela de Edição de Livro.

A tela deve exibir o título da tela "Editar Obra" ao lado de um icone de voltar para a página anterior. ✔️

Logo abaixo, a tela exibe a capa do livro, juntamente com o título e a última vez em que ele foi atualizado. A tela também deve possuir um botão azul com um símbolo de câmera e o título “Trocar capa”, que permitirá substituir a capa ou o arquivo PDF, abrindo o gerenciador de arquivos do Android em ambos os casos. ❌ NÃO ESTA FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA !!!!!! TEM UM REQUISITO LITERALMENTE IGUAL A ESSE. 29.4





1



3

1



RF29.3

A tela deve conter os dados que foram inseridos na RF28, logo, os campos devem ser iguais ao que já foi preenchido, sendo eles: Título, Autor, Gênero (DropDown), Ano de lançamento, páginas, sinopse e campo para inserir o arquivo do livro. O arquivo do livro possui um ícone de lixeira que, ao ser clicado, remove o arquivo atual e permite que o usuário insira um novo no lugar.  ❌ NÃO ESTA FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

1



RF29.4

No campo de capa, o sistema deve exibir a capa do livro atual com seu título e quando ela foi atualizada por último, junto de um botão "Trocar Capa" que disponibiliza a opção de substituir a capa colocada, abrindo o gerenciador de arquivos do android. ❌ NÃO ESTA FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

2

RF29.5

A tela deve conter os botões "Cancelar" para descartar as ações alteradas, voltando a página. Além disso o botão "Atualizar Obra" atualiza as informações no banco de dados e salva o livro com os dados alterados no acervo. ❌ NÃO ESTA FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

1



RF29.6

RF30

RF30.1





A tela deve possuir uma Bottom Navigation Bar com: Dashboard, Acervo, Moderação, Eventos. ✔️

Popup de Confirmação de Exclusão de Livro.

Acionado ao clicar na lixeira no RF27. ✔️





1



2



RF30.2

O popup deve conter o título: “Confirmar Exclusão” e o texto explicativo " A obra (título) será removida permanentemente do acervo e das estantes de todos os alunos. Deseja continuar??". ✔️

3

RF30.3

Deve conter botão "Cancelar"(em branco) que, ao ser clicado, volta para o acervo e não exclui o livro. Além disso, o botão "Excluir" (em vermelho), ao ser clicado, apaga o livro do banco de dados e da interface visual do sistema. ❌ NÃO ESTÁ FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

1



RF31

RF31.1





Tela de Gestão de Eventos (Listagem).

A tela deve exibir o título "Gestão de Eventos" e o número de eventos totais para a gestão. ✔️ (QUANTIDADE DE EVENTOS NÃO ESTA FUNCIONAL)







3



RF31.2

A tela deve exibir o período do evento (EM BREVE ou ANTES), título, data, horário e os ícones de lápis(editar) e lixeira(apagar). Caso o ícone de lápis seja clicado, o usuário será redirecionado para a tela de edição de eventos (RF32). ❌ NÃO ESTÁ FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

1  



Group 157765, Grouped object



Caso o ícone de lixeira seja clicado, o usuário será redirecionado para a tela de exclusão (RF30), porém, o contexto mudará de “obras” para “eventos”. ❌MELHOR CRIAR OUTRO REQUISITOS PARA A EXCLUSÃO DE EVENTOS. TENDO EM VISTA QUE O TEXTO É DIFERENTE.



Group 157766, Grouped object



RF31.2

RF32

RF32.1

RF32.2

RF32.3





A tela deve possuir um botão Flutuante "+" para criar um evento novo (Redireciona para RF32). ✔️

Tela de Criação/Edição de Evento.

A tela deve exibir o titulo da página "Criar evento”. ✔️

A tela deve exibir "Novo evento" em titulo alto e o subtitulo

"Preencha os detalhes para publicas na agenda da biblioteca"✔️

A tela deve ter campos para Título do Evento e Descrição. ✔️





1



3

3

1



RF32.4

A tela deve conter campos com seletor nativo de calendário (DatePicker) para Data e seletor de relógio (TimePicker) para Horário. ❌ NÃO ESTÁ FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

1



RF32.5

RF32.6

RF32.7

RF32.8

RF33





A tela deve ter um campo para "Local (Físico) ou Link (Online)". ✔️

A tela deve ter conter um botão/área pontilhada com simbolo ode camera para "Imagem de Capa(Opcional)". ❌ NÃO ESTÁ FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

Botão "Cancelar" que descarta as informações colocadas. ✔️

Botão "Publicar Evento", que notifica todos os alunos ao ser salvo. ❌ NÃO ESTÁ FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

Tela de "Moderação"





1

2

2

1







RF33.1

O sistema deve exibir um botão em formato de card para a "Moderação de Usuários", contendo um ícone representativo e uma seta indicativa de navegação. ✔️

2

RF33.2

O sistema deve redirecionar o administrador para a tela específica de moderação de usuários ao clicar no card "Moderação de Usuários". ✔️

1

RF33.3

O sistema deve exibir um botão em formato de card para a "Moderação de Obras", contendo um ícone representativo e uma seta indicativa de navegação. ✔️

2

RF33.4

O sistema deve redirecionar o administrador para a tela específica de moderação de obras ao clicar no card "Moderação de Obras". ✔️

1

RF33.5

O sistema deve exibir um botão em formato de card para a "Moderação de Resenha", contendo um ícone representativo e uma seta indicativa de navegação. ✔️

2

RF33.6

O sistema deve redirecionar o administrador para a tela específica de moderação de resenhas ao clicar no card "Moderação de Resenha". ✔️

1

RF33.7

O sistema deve possuir uma barra de navegação inferior fixa contendo atalhos para as telas: "Dashboard", "Acervo", "Moderação" e "Eventos". ✔️

1

RF33.8

O sistema deve indicar visualmente na barra de navegação inferior que a aba "Moderação" está ativa no momento (ex: ícone e texto destacados em azul com uma linha superior). 2 ✔️

3



RF34

RF34.1





Tela de Moderação de Resenhas.

A tela deve possuir um header com o titulo da tela em"Moderação de resenhas” juntamente de um botão em formato de seta que retorna a tela anterior✔️







3







Cód

RF34.2

RF34.3

RF34.4

RF34.5

RF34.6

RF34.7

RF34.8

RF35





Requisito

A tela deve possuir um texto chamado status e logo abaixo um icone de exclamação junto com número de reviews pendentes ao lado do texto "X Reviews pendentes" ❌ NÃO ESTÁ FUNCIONAL, TEXTO ESTA EM INGLES.

A tela deve possuir um filtro do lado direito da tela do texto de "X Reviews pendentes" em azul ❌ NÃO FAÇO A MENOR IDEIA DOQ SEJA ISSO.

A tela deve conter uma lista das resenhas enviadas pelos alunos com o status "Pendente". ❌ NÃO ESTÁ FUNCIONAL, PENDENTE ESTA EM INGLES.

O card de cada item deve mostrar o nome do Aluno, o Livro Avaliado, a nota em estrelas e as primeiras linhas do texto. ✔️

Ao clicar no item, redireciona para a Tela de Análise de Resenha (RF35). ✔️

Ao final da tela, deve exibir um botão texto "Carregar mais resenhas" ❌ NÃO ESTÁ FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

A tela deve possuir uma Bottom Navigation Bar com: Dashboard, Acervo, Moderação, Eventos ✔️

Tela de Análise de Resenha.





Prioridade

2

2

1

2

1

2

1





RF35.1

A tela deve exibir em seu header uma seta a esquerda apontando pra esquerda para voltar para a tela anterior ao lado do titulo em negrito "Analisar Resenha" ✔️

3

RF35.2

A tela deve exibir o texto completo da resenha, foto de perfil aluno, data e horario e nota atribuida. ✔️

1

RF35.3

A tela deve exibir um lembrete em azul "Certifique-se de que a resenha segue as diretrizes da comunidade antes de tomar uma decisão final." ✔️

3

RF35.4

A tela deve possuir um botão verde "Aprovar Resenha" (que publica no livro e dá pontos ao aluno) e retorna a tela anterior. ❌ NÃO ESTÁ FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

1  




Cód

RF35.5

RF35.6

RF36

RF36.1

RF36.2

RF36.3





Requisito

A tela deve possuir um botão vermelho "Rejeitar Resenha" que, ao ser clicado, inativa a resenha do banco e retorna a tela anterior. ❌ NÃO ESTÁ FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

Ao rejeitar, deve exibir um campo opcional: "Motivo da Rejeição" para ser enviado por notificação ao aluno. ❌ NÃO ESTÁ FUNCIONAL, NÃO SEI COMO TA FUNCIONANDO ISSO NO CODIGO.

Tela de Moderação de Obras Autorais.

A tela deve exibir em seu header o título da página em negrito "Moderação de Obras" junto de um botão em formato de seta para voltar a tela anterior ✔️

O título e explicação da tela "Submissões recentes"

"Aguardando revisão editorial" deve aparecer acima das obras ❌ Tirar moderação de obras NO APP

A tela deve listar as obras submetidas por alunos via RF15 (Envio de obra autoral). ✔️





Prioridade

1

2



3

3

1



RF36.4

A tela deve exibir uma lista dos livros para revisão com seus respectivos: Título da Obra, Gênero, Autor, Matrícula do

Aluno, e um texto escrito "Pendente" de fundo amarelo. ✔️

1

RF36.5

Abaixo de cada livro, deve-se possuir o botão de "Revisar" em azul que irá direcionar o administrador à tela de Análise de Obra (RF36). ✔️

1

RF36.6

A tela deve possuir um card abaixo contendo a quantidade de livros pendentes para análise, indicados pelo texto: “TOTAL PENDENTE”, além do card possuir um ícone de calendário. ✔️

2



RF36.7

RF37





A tela deve possuir uma Bottom Navigation Bar com: Dashboard, Acervo, Moderação, Eventos ✔️

Tela de Análise de Obra Autoral.





1





RF38.3

A tela deve listar os alunos cadastrados com barra de busca por Matrícula ou Nome, Quantidade de usuários encontrados com o texto "x Usuários encontrados". ✔️

1



Cód

RF37.1

RF37.2

RF37.3





Requisito

A tela deve exibir o título “Analisar Obra" e uma seta para voltar a tela anterior. ✔️

A tela deve conter um card do livro indicando sua capa, título, Gênero,autor e Sinopse. ✔️

A tela deve conter um botão "Baixar PDF para Análise" para que o administrador possa ler o material. ❌ NÃO ESTÁ FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA





Prioridade

3

1

1





RF37.4

A tela deve conter botões "Aprovar Obra" e "Rejeitar Obra". Caso a obra seja aprovada, o conteúdo será salvo no banco de dados e publicado no acervo digital. Caso a obra seja rejeitada, o conteúdo ficará inativo no banco de dados e será uma notificação ao usuário informando a rejeição e o motivo pelo qual foi reincidida, de ambos os modos o programa retorrna a tela anterior ao qualquer um desses dois botões ser clicado. ❌ NÃO ESTÁ FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

1

RF37.5

A tela deve exibir um lembrete em azul "Certifique-se de que a obra está de acordo com as Diretrizes Editoriais antes de prosseguir com a aprovação." acima dos botões ✔️

3

RF37.6

A tela deve mostrar um campo opcional, caso o administrador rejeite a obra, chamado "Motivo da Rejeição", contendo o texto explicativo: “Descreva o motivo caso a obra não cumpra as diretrizes...” ❌ NÃO ESTÁ FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

2



RF38

RF38.1

RF38.2





Tela de Gestão de Usuários (Alunos).

A tela deve exibir em seu header o título da página em negrito "Gestão de Usuários" junto de um botão em formato de seta que retorna a tela anterior. ✔️

A tela deve exibir uma SearchBar que permite a busca por nome ou mátricula dos alunos. ✔️







3

1



RF40

Tela de Configurações do Sistema (Admin).





Cód

RF38.4

RF38.5

RF38.6

RF39

RF39.1





Requisito

A lista deve mostrar Foto de perfil, Nome, Matrícula e Status ("Ativo" ou "Bloqueado"). ✔️

Ao clicar em um aluno, abre o Popup de Detalhes do Usuário (RF39). ✔️

A tela deve possuir uma Bottom Navigation Bar com:

Dashboard, Acervo, Moderação, Eventos ✔️

Pop-up de Detalhes do Usuário (Visão Admin).

O pop-up deve ser exibido acima da tela Gestão de Usuário (RF38) ✔️





Prioridade

1

1

1



2





RF39.2

O popup deve exibir o título “Detalhes do Usuário” e “Dados pessoais”, informando nome o completo do aluno, matrícula e e-mail institucional. ✔️

1

RF39.3

O popup deve conter um botão de toggle/switch para "Bloquear Acesso" com o texto explicativo “Impede login e interações no acervo”, proibindo o login do aluno em caso de infração às regras da biblioteca. . ❌ NÃO ESTÁ FUNCIONAL, PORÉM A ESTRUTURA ESTA CERTA

1

RF39.4

O popup deve ter um botão "Fechar" que, ao ser clicado, volta a tela RF38. ✔️

2

RF39.5

Deve aparecer um alerta em um card vermelho "Status de Moderação" "Total de resenhas inadequadas" em que mostra quantas resenhas inadequadas o usuário fez e se for 3, exibir o alerta "O usuário atingiu o limite de alertas automáticos no sistema.". ❌ NÃO ESTÁ FUNCIONAL, PORÉM A ESTRUTURA ESTA “CERTA”

1



Cód

Requisito

Prioridade

RF40.1

A tela deve exibir em seu header o título da página em negrito "Configurações do Sistema" ao lado de uma seta para voltar a página ✔️

3

RF40.2

A tela deve conter um campo com o título “Gamificação” para editar a quantidade de pontos dados por Livro Lido (ex: mudar de 15 para 20 pontos globais). ✔️

1

RF40.3

A tela deve possuir o campo “SESSÃO” contendo um texto explicativo: “Ao sair do sistema, todas as sessões ativas neste dispositivo serão encerradas. Certifique-se de salvar quaisquer alterações pendentes na configuração.” e um botão “SAIR” que, ao ser clicado, encerra a sessão do usuário e deixa sua conta inativa no sistema. ✔️

1

RF41

Tela de Chat Bot



RF41.1

A tela deve possuir um cabeçalho superior azul contendo o nome “Assistente ForLibrary” e o status “Online”. ✔️

2

RF41.2

A tela deve conter um ícone de fechamento (“X”) no canto superior direito para encerrar o chat. ✔️

2

RF41.3

A tela deve exibir a foto/ícone do assistente virtual ao lado do cabeçalho. ✔️

3

RF41.4

O sistema deve exibir automaticamente uma mensagem inicial

do assistente dizendo: “Olá!   Sou o assistente da ForLibrary. Como posso te ajudar hoje?”. ✔️

1

Cód

Requisito

Prioridade

RF41.5

A tela deve conter uma área de conversa para exibição das mensagens enviadas pelo usuário e das respostas do assistente. ✔️

1

RF41.6

A tela deve possuir um campo de entrada de texto na parte inferior para digitação das mensagens do usuário. ✔️

1

RF41.7

A tela deve conter um botão de envio representado por um ícone de avião de papel ao lado do campo de texto e deve enviar a mensagem ao pressionar o botão de envio. ❌ NÃO ESTÁ FUNCIONAL

1

RF41.8



O sistema deve permitir múltiplas interações consecutivas entre usuário e assistente sem necessidade de recarregar a página. ❌ NÃO ESTÁ FUNCIONAL



1

RF41.9

O sistema deve exibir as mensagens do usuário e do assistente em balões de conversa distintos. ❌ NÃO ESTÁ FUNCIONAL

1



Requisitos Não-funcionais

Cod

Requisito

Prioridade

RNF01

O sistema deve garantir a segurança dos dados dos usuários.

1

RNF01.1

O sistema deve garantir a proteção de dados conforme a LGPD (Lei Geral de Proteção de Dados).

1

RNF01.2

O sistema deve permitir que o usuário solicite a exclusão de seus dados pessoais.

2



RNF02

O sistema deve apresentar bom desempenho nas interações do usuário.

1

Cod

Requisito



Prioridade

RNF02.1

O tempo de resposta das telas deve ser inferior a 3 segundos em 90% das interações.

1

RNF02.2

O carregamento de listas (acervo, eventos, estante) deve ocorrer em menos de 2 segundos.

1

RNF02.3

A Splash Screen deve respeitar o tempo de exibição definido de 2 segundos.

2

RNF02.4

O sistema deve suportar no mínimo 300 usuários simultâneos sem degradação significativa.

2

RNF03

O sistema deve ser compatível com dispositivos Android.

1

RNF03.1

O sistema deve ser desenvolvido em Kotlin utilizando o Android Studio.

1

RNF03.2

O sistema deve ser compatível com Android 8.0 (API 26) ou superior.

1

RNF03.3

O sistema deve se adaptar a diferentes tamanhos de tela e resoluções.



2

RNF04

O sistema deve possuir uma interface intuitiva e de fácil uso

1

RNF04.1

O sistema deve seguir padrões consistentes de design em todas as telas.

1



RNF04.2

O sistema deve fornecer feedback visual para ações do usuário (mensagens, carregamento, erros)



1

Cod

Requisitos

Prioridade

RNF04.3

O sistema deve ser acessível, permitindo navegação clara

2

RNF05

O sistema deve possuir código organizado e de fácil manutenção.

1

RNF05.1

O sistema deve possuir código organizado e de fácil manutenção.

1

RNF05.2

O código deve seguir padrões de desenvolvimento e boas práticas.

1

RNF06

O sistema deve ser capaz de se integrar com serviços externos.

1

RNF06.1

O sistema deve utilizar APIs para autenticação, armazenamento e dados.

1

RNF07

O sistema deve oferecer suporte a notificações.

2

RNF07.1

O sistema deve suportar notificações push.

2

RNF07.2

As notificações devem ser entregues em tempo hábil ao usuário.

2

RNF08

O sistema deve gerenciar corretamente o armazenamento de dados.

1

RNF08.1

O sistema deve salvar automaticamente o progresso de leitura do usuário.

1

RNF08.2

O aplicativo deve limitar o uso de armazenamento local a no máximo 200MB.

2

RNF09

O sistema deve otimizar o uso de recursos do dispositivo.

2

RNF09.1

O sistema deve minimizar o consumo de bateria.



2



RNF09.2

O sistema deve evitar processamento desnecessário em segundo plano.



2

Cod





Requisitos



Prioridade



RNF10

O sistema deve tratar falhas de conexão com a internet.

1

RNF10.1

O sistema deve operar com certas limitações mesmo sem conexão.

1

RNF10.2

O sistema deve tentar reconectar automaticamente quando possível.

2

RNF11



O sistema deve ser escalável.

1

RNF11.1

O sistema deve suportar aumento de usuários e dados sem perda significativa de desempenho.

1

RNF12

O sistema deve garantir eficiência no uso de rede.

1

RNF12.1

O sistema deve minimizar o consumo de dados móveis.

1

RNF12.2

O sistema deve utilizar cache sempre que possível.

1

RNF13

O sistema deve possuir boa usabilidade.

1

RNF13.1

O usuário deve conseguir realizar tarefas principais em até 3 interações.

2

RNF13.2

O sistema deve reduzir a necessidade de treinamento do usuário.

2  

  
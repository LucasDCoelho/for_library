# ForLibrary - Schema Supabase (supabase_schema.md)

Este documento documenta o schema PostgreSQL do banco de dados Supabase da aplicação **ForLibrary**, incluindo todas as tabelas, colunas, relacionamentos e constraints.

---

## Visão Geral

O servidor Supabase está configurado com:
- **Banco de dados:** PostgreSQL
- **Autenticação:** Supabase Auth (GoTrue) integrado
- **Segurança:** Row Level Security (RLS) habilitado
- **Tabelas principais:** 10 tabelas públicas relacionadas ao domínio da aplicação

---

## 1. Tabela: `usuarios`

**Descrição:** Armazena dados dos usuários do sistema (alunos e administradores).

| Coluna | Tipo | Constraints | Descrição |
|--------|------|-------------|-----------|
| `id` | integer | PRIMARY KEY, AUTO_INCREMENT | Identificador único |
| `nome` | varchar | NOT NULL | Nome completo do usuário |
| `matricula` | varchar | NOT NULL, UNIQUE | Matrícula institucional única |
| `email` | varchar | NOT NULL, UNIQUE | Email institucional único |
| `foto_perfil` | varchar | - | URL da foto de perfil |
| `biografia` | text | - | Biografia do usuário |
| `tipo` | varchar | - | Tipo de usuário (Aluno, Admin) |
| `pontos_gamificacao` | integer | DEFAULT 0 | Pontos acumulados para gamificação |
| `nivel_gamificacao` | integer | DEFAULT 1 | Nível alcançado do usuário |
| `status` | varchar | DEFAULT 'Ativo' | Status do usuário (Ativo, Inativo, Bloqueado) |
| `resenhas_inadequadas` | integer | DEFAULT 0 | Contador de resenhas marcadas como inadequadas |
| `auth_user_id` | uuid | UNIQUE, FOREIGN KEY → auth.users(id) | Referência ao usuário autenticado no Supabase Auth |

**Relacionamentos:**
- 1:N com `atividades_admin` (admin_id)
- 1:N com `eventos` (usuario_id)
- 1:N com `favoritos` (usuario_id)
- 1:N com `historico_pontos` (usuario_id)
- 1:N com `notificacoes` (usuario_id)
- 1:N com `obras_autorais` (usuario_id)
- 1:N com `progresso_leitura` (usuario_id)
- 1:N com `resenhas` (usuario_id)

---

## 2. Tabela: `livros`

**Descrição:** Catálogo de livros disponíveis na biblioteca digital.

| Coluna | Tipo | Constraints | Descrição |
|--------|------|-------------|-----------|
| `id` | integer | PRIMARY KEY, AUTO_INCREMENT | Identificador único |
| `titulo` | varchar | NOT NULL | Título do livro |
| `autor` | varchar | NOT NULL | Autor do livro |
| `genero` | varchar | NOT NULL | Gênero literário |
| `ano_publicacao` | integer | - | Ano de publicação |
| `total_paginas` | integer | NOT NULL | Número total de páginas |
| `sinopse` | text | - | Sinopse/descrição do livro |
| `capa_url` | varchar | - | URL da imagem de capa (Open Library ou local) |
| `arquivo_url` | varchar | - | URL do arquivo PDF ou e-book |
| `nota_media` | numeric | DEFAULT 0.0 | Média de avaliações (1-5) |
| `qtd_avaliacoes` | integer | DEFAULT 0 | Quantidade de avaliações recebidas |
| `data_cadastro` | timestamp | DEFAULT CURRENT_TIMESTAMP | Data de registro no sistema |

**Relacionamentos:**
- 1:N com `favoritos` (livro_id)
- 1:N com `progresso_leitura` (livro_id)
- 1:N com `resenhas` (livro_id)

---

## 3. Tabela: `favoritos`

**Descrição:** Livros marcados como favoritos pelos usuários.

| Coluna | Tipo | Constraints | Descrição |
|--------|------|-------------|-----------|
| `id` | integer | PRIMARY KEY, AUTO_INCREMENT | Identificador único |
| `usuario_id` | integer | NOT NULL, FOREIGN KEY → usuarios(id) | Usuário que salvou o favorito |
| `livro_id` | integer | NOT NULL, FOREIGN KEY → livros(id) | Livro marcado como favorito |
| `data_adicionado` | timestamp | DEFAULT CURRENT_TIMESTAMP | Data em que foi favoritado |

**Notas:**
- Cada combinação (usuario_id, livro_id) deve ser única para evitar duplicatas
- Dados compartilhados entre múltiplas telas (`Estante`, `Acervo`, etc.)

---

## 4. Tabela: `progresso_leitura`

**Descrição:** Acompanha o progresso de cada usuário em seus livros.

| Coluna | Tipo | Constraints | Descrição |
|--------|------|-------------|-----------|
| `id` | integer | PRIMARY KEY, AUTO_INCREMENT | Identificador único |
| `usuario_id` | integer | NOT NULL, FOREIGN KEY → usuarios(id) | Usuário que está lendo |
| `livro_id` | integer | NOT NULL, FOREIGN KEY → livros(id) | Livro sendo lido |
| `pagina_atual` | integer | DEFAULT 0 | Página atual da leitura |
| `capitulo_atual` | varchar | - | Nome/número do capítulo atual |
| `porcentagem_conclusao` | numeric | DEFAULT 0.0 | Percentual de conclusão (0-100) |
| `status` | varchar | DEFAULT 'Lendo' | Status (Lendo, Pausado, Concluído) |
| `data_conclusao` | timestamp | - | Data em que foi concluído (se aplicável) |
| `data_ultimo_acesso` | timestamp | DEFAULT CURRENT_TIMESTAMP | Último acesso ao livro |

**Relacionamentos:**
- N:1 com `usuarios` (usuario_id)
- N:1 com `livros` (livro_id)

---

## 5. Tabela: `resenhas`

**Descrição:** Avaliações e reviews escritas pelos usuários sobre livros.

| Coluna | Tipo | Constraints | Descrição |
|--------|------|-------------|-----------|
| `id` | integer | PRIMARY KEY, AUTO_INCREMENT | Identificador única |
| `usuario_id` | integer | NOT NULL, FOREIGN KEY → usuarios(id) | Usuário que escreveu a resenha |
| `livro_id` | integer | NOT NULL, FOREIGN KEY → livros(id) | Livro sendo avaliado |
| `nota` | integer | NOT NULL, CHECK (1-5) | Nota de 1 a 5 estrelas |
| `texto` | text | - | Texto da resenha/review |
| `status` | varchar | DEFAULT 'Pendente' | Status (Pendente, Aprovado, Rejeitado) |
| `motivo_rejeicao` | text | - | Motivo caso seja rejeitada |
| `data_publicacao` | timestamp | DEFAULT CURRENT_TIMESTAMP | Data de criação/publicação |

**Notas:**
- Resenhas precisam de aprovação de moderador antes de serem públicas (fluxo de moderação)
- Contador `resenhas_inadequadas` em `usuarios` é incrementado quando uma resenha é marcada como inadequada

---

## 6. Tabela: `eventos`

**Descrição:** Eventos da comunidade (palestras, encontros de leitura, etc.).

| Coluna | Tipo | Constraints | Descrição |
|--------|------|-------------|-----------|
| `id` | integer | PRIMARY KEY, AUTO_INCREMENT | Identificador único |
| `usuario_id` | integer | NOT NULL, FOREIGN KEY → usuarios(id) | Criador/organizador do evento |
| `titulo` | varchar | NOT NULL | Título do evento |
| `tipo` | varchar | - | Tipo de evento (Palestra, Lançamento, Clube de Leitura, etc.) |
| `descricao` | text | - | Descrição breve do evento |
| `sobre` | text | - | Descrição detalhada/conteúdo sobre o evento |
| `banner_url` | varchar | - | URL do banner/imagem promocional |
| `data_inicio` | timestamp | NOT NULL | Data e hora de início |
| `data_fim` | timestamp | NOT NULL | Data e hora de término |
| `fuso_horario` | varchar | DEFAULT 'GMT-3' | Fuso horário (padrão: São Paulo) |
| `endereco` | varchar | - | Endereço físico (se presencial) |
| `complemento` | varchar | - | Complemento do endereço (apto, sala, etc.) |

**Relacionamentos:**
- N:1 com `usuarios` (usuario_id)

---

## 7. Tabela: `notificacoes`

**Descrição:** Notificações enviadas aos usuários pelo sistema ou por outros usuários.

| Coluna | Tipo | Constraints | Descrição |
|--------|------|-------------|-----------|
| `id` | integer | PRIMARY KEY, AUTO_INCREMENT | Identificador único |
| `usuario_id` | integer | NOT NULL, FOREIGN KEY → usuarios(id) | Usuário que recebe a notificação |
| `titulo` | varchar | NOT NULL | Título/assunto da notificação |
| `mensagem` | text | NOT NULL | Corpo da mensagem |
| `lido` | boolean | DEFAULT false | Se foi lida ou não |
| `data_envio` | timestamp | DEFAULT CURRENT_TIMESTAMP | Data e hora do envio |

**Notas:**
- Pode ser estendida com campos como `tipo` (comentário, favorito, etc.) e `referencia_id` para rastreamento de ações

---

## 8. Tabela: `obras_autorais`

**Descrição:** Livros/obras criados por usuários (autores) da plataforma, aguardando aprovação para publicação.

| Coluna | Tipo | Constraints | Descrição |
|--------|------|-------------|-----------|
| `id` | integer | PRIMARY KEY, AUTO_INCREMENT | Identificador único |
| `usuario_id` | integer | NOT NULL, FOREIGN KEY → usuarios(id) | Autor/criador da obra |
| `titulo` | varchar | NOT NULL | Título da obra |
| `genero` | varchar | NOT NULL | Gênero literário |
| `sinopse_curta` | text | NOT NULL | Sinopse resumida da obra |
| `arquivo_pdf_url` | varchar | NOT NULL | URL do arquivo PDF enviado |
| `status` | varchar | DEFAULT 'Pendente' | Status (Pendente, Aprovado, Rejeitado) |
| `motivo_rejeicao` | text | - | Motivo da rejeição se aplicável |
| `data_envio` | timestamp | DEFAULT CURRENT_TIMESTAMP | Data de submissão |

**Notas:**
- Fluxo de moderação: admin analisa e aprova/rejeita
- Uma obra aprovada pode ser transformada em um registro em `livros`

---

## 9. Tabela: `historico_pontos`

**Descrição:** Registro histórico de todas as transações de pontos de gamificação.

| Coluna | Tipo | Constraints | Descrição |
|--------|------|-------------|-----------|
| `id` | integer | PRIMARY KEY, AUTO_INCREMENT | Identificador único |
| `usuario_id` | integer | NOT NULL, FOREIGN KEY → usuarios(id) | Usuário que ganhou/perdeu pontos |
| `pontos_ganhos` | integer | NOT NULL | Quantidade de pontos (pode ser negativa) |
| `descricao` | varchar | NOT NULL | Descrição do motivo (ex: "Resenha aprovada", "Leitura concluída") |
| `tipo_referencia` | varchar | - | Tipo de referência (resenha, livro_concluido, evento, etc.) |
| `referencia_id` | integer | - | ID da entidade referenciada (livro_id, evento_id, etc.) |
| `data_ganho` | timestamp | DEFAULT CURRENT_TIMESTAMP | Data da transação |

**Notas:**
- Auditoria completa de pontos para fins de transparência e anti-fraude

---

## 10. Tabela: `atividades_admin`

**Descrição:** Log de ações realizadas por administradores (moderação, rejeição de obras, etc.).

| Coluna | Tipo | Constraints | Descrição |
|--------|------|-------------|-----------|
| `id` | integer | PRIMARY KEY, AUTO_INCREMENT | Identificador único |
| `admin_id` | integer | NOT NULL, FOREIGN KEY → usuarios(id) | Admin que realizou a ação |
| `titulo_atividade` | varchar | NOT NULL | Título/descrição da atividade |
| `icone_referencia` | varchar | - | Ícone para representar a atividade na UI |
| `livro_nome` | varchar | - | Nome do livro envolvido (se aplicável) |
| `autor_nome` | varchar | - | Nome do autor/criador envolvido (se aplicável) |
| `data_atividade` | timestamp | DEFAULT CURRENT_TIMESTAMP | Data e hora da atividade |

**Notas:**
- Rastreamento de todas as ações administrativas para auditoria
- Referências flexíveis (livro_nome, autor_nome) para histórico textual mesmo se registros forem eliminados

---

## 11. Tabela: `configuracoes_sistema`

**Descrição:** Configurações globais do sistema armazenadas no banco.

| Coluna | Tipo | Constraints | Descrição |
|--------|------|-------------|-----------|
| `chave` | varchar | PRIMARY KEY | Chave identificadora da configuração |
| `valor` | varchar | NOT NULL | Valor da configuração |
| `descricao` | text | - | Descrição/documentação da configuração |

**Exemplos de chaves:**
- `sistema_versao`: Versão atual do aplicativo
- `suporte_email`: Email para contato de suporte
- `pontos_por_resenha`: Pontos ganhos ao aprovar uma resenha
- `max_resenhas_inadequadas`: Limite de resenhas inadequadas antes de ação
- `manutencao_ativa`: Flag para colocar o sistema em manutenção

**Notas:**
- Permite ajustes em tempo real sem deploy de novo código
- Ideal para valores que mudam frequentemente (pontuações, limites, etc.)

---

## Relacionamentos e Integridades Referenciais

```
usuarios (núcleo central)
├── atividades_admin (admin_id)
├── eventos (usuario_id)
├── favoritos (usuario_id)
├── historico_pontos (usuario_id)
├── notificacoes (usuario_id)
├── obras_autorais (usuario_id)
├── progresso_leitura (usuario_id)
└── resenhas (usuario_id)

livros (catálogo)
├── favoritos (livro_id)
├── progresso_leitura (livro_id)
└── resenhas (livro_id)

auth.users (Supabase Auth)
└── usuarios (auth_user_id)
```

---

## Segurança e Row Level Security (RLS)

Todas as tabelas que contêm dados do usuário devem ter RLS habilitado:

- **favoritos**, **progresso_leitura**, **resenhas**, **historico_pontos**, **notificacoes**, **obras_autorais**
  - Acesso de leitura/escrita: Apenas o usuário proprietário (`auth.uid() = usuario_id`)
  
- **eventos**
  - Leitura: Todos os usuários autenticados podem visualizar eventos
  - Escrita: Apenas o criador ou admin pode modificar
  
- **atividades_admin**
  - Leitura: Apenas o admin que executou a ação ou super-admin
  - Escrita: Sistema/triggers apenas

- **usuarios**
  - Leitura: Usuário pode ler seu próprio perfil; admin pode ler todos
  - Escrita: Usuário pode atualizar seu perfil; auto-registrado via trigger no cadastro

- **livros**
  - Leitura: Todos (público)
  - Escrita: Admin apenas

- **configuracoes_sistema**
  - Leitura: Todos (dados não sensíveis)
  - Escrita: Admin apenas

---

## Sincronização com o Aplicativo

1. **Após Login:** O aplicativo consulta `usuarios` para obter dados do perfil actual
2. **Tela Home:** Carrega `livros` e `favoritos` do usuário
3. **Progresso de Leitura:** Sincroniza em tempo real via `progresso_leitura`
4. **Gamificação:** Consulta `pontos_gamificacao` e `nivel_gamificacao` em `usuarios`
5. **Eventos:** Lista completa disponível em `eventos`
6. **Notificações:** Push notifications mapeadas de `notificacoes`

---

## Índices Recomendados (Otimização)

```sql
-- Melhorar buscas por usuário
CREATE INDEX idx_favoritos_usuario ON favoritos(usuario_id);
CREATE INDEX idx_progresso_usuario ON progresso_leitura(usuario_id);
CREATE INDEX idx_resenhas_usuario ON resenhas(usuario_id);
CREATE INDEX idx_resenhas_livro ON resenhas(livro_id);
CREATE INDEX idx_historico_usuario ON historico_pontos(usuario_id);
CREATE INDEX idx_eventos_usuario ON eventos(usuario_id);
CREATE INDEX idx_notificacoes_usuario ON notificacoes(usuario_id);

-- Melhorar buscas por livro
CREATE INDEX idx_progresso_livro ON progresso_leitura(livro_id);
CREATE INDEX idx_favoritos_livro ON favoritos(livro_id);
```

---

## Próximos Passos

- [ ] Implementar RLS para todas as tabelas conforme especificado
- [ ] Criar triggers para sincronização automática (ex: ao criar usuário em `auth.users`, popular `usuarios`)
- [ ] Implementar políticas de replicação em tempo real via Supabase Realtime
- [ ] Adicionar índices de desempenho via Supabase Dashboard
- [ ] Testes de segurança para validar RLS policies


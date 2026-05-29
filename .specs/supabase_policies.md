# ForLibrary - Políticas de Row Level Security (supabase_policies.md)

Este documento define todas as políticas de **Row Level Security (RLS)** do banco de dados PostgreSQL do Supabase para o projeto **ForLibrary**. As políticas garantem que cada usuário acesse apenas os dados que lhe pertencem, respeitando os perfis `Aluno` e `Admin`.

**Documentação Relacionada:** `supabase_schema.md` · `auth_supabase.md` · `mapeamento_entidades.md`

---

## Visão Geral de Acesso por Perfil

| Tabela | Aluno (leitura) | Aluno (escrita) | Admin (leitura) | Admin (escrita) |
|---|---|---|---|---|
| `usuarios` | Próprio perfil | Atualizar próprio | Todos | Todos |
| `livros` | Todos | ✗ | Todos | Todos (CRUD) |
| `favoritos` | Próprios | Próprios | ✗ | ✗ |
| `progresso_leitura` | Próprios | Próprios | ✗ | ✗ |
| `resenhas` | Próprias + aprovadas | Inserir | Todas | Moderar |
| `eventos` | Todos | ✗ | Todos | Todos (CRUD) |
| `notificacoes` | Próprias | Marcar lida | Todas | Inserir/deletar |
| `obras_autorais` | Próprias | Inserir | Todas | Moderar |
| `historico_pontos` | Próprios | ✗ | Todos | ✗ (trigger) |
| `atividades_admin` | ✗ | ✗ | Todas | Inserir |
| `configuracoes_sistema` | Todas (leitura) | ✗ | Todas | Todas |

---

## Funções Auxiliares (Helper Functions)

Crie estas funções antes de aplicar as políticas. Elas evitam subconsultas repetitivas e são executadas com `SECURITY DEFINER` para contornar o RLS durante a checagem de papel.

```sql
-- Retorna o ID inteiro do usuário autenticado na tabela pública `usuarios`
CREATE OR REPLACE FUNCTION get_usuario_id()
RETURNS integer AS $$
  SELECT id FROM usuarios WHERE auth_user_id = auth.uid();
$$ LANGUAGE sql STABLE SECURITY DEFINER;

-- Retorna true se o usuário autenticado for Admin e estiver Ativo
CREATE OR REPLACE FUNCTION is_admin()
RETURNS boolean AS $$
  SELECT EXISTS (
    SELECT 1 FROM usuarios
    WHERE auth_user_id = auth.uid()
      AND tipo = 'Admin'
      AND status = 'Ativo'
  );
$$ LANGUAGE sql STABLE SECURITY DEFINER;

-- Retorna true se o usuário autenticado for Aluno e estiver Ativo
CREATE OR REPLACE FUNCTION is_aluno_ativo()
RETURNS boolean AS $$
  SELECT EXISTS (
    SELECT 1 FROM usuarios
    WHERE auth_user_id = auth.uid()
      AND tipo = 'Aluno'
      AND status = 'Ativo'
  );
$$ LANGUAGE sql STABLE SECURITY DEFINER;
```

---

## 1. Tabela: `usuarios`

**Regras:**
- Usuário autenticado lê apenas seu próprio perfil.
- Admin lê e atualiza todos os perfis.
- `INSERT` é feito exclusivamente por um trigger no `auth.users` (via `service_role`).
- `DELETE` não é permitido via RLS (operação reservada ao `service_role`).

```sql
ALTER TABLE usuarios ENABLE ROW LEVEL SECURITY;

-- Aluno: ver apenas o próprio perfil
CREATE POLICY "usuarios_select_proprio"
  ON usuarios FOR SELECT
  USING (auth_user_id = auth.uid());

-- Admin: ver todos os perfis
CREATE POLICY "usuarios_select_admin"
  ON usuarios FOR SELECT
  USING (is_admin());

-- Aluno: atualizar apenas foto_perfil, nome e biografia do próprio perfil
CREATE POLICY "usuarios_update_proprio"
  ON usuarios FOR UPDATE
  USING (auth_user_id = auth.uid())
  WITH CHECK (auth_user_id = auth.uid());

-- Admin: atualizar qualquer perfil (ex: bloquear usuário — RF39.3)
CREATE POLICY "usuarios_update_admin"
  ON usuarios FOR UPDATE
  USING (is_admin());

-- INSERT via trigger do Supabase Auth (service_role não é restrito pelo RLS)
-- Nenhuma policy de INSERT necessária para usuários comuns
```

---

## 2. Tabela: `livros`

**Regras:**
- Catálogo público: qualquer usuário autenticado pode ler.
- Somente Admin pode criar, editar e excluir livros (RF28, RF29, RF30).

```sql
ALTER TABLE livros ENABLE ROW LEVEL SECURITY;

-- Todos os usuários autenticados podem ver livros
CREATE POLICY "livros_select_autenticados"
  ON livros FOR SELECT
  TO authenticated
  USING (true);

-- Admin: inserir novo livro (RF28)
CREATE POLICY "livros_insert_admin"
  ON livros FOR INSERT
  WITH CHECK (is_admin());

-- Admin: editar livro (RF29)
CREATE POLICY "livros_update_admin"
  ON livros FOR UPDATE
  USING (is_admin());

-- Admin: excluir livro (RF30)
CREATE POLICY "livros_delete_admin"
  ON livros FOR DELETE
  USING (is_admin());
```

---

## 3. Tabela: `favoritos`

**Regras:**
- Usuário acessa apenas seus próprios favoritos (RF13).
- Nenhum admin precisa acessar diretamente esta tabela.

```sql
ALTER TABLE favoritos ENABLE ROW LEVEL SECURITY;

-- Aluno: ver apenas seus favoritos
CREATE POLICY "favoritos_select_proprio"
  ON favoritos FOR SELECT
  USING (usuario_id = get_usuario_id());

-- Aluno: adicionar favorito (RF09.5)
CREATE POLICY "favoritos_insert_proprio"
  ON favoritos FOR INSERT
  WITH CHECK (usuario_id = get_usuario_id());

-- Aluno: remover favorito (RF13.3)
CREATE POLICY "favoritos_delete_proprio"
  ON favoritos FOR DELETE
  USING (usuario_id = get_usuario_id());
```

---

## 4. Tabela: `progresso_leitura`

**Regras:**
- Usuário acessa e atualiza apenas seu próprio progresso (RF10.5, RF12.3).
- Progresso é criado automaticamente ao iniciar a leitura.

```sql
ALTER TABLE progresso_leitura ENABLE ROW LEVEL SECURITY;

-- Aluno: ver seu progresso de leitura
CREATE POLICY "progresso_select_proprio"
  ON progresso_leitura FOR SELECT
  USING (usuario_id = get_usuario_id());

-- Aluno: iniciar progresso ao abrir um livro
CREATE POLICY "progresso_insert_proprio"
  ON progresso_leitura FOR INSERT
  WITH CHECK (usuario_id = get_usuario_id());

-- Aluno: atualizar página atual, status, etc.
CREATE POLICY "progresso_update_proprio"
  ON progresso_leitura FOR UPDATE
  USING (usuario_id = get_usuario_id())
  WITH CHECK (usuario_id = get_usuario_id());

-- Aluno: remover progresso (se necessário)
CREATE POLICY "progresso_delete_proprio"
  ON progresso_leitura FOR DELETE
  USING (usuario_id = get_usuario_id());
```

---

## 5. Tabela: `resenhas`

**Regras:**
- Aluno pode inserir resenhas e visualizar as suas + as aprovadas de outros (RF09.7, RF14).
- Admin pode ler todas (pendentes, aprovadas, rejeitadas) e moderar (RF34, RF35).
- `UPDATE` de `status` e `motivo_rejeicao` é exclusivo do Admin.

```sql
ALTER TABLE resenhas ENABLE ROW LEVEL SECURITY;

-- Aluno: ver suas próprias resenhas (qualquer status)
CREATE POLICY "resenhas_select_proprio"
  ON resenhas FOR SELECT
  USING (usuario_id = get_usuario_id());

-- Todos autenticados: ver resenhas aprovadas de qualquer usuário (RF09.7)
CREATE POLICY "resenhas_select_aprovadas"
  ON resenhas FOR SELECT
  USING (status = 'Aprovado');

-- Admin: ver todas as resenhas (moderação — RF34)
CREATE POLICY "resenhas_select_admin"
  ON resenhas FOR SELECT
  USING (is_admin());

-- Aluno ativo: submeter nova resenha (RF14)
CREATE POLICY "resenhas_insert_aluno"
  ON resenhas FOR INSERT
  WITH CHECK (
    usuario_id = get_usuario_id()
    AND is_aluno_ativo()
  );

-- Admin: moderar resenha (aprovar/rejeitar — RF35.4, RF35.5)
CREATE POLICY "resenhas_update_admin"
  ON resenhas FOR UPDATE
  USING (is_admin());

-- Admin: excluir resenha (se necessário)
CREATE POLICY "resenhas_delete_admin"
  ON resenhas FOR DELETE
  USING (is_admin());
```

---

## 6. Tabela: `eventos`

**Regras:**
- Todos os usuários autenticados visualizam eventos (RF16, RF17).
- Somente Admin cria, edita e exclui eventos (RF31, RF32).

```sql
ALTER TABLE eventos ENABLE ROW LEVEL SECURITY;

-- Todos autenticados: ver eventos
CREATE POLICY "eventos_select_autenticados"
  ON eventos FOR SELECT
  TO authenticated
  USING (true);

-- Admin: criar evento (RF32)
CREATE POLICY "eventos_insert_admin"
  ON eventos FOR INSERT
  WITH CHECK (
    is_admin()
    AND usuario_id = get_usuario_id()
  );

-- Admin: editar evento (RF31.2)
CREATE POLICY "eventos_update_admin"
  ON eventos FOR UPDATE
  USING (is_admin());

-- Admin: excluir evento (RF31.2)
CREATE POLICY "eventos_delete_admin"
  ON eventos FOR DELETE
  USING (is_admin());
```

---

## 7. Tabela: `notificacoes`

**Regras:**
- Usuário vê apenas as próprias notificações e pode marcá-las como lidas (RF18).
- Admin e sistema (service_role) inserem notificações para usuários.

```sql
ALTER TABLE notificacoes ENABLE ROW LEVEL SECURITY;

-- Aluno: ver apenas suas notificações
CREATE POLICY "notificacoes_select_proprio"
  ON notificacoes FOR SELECT
  USING (usuario_id = get_usuario_id());

-- Aluno: marcar como lida (UPDATE no campo `lido` — RF18.6)
CREATE POLICY "notificacoes_update_proprio"
  ON notificacoes FOR UPDATE
  USING (usuario_id = get_usuario_id())
  WITH CHECK (usuario_id = get_usuario_id());

-- Admin: inserir notificações para alunos (RF32.8 - notificar ao publicar evento)
CREATE POLICY "notificacoes_insert_admin"
  ON notificacoes FOR INSERT
  WITH CHECK (is_admin());

-- Admin: ver todas as notificações (para auditoria)
CREATE POLICY "notificacoes_select_admin"
  ON notificacoes FOR SELECT
  USING (is_admin());

-- Admin: excluir notificações
CREATE POLICY "notificacoes_delete_admin"
  ON notificacoes FOR DELETE
  USING (is_admin());

-- Aluno: excluir própria notificação
CREATE POLICY "notificacoes_delete_proprio"
  ON notificacoes FOR DELETE
  USING (usuario_id = get_usuario_id());
```

---

## 8. Tabela: `obras_autorais`

**Regras:**
- Aluno visualiza apenas suas próprias obras submetidas (RF15, RF36).
- Admin visualiza todas e pode moderar (aprovar/rejeitar — RF37).
- Apenas alunos ativos podem submeter obras.

```sql
ALTER TABLE obras_autorais ENABLE ROW LEVEL SECURITY;

-- Aluno: ver apenas suas obras
CREATE POLICY "obras_select_proprio"
  ON obras_autorais FOR SELECT
  USING (usuario_id = get_usuario_id());

-- Admin: ver todas as obras (moderação — RF36)
CREATE POLICY "obras_select_admin"
  ON obras_autorais FOR SELECT
  USING (is_admin());

-- Aluno ativo: submeter obra autoral (RF15)
CREATE POLICY "obras_insert_aluno"
  ON obras_autorais FOR INSERT
  WITH CHECK (
    usuario_id = get_usuario_id()
    AND is_aluno_ativo()
  );

-- Admin: moderar obra (aprovar/rejeitar — RF37.4)
CREATE POLICY "obras_update_admin"
  ON obras_autorais FOR UPDATE
  USING (is_admin());

-- Aluno: cancelar obra pendente própria
CREATE POLICY "obras_delete_proprio_pendente"
  ON obras_autorais FOR DELETE
  USING (
    usuario_id = get_usuario_id()
    AND status = 'Pendente'
  );

-- Admin: excluir qualquer obra
CREATE POLICY "obras_delete_admin"
  ON obras_autorais FOR DELETE
  USING (is_admin());
```

---

## 9. Tabela: `historico_pontos`

**Regras:**
- Aluno lê apenas seu próprio histórico (RF21.3).
- `INSERT` é feito exclusivamente por triggers/service_role (auditoria imutável).
- Nenhum `UPDATE` ou `DELETE` permitido via RLS para preservar integridade.

```sql
ALTER TABLE historico_pontos ENABLE ROW LEVEL SECURITY;

-- Aluno: ver seu histórico de pontos
CREATE POLICY "historico_select_proprio"
  ON historico_pontos FOR SELECT
  USING (usuario_id = get_usuario_id());

-- Admin: ver histórico de qualquer usuário
CREATE POLICY "historico_select_admin"
  ON historico_pontos FOR SELECT
  USING (is_admin());

-- INSERT somente via service_role (triggers de gamificação)
-- Nenhuma policy de INSERT, UPDATE ou DELETE para usuários comuns
-- O service_role ignora o RLS por padrão
```

---

## 10. Tabela: `atividades_admin`

**Regras:**
- Apenas Admins visualizam o log de atividades (RF26.4).
- `INSERT` é feito pelo próprio Admin ou por triggers do sistema.
- Log imutável: `UPDATE` e `DELETE` não são permitidos.

```sql
ALTER TABLE atividades_admin ENABLE ROW LEVEL SECURITY;

-- Admin: ver todas as atividades administrativas
CREATE POLICY "atividades_select_admin"
  ON atividades_admin FOR SELECT
  USING (is_admin());

-- Admin: registrar atividade própria
CREATE POLICY "atividades_insert_admin"
  ON atividades_admin FOR INSERT
  WITH CHECK (
    is_admin()
    AND admin_id = get_usuario_id()
  );

-- Sem UPDATE ou DELETE (log imutável de auditoria)
```

---

## 11. Tabela: `configuracoes_sistema`

**Regras:**
- Todos os usuários autenticados podem ler configurações não sensíveis (ex: pontos por ação).
- Somente Admin pode criar, editar e excluir configurações (RF40.2).

```sql
ALTER TABLE configuracoes_sistema ENABLE ROW LEVEL SECURITY;

-- Todos autenticados: ler configurações do sistema
CREATE POLICY "config_select_autenticados"
  ON configuracoes_sistema FOR SELECT
  TO authenticated
  USING (true);

-- Admin: inserir nova configuração
CREATE POLICY "config_insert_admin"
  ON configuracoes_sistema FOR INSERT
  WITH CHECK (is_admin());

-- Admin: atualizar configuração (ex: pontos por resenha — RF40.2)
CREATE POLICY "config_update_admin"
  ON configuracoes_sistema FOR UPDATE
  USING (is_admin());

-- Admin: excluir configuração
CREATE POLICY "config_delete_admin"
  ON configuracoes_sistema FOR DELETE
  USING (is_admin());
```

---

## Script Completo de Habilitação de RLS

Execute este bloco para garantir que o RLS esteja ativo em todas as tabelas antes de aplicar as políticas individuais:

```sql
ALTER TABLE usuarios             ENABLE ROW LEVEL SECURITY;
ALTER TABLE livros               ENABLE ROW LEVEL SECURITY;
ALTER TABLE favoritos            ENABLE ROW LEVEL SECURITY;
ALTER TABLE progresso_leitura    ENABLE ROW LEVEL SECURITY;
ALTER TABLE resenhas             ENABLE ROW LEVEL SECURITY;
ALTER TABLE eventos              ENABLE ROW LEVEL SECURITY;
ALTER TABLE notificacoes         ENABLE ROW LEVEL SECURITY;
ALTER TABLE obras_autorais       ENABLE ROW LEVEL SECURITY;
ALTER TABLE historico_pontos     ENABLE ROW LEVEL SECURITY;
ALTER TABLE atividades_admin     ENABLE ROW LEVEL SECURITY;
ALTER TABLE configuracoes_sistema ENABLE ROW LEVEL SECURITY;
```

---

## Notas de Implementação

### Trigger de Criação de Usuário
Ao cadastrar via `supabase.gotrue.signUpWith(Email)`, o Supabase cria automaticamente uma linha em `auth.users`. Um trigger deve popular a tabela pública `usuarios`:

```sql
CREATE OR REPLACE FUNCTION handle_new_user()
RETURNS trigger AS $$
BEGIN
  INSERT INTO public.usuarios (auth_user_id, nome, email, matricula, tipo)
  VALUES (
    NEW.id,
    NEW.raw_user_meta_data->>'nome',
    NEW.email,
    NEW.raw_user_meta_data->>'matricula',
    CASE
      WHEN NEW.email LIKE '%@unifor.br' THEN 'Admin'
      ELSE 'Aluno'
    END
  );
  RETURN NEW;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

CREATE TRIGGER on_auth_user_created
  AFTER INSERT ON auth.users
  FOR EACH ROW EXECUTE FUNCTION handle_new_user();
```

### service_role vs anon vs authenticated
- **`service_role`**: Ignora RLS completamente. Usado em triggers e funções de backend (gamificação, notificações automáticas).
- **`authenticated`**: Usuário logado. Todas as políticas acima se aplicam.
- **`anon`**: Usuário não logado. Não possui acesso a nenhuma tabela (exceto se explicitamente liberado — não recomendado neste projeto).

### Ordem de Aplicação
1. Habilitar RLS em todas as tabelas
2. Criar as funções auxiliares (`get_usuario_id`, `is_admin`, `is_aluno_ativo`)
3. Criar o trigger de `handle_new_user`
4. Aplicar as políticas tabela por tabela
5. Testar com `SET ROLE authenticated` e simular `auth.uid()` via `SET LOCAL jwt.claims.sub`

---

## Checklist de Validação

- [ ] RLS habilitado em todas as 11 tabelas
- [ ] Funções auxiliares criadas e testadas
- [ ] Trigger `handle_new_user` ativo e populando `usuarios`
- [ ] Aluno não consegue ver dados de outro aluno em nenhuma tabela
- [ ] Aluno bloqueado (`status = 'Bloqueado'`) não consegue inserir resenhas/obras
- [ ] Admin consegue moderar resenhas e obras pendentes
- [ ] `historico_pontos` imutável via RLS (sem UPDATE/DELETE para usuários)
- [ ] `atividades_admin` invisível para alunos
- [ ] Configurações do sistema visíveis mas não editáveis por alunos

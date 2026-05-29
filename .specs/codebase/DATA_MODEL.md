# DATA_MODEL.md — Modelo de Dados do ForLibrary

> Última sincronização com o código: 2026-05-29 (branch `lucasdev`).

> ⚠️ **Realidade vs. modelo abaixo:** **não existe** o pacote `core/domain/model/` no código. As `data class` de domínio desta seção são uma **referência aspiracional**. O que existe de fato são **DTOs `@Serializable` por feature** (campos em `snake_case`, casando com as colunas), em geral declarados dentro dos ViewModels. Ver tabela "DTOs reais" ao final.

## Entidades de Domínio (referência aspiracional)

### Usuario
```kotlin
data class Usuario(
    val id: Long = 0,
    val nome: String,
    val matricula: String,
    val email: String,                        // Apenas @unifor.br ou @edu.unifor.br
    val fotoPerfil: String? = null,
    val biografia: String? = null,
    val tipo: TipoUsuario,                    // ALUNO | ADMIN
    val pontosGamificacao: Int = 0,
    val nivelGamificacao: Int = 1,            // 1–5
    val status: StatusUsuario = ATIVO,        // ATIVO | BLOQUEADO
    val resenhasInadequadas: Int = 0
)
```

### Livro
```kotlin
data class Livro(
    val id: Long = 0,
    val titulo: String,
    val autor: String,
    val genero: String,                       // Ver lista oficial em CONVENTIONS.md
    val anoPublicacao: Int?,
    val totalPaginas: Int,
    val sinopse: String?,
    val capaUrl: String?,                     // Supabase Storage ou ISBN via OpenLibrary
    val arquivoUrl: String?,                  // Supabase Storage — PDF/ePub
    val notaMedia: Double = 0.0,
    val qtdAvaliacoes: Int = 0,
    val dataCadastro: LocalDateTime = LocalDateTime.now()
)
```

### ProgressoLeitura
```kotlin
data class ProgressoLeitura(
    val id: Long = 0,
    val usuarioId: Long,
    val livroId: Long,
    val paginaAtual: Int = 0,
    val capituloAtual: String? = null,
    val porcentagemConclusao: Double = 0.0,
    val status: StatusLeitura = LENDO,        // LENDO | CONCLUIDO
    val dataConclusao: LocalDateTime? = null,
    val dataUltimoAcesso: LocalDateTime = LocalDateTime.now()
)
```

### Resenha
```kotlin
data class Resenha(
    val id: Long = 0,
    val usuarioId: Long,
    val livroId: Long,
    val nota: Int,                            // 1–5 estrelas (obrigatório)
    val texto: String?,                       // Opcional, até 500 chars
    val status: StatusModeracao = PENDENTE,   // PENDENTE | APROVADA | REJEITADA
    val motivoRejeicao: String? = null,
    val dataPublicacao: LocalDateTime = LocalDateTime.now()
)
```

### ObraAutoral
```kotlin
data class ObraAutoral(
    val id: Long = 0,
    val usuarioId: Long,
    val titulo: String,
    val genero: String,
    val sinopseCurta: String,
    val arquivoPdfUrl: String,
    val status: StatusModeracao = PENDENTE,
    val motivoRejeicao: String? = null,
    val dataEnvio: LocalDateTime = LocalDateTime.now()
)
```

### Evento
```kotlin
data class Evento(
    val id: Long = 0,
    val usuarioId: Long,                      // Admin criador
    val titulo: String,
    val tipo: TipoEvento,                     // WORKSHOP | PALESTRA | LANCAMENTO
    val descricao: String?,
    val sobre: String?,
    val bannerUrl: String?,
    val dataInicio: LocalDateTime,
    val dataFim: LocalDateTime,
    val fusoHorario: String = "GMT-3",
    val endereco: String?,
    val complemento: String?
)
```

### Notificacao
```kotlin
data class Notificacao(
    val id: Long = 0,
    val usuarioId: Long,
    val titulo: String,
    val mensagem: String,
    val lido: Boolean = false,
    val dataEnvio: LocalDateTime = LocalDateTime.now()
)
```

### HistoricoPontos
```kotlin
data class HistoricoPontos(
    val id: Long = 0,
    val usuarioId: Long,
    val pontosGanhos: Int,
    val descricao: String,                    // Ex: "+50 pontos por concluir livro X"
    val tipoReferencia: TipoReferenciaExtrato?,
    val referenciaId: Long?
)

enum class TipoReferenciaExtrato { PONTO, RESENHA, OBRA, EVENTO }
```

### ConfiguracaoSistema
```kotlin
data class ConfiguracaoSistema(
    val chave: String,                        // Ex: "PONTOS_POR_LIVRO"
    val valor: String,                        // Ex: "50"
    val descricao: String?
)
```

---

## Tabelas Supabase (public schema)

| Tabela | Mapeamento | Notas |
|---|---|---|
| `usuarios` | `Usuario` | Criada via trigger no `auth.users` |
| `livros` | `Livro` | RLS: leitura pública, escrita só admin |
| `favoritos` | `Favorito` | N:M entre `usuarios` e `livros` |
| `progresso_leitura` | `ProgressoLeitura` | RLS: apenas dono lê/escreve |
| `resenhas` | `Resenha` | RLS: aluno cria, admin modera |
| `eventos` | `Evento` | RLS: admin cria, alunos só lêem |
| `notificacoes` | `Notificacao` | RLS: destinatário lê, admin escreve |
| `obras_autorais` | `ObraAutoral` | RLS: aluno envia, admin modera |
| `historico_pontos` | `HistoricoPontos` | RLS: append-only pelo sistema |
| `atividades_admin` | `AtividadeAdmin` | Audit log admin |
| `configuracoes_sistema` | `ConfiguracaoSistema` | Apenas admin lê/escreve |

## DTOs reais no código (`@Serializable`)

| DTO | Local | Tabela | Observações |
|---|---|---|---|
| `LivroAcervo` | `aluno/acervo/viewmodel/AcervoViewModel.kt` | `livros` | id `Int`, `capa_url`, `genero`, `data_cadastro`, `avaliacao`; campo derivado `isNovo` |
| `UsuarioAcervo` | idem | `usuarios` | `nome`, `foto_perfil`; lido por `auth_user_id == auth.uid` |
| `NovoLivro` | `adm/acervo/CadastroLivroViewModel.kt` (private) | `livros` | payload de insert: `total_paginas`, `ano_publicacao`, `capa_url`, `arquivo_url` |
| `EventoDb` | `aluno/eventos/viewmodel/EventoDb.kt` | `eventos` | `banner_url`, `data_inicio/fim`, `fuso_horario`; convertido p/ `Evento` (UI) via `toEvento()` |

> A coluna de vínculo de usuário usada nas queries é **`auth_user_id`** (= `auth.users.id`), não um `id` numérico próprio.

## Regras de Gamificação

| Ação | Pontos |
|---|---|
| Concluir leitura de um livro | +50 |
| Ter resenha aprovada | +15 |

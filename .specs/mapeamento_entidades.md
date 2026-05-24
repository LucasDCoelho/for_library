# Mapeamento de Entidades — For Library

**Pacote base:** `com.br.unifor.for_library.core.domain.model`

---

## Usuario

```kotlin
package com.br.unifor.for_library.core.domain.model

data class Usuario(
    val id: Long = 0,
    val nome: String,
    val matricula: String,
    val email: String,
    val fotoPerfil: String? = null,
    val biografia: String? = null,
    val tipo: TipoUsuario, // Enum: ALUNO, ADMIN
    val pontosGamificacao: Int = 0,
    val nivelGamificacao: Int = 1,
    val status: StatusUsuario = StatusUsuario.ATIVO, // Enum: ATIVO, BLOQUEADO
    val resenhasInadequadas: Int = 0
)

enum class TipoUsuario {
    ALUNO, ADMIN
}

enum class StatusUsuario {
    ATIVO, BLOQUEADO
}
```
## Livro

```kotlin
package com.br.unifor.for_library.core.domain.model

import java.time.LocalDateTime

data class Livro(
    val id: Long = 0,
    val titulo: String,
    val autor: String,
    val genero: String,
    val anoPublicacao: Int?,
    val totalPaginas: Int,
    val sinopse: String?,
    val capaUrl: String?,
    val arquivoUrl: String?,         // Caminho lógico/físico para o arquivo PDF/ePub
    val notaMedia: Double = 0.0,
    val qtdAvaliacoes: Int = 0,
    val dataCadastro: LocalDateTime = LocalDateTime.now()
)
```

---

## ProgressoLeitura

```kotlin
package com.br.unifor.for_library.core.domain.model

import java.time.LocalDateTime

data class ProgressoLeitura(
    val id: Long = 0,
    val usuarioId: Long,             // Chave Estrangeira: Usuario
    val livroId: Long,               // Chave Estrangeira: Livro
    val paginaAtual: Int = 0,
    val capituloAtual: String? = null,
    val porcentagemConclusao: Double = 0.0,
    val status: StatusLeitura = StatusLeitura.LENDO,
    val dataConclusao: LocalDateTime? = null,
    val dataUltimoAcesso: LocalDateTime = LocalDateTime.now()
)

enum class StatusLeitura {
    LENDO, CONCLUIDO
}
```

---

## Favorito

```kotlin
package com.br.unifor.for_library.core.domain.model

import java.time.LocalDateTime

data class Favorito(
    val id: Long = 0,
    val usuarioId: Long,             // Chave Estrangeira: Usuario
    val livroId: Long,               // Chave Estrangeira: Livro
    val dataAdicionado: LocalDateTime = LocalDateTime.now()
)
```

---

## Resenha

```kotlin
package com.br.unifor.for_library.core.domain.model

import java.time.LocalDateTime

data class Resenha(
    val id: Long = 0,
    val usuarioId: Long,             // Chave Estrangeira: Usuario
    val livroId: Long,               // Chave Estrangeira: Livro
    val nota: Int,                   // Validação obrigatória: 1 a 5 estrelas
    val texto: String?,              // Texto descritivo opcional (até 500 caracteres)
    val status: StatusModeracao = StatusModeracao.PENDENTE,
    val motivoRejeicao: String? = null,
    val dataPublicacao: LocalDateTime = LocalDateTime.now()
)

enum class StatusModeracao {
    PENDENTE, APROVADA, REJEITADA
}
```

---

## ObraAutoral

```kotlin
package com.br.unifor.for_library.core.domain.model

import java.time.LocalDateTime

data class ObraAutoral(
    val id: Long = 0,
    val usuarioId: Long,             // Chave Estrangeira: Aluno remetente
    val titulo: String,
    val genero: String,
    val sinopseCurta: String,
    val arquivoPdfUrl: String,       // Caminho temporário/final do PDF no servidor
    val status: StatusModeracao = StatusModeracao.PENDENTE,
    val motivoRejeicao: String? = null,
    val dataEnvio: LocalDateTime = LocalDateTime.now()
)
```

> `StatusModeracao` compartilhado com `Resenha`: `PENDENTE`, `APROVADA`, `REJEITADA`

---

## Evento

```kotlin
package com.br.unifor.for_library.core.domain.model

import java.time.LocalDateTime

data class Evento(
    val id: Long = 0,
    val usuarioId: Long,             // Chave Estrangeira: Administrador criador
    val titulo: String,
    val tipo: TipoEvento,
    val descricao: String?,
    val sobre: String?,
    val bannerUrl: String?,
    val dataInicio: LocalDateTime,
    val dataFim: LocalDateTime,
    val fusoHorario: String = "GMT-3",
    val endereco: String?,
    val complemento: String?
)

enum class TipoEvento {
    WORKSHOP, PALESTRA, LANCAMENTO
}
```

---

## Notificacao

```kotlin
package com.br.unifor.for_library.core.domain.model

import java.time.LocalDateTime

data class Notificacao(
    val id: Long = 0,
    val usuarioId: Long,             // Chave Estrangeira: Destinatário do alerta
    val titulo: String,
    val mensagem: String,
    val lido: Boolean = false,
    val dataEnvio: LocalDateTime = LocalDateTime.now()
)
```

---

## HistoricoPontos

```kotlin
package com.br.unifor.for_library.core.domain.model

import java.time.LocalDateTime

data class HistoricoPontos(
    val id: Long = 0,
    val usuarioId: Long,             // Chave Estrangeira: Aluno bonificado
    val pontosGanhos: Int,
    val descricao: String,           // Ex: "+50 pontos por concluir livro X"
    val tipoReferencia: TipoReferenciaExtrato?,
    val referenciaId: Long?,         // ID polimórfico mapeado dinamicamente via código
    val dataGanho: LocalDateTime = LocalDateTime.now()
)

enum class TipoReferenciaExtrato {
    PONTO, RESENHA, OBRA, EVENTO
}
```

---

## AtividadeAdmin

```kotlin
package com.br.unifor.for_library.core.domain.model

import java.time.LocalDateTime

data class AtividadeAdmin(
    val id: Long = 0,
    val adminId: Long,               // Chave Estrangeira: Administrador operante
    val tituloAtividade: String,
    val iconeReferencia: String?,
    val livroNome: String?,
    val autorNome: String?,
    val dataAtividade: LocalDateTime = LocalDateTime.now()
)
```

---

## ConfiguracaoSistema

```kotlin
package com.br.unifor.for_library.core.domain.model

data class ConfiguracaoSistema(
    val chave: String,               // Chave primária textual (ex: "PONTOS_POR_LIVRO")
    val valor: String,               // Valor correspondente em string (ex: "50")
    val descricao: String?
)
```
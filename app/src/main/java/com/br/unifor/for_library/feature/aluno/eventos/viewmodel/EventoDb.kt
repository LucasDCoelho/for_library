package com.br.unifor.for_library.feature.aluno.eventos.viewmodel

import kotlinx.serialization.Serializable

@Serializable
data class EventoDb(
    val id: Int,
    val titulo: String,
    val tipo: String = "",
    val descricao: String? = null,
    val sobre: String? = null,
    val banner_url: String? = null,
    val data_inicio: String? = null,
    val data_fim: String? = null,
    val fuso_horario: String = "GMT-3",
    val endereco: String? = null,
    val complemento: String? = null
)

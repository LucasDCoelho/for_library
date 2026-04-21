package com.br.unifor.for_library.core

import androidx.compose.runtime.mutableStateListOf

// Catálogo unificado — todos os livros que aparecem na Home e no Acervo
data class LivroCatalogo(val titulo: String, val autor: String, val isbn: String)

val catalogoGlobal = listOf(
    // Home
    LivroCatalogo("O Alquimista",          "Paulo Coelho",              "9780062315007"),
    LivroCatalogo("Dom Casmurro",          "Machado de Assis",          "9788535902778"),
    LivroCatalogo("1984",                  "George Orwell",             "9780451524935"),
    LivroCatalogo("Clean Code",            "Robert C. Martin",          "9780132350884"),
    LivroCatalogo("Atomic Habits",         "James Clear",               "9780735211292"),
    // Acervo
    LivroCatalogo("O Design do Dia a Dia", "Don Norman",                "9780465050659"),
    LivroCatalogo("Sapiens",               "Yuval Noah Harari",         "9788543102146"),
    LivroCatalogo("O Pequeno Príncipe",    "Antoine de Saint-Exupéry",  "9788522031412"),
    LivroCatalogo("Fundamentos da Gestão", "Peter Drucker",             "9788522102716"),
)

// Estado global de ISBNs salvos — pré-populado com os que estavam marcados nos mocks
object LivrosSalvosState {
    val isbnsSalvos = mutableStateListOf(
        "9788535902778", // Dom Casmurro  (home - salvo = true)
        "9780132350884"  // Clean Code    (home - salvo = true)
    )

    fun toggleSalvo(isbn: String) {
        if (isbn in isbnsSalvos) isbnsSalvos.remove(isbn) else isbnsSalvos.add(isbn)
    }

    fun isSalvo(isbn: String): Boolean = isbn in isbnsSalvos
}
package com.example.reactcypressexperiment

data class Book(
    var id: Int = 0,
    val author: String? = null,
    val title: String? = null,
    val recommendedReadingAge: Int? = null
)

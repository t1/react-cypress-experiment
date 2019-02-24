package com.example.reactcypressexperiment

data class Book(
    var id: Int = NO_ID,
    val author: String? = null,
    val title: String? = null,
    val recommendedReadingAge: Int? = null

) {
    companion object {
        const val NO_ID = -1
    }
}

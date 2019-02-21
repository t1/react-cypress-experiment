package com.example.reactcypressexperiment

import org.springframework.stereotype.Repository

@Repository
class BookStore {
    var all = mutableListOf<Book>()

    operator fun get(id: Int): Book = all.find { it.id == id } ?: throw BookNotFoundException(id)

    fun add(book: Book) {
        book.id = maxId() + 1
        all.add(book)
    }

    private fun maxId(): Int = all.map { it.id }.max() ?: 0
}

package com.example.reactcypressexperiment

import com.example.reactcypressexperiment.Book.Companion.NO_ID
import org.springframework.stereotype.Repository

@Repository
class BookStore {
    var all = mutableListOf<Book>()

    operator fun get(id: Int): Book = all.find { it.id == id } ?: throw BookNotFoundException(id)

    fun add(book: Book): Boolean =
        if (book.id == NO_ID) {
            book.id = maxId() + 1
            all.add(book)
            true
        } else {
            val i = all.indexOfFirst { it.id == book.id }
            if (i < 0) throw BookNotFoundException(book.id)
            all[i] = book
            false
        }

    private fun maxId(): Int = all.maxBy { it.id }?.id ?: 0
}

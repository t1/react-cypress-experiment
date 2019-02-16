package com.example.reactcypressexperiment

import org.springframework.stereotype.Repository

import java.util.Arrays.asList

@Repository
class BookStore {
    val all: List<Book>
        get() = asList(get(1), get(2), get(3))

    operator fun get(id: Int): Book {
        return when (id) {
            1 -> Book(id = 1, author = "J.R.R. Tolkien", title = "The Hobbit", recommendedReadingAge = 5)
            2 -> Book(id = 2, author = "J.R.R. Tolkien", title = "The Lord Of The Rings", recommendedReadingAge = 14)
            3 -> Book(id = 3, author = "Steven King", title = "It", recommendedReadingAge = 16)
            else -> throw BookNotFoundException(id)
        }
    }
}

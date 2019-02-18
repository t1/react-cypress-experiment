package com.example.reactcypressexperiment

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.Thread.sleep

@RestController
@RequestMapping("/books")
@CrossOrigin
class BookBoundary(
    private val bookStore: BookStore
) {

    @GetMapping fun getAllBooks(): List<Book> {
        sleep(1000)
        return bookStore.all
    }

    @GetMapping("/{id}") fun getBook(@PathVariable id: Int): Book {
        if (id < 0) throw InvalidBookIdException(id)
        sleep(1000)
        return bookStore[id]
    }
}

package com.example.reactcypressexperiment

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.Thread.sleep
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/books")
@CrossOrigin
class BookBoundary(
    private val store: BookStore
) {

    @GetMapping fun getAllBooks(): List<Book> {
        sleep(1000)
        return store.all
    }

    @GetMapping("/{id}") fun getBook(@PathVariable id: Int): Book {
        if (id < 0) throw InvalidBookIdException(id)
        sleep(1000)
        return store[id]
    }

    @PostMapping fun postOneBooks(@Valid @RequestBody book: Book): ResponseEntity<Book> {
        sleep(1000)
        store.add(book)
        return ResponseEntity.created(URI.create("/books/" + book.id)).body(book); }

    @PutMapping fun putAllBooks(@Valid @RequestBody books: List<Book>) {
        sleep(1000)
        store.all = books.toMutableList()
    }
}

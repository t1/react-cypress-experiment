package com.example.reactcypressexperiment

import com.example.reactcypressexperiment.Book.Companion.NO_ID
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/books")
@CrossOrigin
class BookBoundary(
    private val store: BookStore
) {

    @GetMapping fun getAllBooks(): List<Book> = store.all

    @GetMapping("/{id}") fun getBook(@PathVariable id: Int): Book {
        if (id < 0) throw InvalidBookIdException(id)
        return store[id]
    }

    @PostMapping fun postBook(@Valid @RequestBody book: Book): ResponseEntity<Book> {
        val created = store.add(book)
        val response = when {
            created -> ResponseEntity.created(URI.create("/books/" + book.id))
            else -> ResponseEntity.ok()
        }
        return response.body(book)
    }

    @PostMapping("/{id}") fun postExitingBook(@PathVariable id: Int, @Valid @RequestBody book: Book): ResponseEntity<Book> {
        if (book.id != NO_ID && book.id != id) throw BookIdMismatchException(id, book)
        return postBook(book)
    }

    @PutMapping fun putAllBooks(@Valid @RequestBody books: List<Book>) {
        store.all = books.toMutableList()
    }
}

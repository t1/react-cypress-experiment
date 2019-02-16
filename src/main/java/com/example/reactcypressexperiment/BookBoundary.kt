package com.example.reactcypressexperiment

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
@CrossOrigin
class BookBoundary(
    private val bookStore: BookStore,
    private val mapper: BookMapper
) {

    @GetMapping fun getAllBooks(): List<BookListItem> = bookStore.all.map(mapper::toBookListItem)

    @GetMapping("/{id}") fun getBook(@PathVariable id: Int): Book = bookStore[id]
}

package com.example.reactcypressexperiment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookBoundary {

    private final BookStore bookStore;

    @Autowired public BookBoundary(BookStore bookStore) { this.bookStore = bookStore; }

    @GetMapping("/{id}") public Book getBook(@PathVariable int id) {
        return bookStore.get(id);
    }
}

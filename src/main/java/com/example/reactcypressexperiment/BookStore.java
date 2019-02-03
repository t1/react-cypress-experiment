package com.example.reactcypressexperiment;

import org.springframework.stereotype.Repository;

@Repository
public class BookStore {
    public Book get(int id) {
        switch (id) {
            case 1:
                return Book.by("J.R.R. Tolkien").setTitle("The Hobbit");
            default:
                throw new BookNotFoundException(id);
        }
    }
}

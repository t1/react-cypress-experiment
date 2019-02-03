package com.example.reactcypressexperiment;

import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.Arrays.asList;

@Repository
public class BookStore {
    public List<Book> getAll() {
        return asList(get(1), get(2));
    }

    public Book get(int id) {
        switch (id) {
            case 1:
                return Book.by("J.R.R. Tolkien").setTitle("The Hobbit");
            case 2:
                return Book.by("J.R.R. Tolkien").setTitle("The Lord Of The Rings");
            default:
                throw new BookNotFoundException(id);
        }
    }
}

package com.example.reactcypressexperiment;

import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.Arrays.asList;

@Repository
public class BookStore {
    public List<Book> getAll() {
        return asList(get(1), get(2), get(3));
    }

    public Book get(int id) {
        switch (id) {
            case 1:
                return Book.builder().id(1).author("J.R.R. Tolkien").title("The Hobbit").build();
            case 2:
                return Book.builder().id(2).author("J.R.R. Tolkien").title("The Lord Of The Rings").build();
            case 3:
                return Book.builder().id(3).author("Steven King").title("It").build();
            default:
                throw new BookNotFoundException(id);
        }
    }
}

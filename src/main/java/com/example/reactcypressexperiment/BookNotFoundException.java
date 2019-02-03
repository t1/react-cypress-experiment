package com.example.reactcypressexperiment;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(int id) { super("no book found with id " + id); }
}

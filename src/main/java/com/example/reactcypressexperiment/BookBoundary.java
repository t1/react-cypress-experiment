package com.example.reactcypressexperiment;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

@RestController
@RequestMapping("/books")
public class BookBoundary {
    @GetMapping("/{id}") public String getBook(@PathVariable int id) {
        if (id == 1)
            return "{\"author\":\"J.R.R. Tolkien\",\"title\":\"The Hobbit\"}";
        else
            throw new BookNotFoundException(id);
    }
}

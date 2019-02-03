package com.example.reactcypressexperiment;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookBoundaryTest {
    private final MockMvc mvc = MockMvcBuilders
        .standaloneSetup(BookBoundary.class)
        .build();

    @Test void shouldFailToGetBookWithoutId() throws Exception {
        mvc.perform(get("/books/"))

            .andExpect(status().isNotFound());
    }

    @Test void shouldFailToGetBookWithInvalidId() throws Exception {
        mvc.perform(get("/books/x"))

            .andExpect(status().isBadRequest())
        // .andExpect(content().json("{\"x\":\"y\"}"))
        ;
    }

    @Test void shouldFailToGetUnknownBook() throws Exception {
        mvc.perform(get("/books/-1"))

            .andExpect(status().isNotFound())
        // .andExpect(content().json("{\"x\":\"y\"}"))
        ;
    }

    @Test void shouldGetBook() throws Exception {
        mvc.perform(get("/books/1"))

            .andExpect(status().isOk())
            .andExpect(content().json("{" +
                "\"author\":\"J.R.R. Tolkien\"," +
                "\"title\":\"The Hobbit\"" +
                "}"));
    }
}

package com.example.reactcypressexperiment;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookBoundaryTest {
    private static final String THE_HOBBIT = "{" +
        "\"author\":\"J.R.R. Tolkien\"," +
        "\"title\":\"The Hobbit\"" +
        "}";
    private static final String THE_LORD_OF_THE_RINGS = "{" +
        "\"author\":\"J.R.R. Tolkien\"," +
        "\"title\":\"The Lord Of The Rings\"" +
        "}";

    private final MockMvc mvc = MockMvcBuilders.standaloneSetup(new BookBoundary(new BookStore())).build();


    @Test void shouldFailToGetBookWithInvalidId() throws Exception {
        mvc.perform(get("/books/x"))

            .andExpect(status().isBadRequest());
    }

    @Test void shouldFailToGetUnknownBook() throws Exception {
        mvc.perform(get("/books/-1"))

            .andExpect(status().isNotFound())
        // Can't check the problem body in MockMvc. It's not rfc-7807 anyway.
        // .andExpect(content().json("" +
        // "{" +
        // "    \"error\": \"Not Found\"," +
        // "    \"message\": \"no book found with id 2\"," +
        // "    \"path\": \"/books/2\"," +
        // "    \"status\": 404," +
        // "}"))
        ;
    }

    @Test void shouldGetBook() throws Exception {
        mvc.perform(get("/books/1"))

            .andExpect(status().isOk())
            .andExpect(content().json(THE_HOBBIT));
    }

    @Test void shouldGetAllBooks() throws Exception {
        mvc.perform(get("/books"))

            .andExpect(status().isOk())
            .andExpect(content().json("[" + THE_HOBBIT + "," + THE_LORD_OF_THE_RINGS + "]"));
    }
}

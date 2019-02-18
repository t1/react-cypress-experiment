package com.example.reactcypressexperiment

import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

internal class BookBoundaryTest {

    private val mvc = MockMvcBuilders.standaloneSetup(BookBoundary(BookStore())).build()

    @Test fun shouldFailToGetBookWithInvalidId() {
        mvc.perform(get("/books/x"))

            .andExpect(status().isBadRequest)
    }

    @Test fun shouldFailToGetBookWithNegativeId() {
        mvc.perform(get("/books/-1"))

            .andExpect(status().isBadRequest)
    }

    @Test fun shouldFailToGetUnknownBook() {
        mvc.perform(get("/books/9999999"))

            .andExpect(status().isNotFound) // Can't check the problem body in MockMvc. It's not rfc-7807 anyway.
        // .andExpect(content().json("" +
        // "{" +
        // "    \"error\": \"Not Found\"," +
        // "    \"message\": \"no book found with id 2\"," +
        // "    \"path\": \"/books/2\"," +
        // "    \"status\": 404," +
        // "}"))
    }

    @Test fun shouldGetBook() {
        mvc.perform(get("/books/1"))

            .andExpect(status().isOk)
            .andExpect(content().json("{" +
                "\"author\":\"J.R.R. Tolkien\"," +
                "\"title\":\"The Hobbit\"," +
                "\"recommendedReadingAge\":5" +
                "}"))
    }

    @Test fun shouldGetAllBooks() {
        mvc.perform(get("/books"))

            .andExpect(status().isOk)
            .andExpect(content().json("[" +
                "{" +
                "\"id\":1," +
                "\"author\":\"J.R.R. Tolkien\"," +
                "\"title\":\"The Hobbit\"" +
                "},{" +
                "\"id\":2," +
                "\"author\":\"J.R.R. Tolkien\"," +
                "\"title\":\"The Lord Of The Rings\"" +
                "},{" +
                "\"id\":3," +
                "\"author\":\"Steven King\"," +
                "\"title\":\"It\"" +
                "}" +
                "]"))
    }
}

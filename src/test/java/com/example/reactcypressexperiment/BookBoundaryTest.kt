package com.example.reactcypressexperiment

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

internal class BookBoundaryTest {

    private val mvc = MockMvcBuilders.standaloneSetup(BookBoundary(BookStore(), BookMapperImpl())).build()

    @Test fun shouldFailToGetBookWithInvalidId() {
        mvc.perform(get("/books/x"))

            .andExpect(status().isBadRequest)
    }

    @Test fun shouldFailToGetUnknownBook() {
        mvc.perform(get("/books/-1"))

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
        val result = mvc.perform(get("/books"))

        result.andExpect(status().isOk)
            .andExpect(content().json("[" +
                "{" +
                "\"author\":\"J.R.R. Tolkien\"," +
                "\"title\":\"The Hobbit\"" +
                "},{" +
                "\"author\":\"J.R.R. Tolkien\"," +
                "\"title\":\"The Lord Of The Rings\"" +
                "},{" +
                "\"author\":\"Steven King\"," +
                "\"title\":\"It\"" +
                "}" +
                "]"))
        assertThat(result.andReturn().response.contentAsString).doesNotContain("recommended")
    }
}

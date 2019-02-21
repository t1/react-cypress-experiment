package com.example.reactcypressexperiment

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class BookBoundaryTest {
    companion object {
        private const val BOOKS_JSON = "[" +
            "{" +
            "\"id\":1," +
            "\"author\":\"J.R.R. Tolkien\"," +
            "\"title\":\"The Hobbit\"," +
            "\"recommendedReadingAge\":5" +
            "},{" +
            "\"id\":2," +
            "\"author\":\"J.R.R. Tolkien\"," +
            "\"title\":\"The Lord Of The Rings\"," +
            "\"recommendedReadingAge\":14" +
            "},{" +
            "\"id\":3," +
            "\"author\":\"Steven King\"," +
            "\"title\":\"It\"," +
            "\"recommendedReadingAge\":16" +
            "}" +
            "]"
    }

    private val bookStore = BookStore().apply {
        all.add(Book(id = 1, author = "J.R.R. Tolkien", title = "The Hobbit", recommendedReadingAge = 5))
        all.add(Book(id = 2, author = "J.R.R. Tolkien", title = "The Lord Of The Rings", recommendedReadingAge = 14))
        all.add(Book(id = 3, author = "Steven King", title = "It", recommendedReadingAge = 16))
    }
    private val mvc = MockMvcBuilders.standaloneSetup(BookBoundary(bookStore)).build()

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
            .andExpect(content().json(BOOKS_JSON))
    }

    @Test fun shouldPostNewBook() {
        val json = "{" +
            "\"author\":\"Marion Zimmer Bradley\"," +
            "\"title\":\"The Mists of Avalon\"," +
            "\"recommendedReadingAge\":15" +
            "}"
        mvc.perform(post("/books").content(json).contentType(APPLICATION_JSON_UTF8))

            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "/books/4"))
            .andExpect(content().json("{\"id\":4," + json.substring(1)))
        assertThat(bookStore.all.last()).isEqualTo(Book(
            id = 4,
            author = "Marion Zimmer Bradley",
            title = "The Mists of Avalon",
            recommendedReadingAge = 15
        ))
    }

    @Test fun shouldPutAllBooks() {
        val books = bookStore.all
        bookStore.all = mutableListOf()

        mvc.perform(put("/books").content(BOOKS_JSON).contentType(APPLICATION_JSON_UTF8))

            .andExpect(status().isOk)
        assertThat(bookStore.all).containsExactlyElementsOf(books)
    }
}

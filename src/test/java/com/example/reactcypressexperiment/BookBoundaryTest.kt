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
        private const val THE_HOBBIT = "{" +
            "\"id\":1," +
            "\"author\":\"J.R.R. Tolkien\"," +
            "\"title\":\"The Hobbit\"," +
            "\"recommendedReadingAge\":5" +
            "}"
        private const val THE_LORD_OF_THE_RINGS = "{" +
            "\"id\":2," +
            "\"author\":\"J.R.R. Tolkien\"," +
            "\"title\":\"The Lord Of The Rings\"," +
            "\"recommendedReadingAge\":14" +
            "}"
        private const val IT = "{" +
            "\"id\":3," +
            "\"author\":\"Steven King\"," +
            "\"title\":\"It\"," +
            "\"recommendedReadingAge\":16" +
            "}"

        private const val BOOKS_JSON = "[$THE_HOBBIT,$THE_LORD_OF_THE_RINGS,$IT]"

        private const val THE_MISTS_OF_AVALON = "{" +
            "\"author\":\"Marion Zimmer Bradley\"," +
            "\"title\":\"The Mists of Avalon\"," +
            "\"recommendedReadingAge\":15" +
            "}"

        private fun String.withId(id: Int) = "{\"id\":$id," + substring(1)
        private fun List<Book>.clone() = map { it.copy() }
    }

    private val bookStore = BookStore().apply {
        all.add(Book(id = 1, author = "J.R.R. Tolkien", title = "The Hobbit", recommendedReadingAge = 5))
        all.add(Book(id = 2, author = "J.R.R. Tolkien", title = "The Lord Of The Rings", recommendedReadingAge = 14))
        all.add(Book(id = 3, author = "Steven King", title = "It", recommendedReadingAge = 16))
    }
    private val mvc = MockMvcBuilders.standaloneSetup(BookBoundary(bookStore)).build()

    private fun givenEmptyBookStore(): MutableList<Book> {
        val books = bookStore.all
        bookStore.all = mutableListOf()
        return books
    }

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
            .andExpect(content().json(THE_HOBBIT))
    }

    @Test fun shouldGetAllBooks() {
        mvc.perform(get("/books"))

            .andExpect(status().isOk)
            .andExpect(content().json(BOOKS_JSON))
    }

    @Test fun shouldPostNewBook() {
        mvc.perform(post("/books").content(THE_MISTS_OF_AVALON).contentType(APPLICATION_JSON_UTF8))

            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "/books/4"))
            .andExpect(content().json(THE_MISTS_OF_AVALON.withId(4)))
        assertThat(bookStore.all.last()).isEqualTo(Book(
            id = 4,
            author = "Marion Zimmer Bradley",
            title = "The Mists of Avalon",
            recommendedReadingAge = 15
        ))
    }

    @Test fun shouldPostExistingBook() {
        val json = THE_HOBBIT.replace("5", "4")
        mvc.perform(post("/books").content(json).contentType(APPLICATION_JSON_UTF8))

            .andExpect(status().isOk)
        assertThat(bookStore.all[0]).isEqualTo(Book(
            id = 1,
            author = "J.R.R. Tolkien",
            title = "The Hobbit",
            recommendedReadingAge = 4
        ))
    }

    @Test fun shouldFailToPostBookWithUnknownId() {
        val backup = bookStore.all.clone()
        val json = THE_MISTS_OF_AVALON.withId(4)

        mvc.perform(post("/books").content(json).contentType(APPLICATION_JSON_UTF8))

            .andExpect(status().isNotFound)
        assertThat(bookStore.all).containsExactlyElementsOf(backup)
    }

    @Test fun shouldPostNewBookWithPath() {
        mvc.perform(post("/books/4").content(THE_MISTS_OF_AVALON).contentType(APPLICATION_JSON_UTF8))

            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "/books/4"))
            .andExpect(content().json(THE_MISTS_OF_AVALON.withId(4)))
        assertThat(bookStore.all.last()).isEqualTo(Book(
            id = 4,
            author = "Marion Zimmer Bradley",
            title = "The Mists of Avalon",
            recommendedReadingAge = 15
        ))
    }

    @Test fun shouldPostExistingBookWithPath() {
        val json = THE_LORD_OF_THE_RINGS.replace("14", "13")
        mvc.perform(post("/books/2").content(json).contentType(APPLICATION_JSON_UTF8))

            .andExpect(status().isOk)
        assertThat(bookStore.all[1]).isEqualTo(Book(
            id = 2,
            author = "J.R.R. Tolkien",
            title = "The Lord Of The Rings",
            recommendedReadingAge = 13
        ))
    }

    @Test fun shouldFailToPostBookWithUnknownIdWithPath() {
        val backup = bookStore.all.clone()
        val json = THE_MISTS_OF_AVALON.withId(4)

        mvc.perform(post("/books/4").content(json).contentType(APPLICATION_JSON_UTF8))

            .andExpect(status().isNotFound)
        assertThat(bookStore.all).containsExactlyElementsOf(backup)
    }

    @Test fun shouldFailToPostBookWithPathMismatch() {
        val backup = bookStore.all.clone()
        val json = IT

        mvc.perform(post("/books/2").content(json).contentType(APPLICATION_JSON_UTF8))

            .andExpect(status().isBadRequest)
        assertThat(bookStore.all).containsExactlyElementsOf(backup)
    }

    @Test fun shouldPutAllBooks() {
        val backup = givenEmptyBookStore()

        mvc.perform(put("/books").content(BOOKS_JSON).contentType(APPLICATION_JSON_UTF8))

            .andExpect(status().isOk)
        assertThat(bookStore.all).containsExactlyElementsOf(backup)
    }
}

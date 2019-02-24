package com.example.reactcypressexperiment

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(BAD_REQUEST)
class BookIdMismatchException(pathId: Int, book: Book) : RuntimeException("path id '$pathId' doesn't match book id '${book.id}'")

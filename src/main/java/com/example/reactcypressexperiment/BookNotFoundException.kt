package com.example.reactcypressexperiment

import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(NOT_FOUND)
class BookNotFoundException(id: Int) : RuntimeException("no book found with id $id")

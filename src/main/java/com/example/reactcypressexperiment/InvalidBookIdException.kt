package com.example.reactcypressexperiment

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(BAD_REQUEST)
class InvalidBookIdException(id: Int) : RuntimeException("invalid book id: $id")

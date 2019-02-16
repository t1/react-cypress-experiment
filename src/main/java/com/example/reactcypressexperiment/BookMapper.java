package com.example.reactcypressexperiment;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookListItem toBookListItem(Book book);
}

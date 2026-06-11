package com.biblioteca.digital.application.mapper;

import com.biblioteca.digital.application.dto.BookDto;
import com.biblioteca.digital.domain.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookMapper {

    private final AuthorMapper authorMapper;

    public BookDto toDto(Book book) {
        if (book == null) return null;
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getPublishYear(),
                book.getSynopsis(),
                book.getGenre(),
                book.getAvailable(),
                authorMapper.toDto(book.getAuthor())
        );
    }
}

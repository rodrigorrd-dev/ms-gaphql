package com.biblioteca.digital.api.graphql;

import com.biblioteca.digital.application.dto.BookDto;
import com.biblioteca.digital.application.dto.input.CreateBookInput;
import com.biblioteca.digital.application.dto.input.UpdateBookInput;
import com.biblioteca.digital.application.usecase.BookUseCase;
import com.biblioteca.digital.domain.model.vo.Genre;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookGraphQLController {

    private final BookUseCase bookUseCase;

    @QueryMapping
    public List<BookDto> books() {
        return bookUseCase.findAll();
    }

    @QueryMapping
    public BookDto bookById(@Argument Long id) {
        return bookUseCase.findById(id);
    }

    @QueryMapping
    public BookDto bookByIsbn(@Argument String isbn) {
        return bookUseCase.findByIsbn(isbn);
    }

    @QueryMapping
    public List<BookDto> booksByGenre(@Argument Genre genre) {
        return bookUseCase.findByGenre(genre);
    }

    @QueryMapping
    public List<BookDto> availableBooks() {
        return bookUseCase.findAvailable();
    }

    @QueryMapping
    public List<BookDto> booksByAuthor(@Argument Long authorId) {
        return bookUseCase.findByAuthorId(authorId);
    }

    @MutationMapping
    public BookDto createBook(@Argument @Valid CreateBookInput input) {
        return bookUseCase.create(input);
    }

    @MutationMapping
    public BookDto updateBook(@Argument Long id, @Argument @Valid UpdateBookInput input) {
        return bookUseCase.update(id, input);
    }

    @MutationMapping
    public Boolean deleteBook(@Argument Long id) {
        return bookUseCase.delete(id);
    }
}

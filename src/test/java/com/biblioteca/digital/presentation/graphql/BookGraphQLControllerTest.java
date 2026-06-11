package com.biblioteca.digital.presentation.graphql;

import com.biblioteca.digital.api.graphql.BookGraphQLController;
import com.biblioteca.digital.application.dto.AuthorDto;
import com.biblioteca.digital.application.dto.BookDto;
import com.biblioteca.digital.application.usecase.BookUseCase;
import com.biblioteca.digital.domain.model.vo.Genre;
import com.biblioteca.digital.infrastructure.graphql.DateScalarConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.*;

@GraphQlTest(BookGraphQLController.class)
@Import(DateScalarConfiguration.class)
@DisplayName("BookGraphQLController — integration tests")
class BookGraphQLControllerTest {

    @Autowired GraphQlTester graphQlTester;
    @MockitoBean BookUseCase bookUseCase;

    private final AuthorDto authorDto = new AuthorDto(
            1L, "Clarice Lispector", "Brasileira",
            LocalDate.of(1920, 12, 10), "Escritora", "clarice@test.com");

    private final BookDto bookDto = new BookDto(
            1L, "A Hora da Estrela", "9788532630407",
            1977, "Sinopse", Genre.ROMANCE, true, authorDto);

    @Test
    @DisplayName("query books retorna lista de livros")
    void booksQuery_returnsBooks() {
        given(bookUseCase.findAll()).willReturn(List.of(bookDto));

        graphQlTester.document("""
                query {
                  books {
                    id
                    title
                    isbn
                    genre
                    available
                  }
                }
                """)
                .execute()
                .path("books[0].title").entity(String.class).isEqualTo("A Hora da Estrela")
                .path("books[0].isbn").entity(String.class).isEqualTo("9788532630407")
                .path("books[0].available").entity(Boolean.class).isEqualTo(true);
    }

    @Test
    @DisplayName("query bookById retorna livro correto")
    void bookByIdQuery_returnsBook() {
        given(bookUseCase.findById(1L)).willReturn(bookDto);

        graphQlTester.document("""
                query {
                  bookById(id: "1") {
                    id
                    title
                    author {
                      name
                    }
                  }
                }
                """)
                .execute()
                .path("bookById.title").entity(String.class).isEqualTo("A Hora da Estrela")
                .path("bookById.author.name").entity(String.class).isEqualTo("Clarice Lispector");
    }

    @Test
    @DisplayName("query availableBooks retorna somente livros disponíveis")
    void availableBooksQuery_returnsOnlyAvailable() {
        given(bookUseCase.findAvailable()).willReturn(List.of(bookDto));

        graphQlTester.document("""
                query {
                  availableBooks {
                    id
                    title
                    available
                  }
                }
                """)
                .execute()
                .path("availableBooks").entityList(Object.class).hasSize(1)
                .path("availableBooks[0].available").entity(Boolean.class).isEqualTo(true);
    }
}

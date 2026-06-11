package com.biblioteca.digital.application.usecase;

import com.biblioteca.digital.application.dto.BookDto;
import com.biblioteca.digital.application.dto.input.CreateBookInput;
import com.biblioteca.digital.application.mapper.AuthorMapper;
import com.biblioteca.digital.application.mapper.BookMapper;
import com.biblioteca.digital.domain.exception.BookNotFoundException;
import com.biblioteca.digital.domain.exception.DomainException;
import com.biblioteca.digital.domain.model.Author;
import com.biblioteca.digital.domain.model.Book;
import com.biblioteca.digital.domain.model.vo.Genre;
import com.biblioteca.digital.domain.repository.AuthorRepository;
import com.biblioteca.digital.domain.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookUseCase")
class BookUseCaseTest {

    @Mock BookRepository bookRepository;
    @Mock AuthorRepository authorRepository;
    @Mock BookMapper bookMapper;

    @InjectMocks BookUseCase bookUseCase;

    private Author author;
    private Book book;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        author = Author.create("Clarice Lispector", "Brasileira",
                LocalDate.of(1920, 12, 10), "Escritora brasileira", "clarice@test.com");

        book = Book.create("A Hora da Estrela", "9788532630407", 1977,
                "Romance sobre Macabéa", Genre.ROMANCE, author);

        bookDto = new BookDto(1L, "A Hora da Estrela", "9788532630407",
                1977, "Romance sobre Macabéa", Genre.ROMANCE, true,
                new com.biblioteca.digital.application.dto.AuthorDto(
                        1L, "Clarice Lispector", "Brasileira",
                        LocalDate.of(1920, 12, 10), "Escritora brasileira", "clarice@test.com"));
    }

    @Test
    @DisplayName("findAll retorna lista de livros mapeados")
    void findAll_returnsAllBooks() {
        given(bookRepository.findAll()).willReturn(List.of(book));
        given(bookMapper.toDto(book)).willReturn(bookDto);

        List<BookDto> result = bookUseCase.findAll();

        assertThat(result).hasSize(1).first().isEqualTo(bookDto);
        then(bookRepository).should().findAll();
    }

    @Test
    @DisplayName("findById lança exceção quando livro não existe")
    void findById_throwsWhenNotFound() {
        given(bookRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> bookUseCase.findById(99L))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("create lança exceção para ISBN duplicado")
    void create_throwsForDuplicateIsbn() {
        CreateBookInput input = new CreateBookInput(
                "Novo Livro", "9788532630407", 2024, "Sinopse", Genre.FANTASY, 1L);

        given(bookRepository.existsByIsbn("9788532630407")).willReturn(true);

        assertThatThrownBy(() -> bookUseCase.create(input))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("ISBN");
    }

    @Test
    @DisplayName("create persiste livro quando dados são válidos")
    void create_persistsBookWithValidData() {
        CreateBookInput input = new CreateBookInput(
                "A Hora da Estrela", "9788532630407", 1977, "Sinopse", Genre.ROMANCE, 1L);

        given(bookRepository.existsByIsbn(any())).willReturn(false);
        given(authorRepository.findById(1L)).willReturn(Optional.of(author));
        given(bookRepository.save(any())).willReturn(book);
        given(bookMapper.toDto(book)).willReturn(bookDto);

        BookDto result = bookUseCase.create(input);

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("A Hora da Estrela");
        then(bookRepository).should().save(any(Book.class));
    }

    @Test
    @DisplayName("delete retorna true quando livro existe")
    void delete_returnsTrueWhenBookExists() {
        given(bookRepository.findById(1L)).willReturn(Optional.of(book));
        willDoNothing().given(bookRepository).deleteById(1L);

        boolean result = bookUseCase.delete(1L);

        assertThat(result).isTrue();
        then(bookRepository).should().deleteById(1L);
    }
}

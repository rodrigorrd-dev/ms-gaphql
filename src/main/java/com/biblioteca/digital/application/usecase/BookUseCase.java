package com.biblioteca.digital.application.usecase;

import com.biblioteca.digital.application.dto.BookDto;
import com.biblioteca.digital.application.dto.input.CreateBookInput;
import com.biblioteca.digital.application.dto.input.UpdateBookInput;
import com.biblioteca.digital.application.mapper.BookMapper;
import com.biblioteca.digital.domain.exception.AuthorNotFoundException;
import com.biblioteca.digital.domain.exception.BookNotFoundException;
import com.biblioteca.digital.domain.exception.DomainException;
import com.biblioteca.digital.domain.model.Author;
import com.biblioteca.digital.domain.model.Book;
import com.biblioteca.digital.domain.model.vo.Genre;
import com.biblioteca.digital.domain.repository.AuthorRepository;
import com.biblioteca.digital.domain.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookUseCase {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    public BookDto findById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    public BookDto findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new BookNotFoundException(isbn));
    }

    public List<BookDto> findByGenre(Genre genre) {
        return bookRepository.findByGenre(genre).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    public List<BookDto> findAvailable() {
        return bookRepository.findAvailable().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    public List<BookDto> findByAuthorId(Long authorId) {
        return bookRepository.findByAuthorId(authorId).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Transactional
    public BookDto create(CreateBookInput input) {
        if (bookRepository.existsByIsbn(input.isbn())) {
            throw new DomainException("Já existe um livro com o ISBN: " + input.isbn());
        }
        Author author = authorRepository.findById(input.authorId())
                .orElseThrow(() -> new AuthorNotFoundException(input.authorId()));

        Book book = Book.create(
                input.title(),
                input.isbn(),
                input.publishYear(),
                input.synopsis(),
                input.genre(),
                author
        );
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Transactional
    public BookDto update(Long id, UpdateBookInput input) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        Author author = null;
        if (input.authorId() != null) {
            author = authorRepository.findById(input.authorId())
                    .orElseThrow(() -> new AuthorNotFoundException(input.authorId()));
        }

        book.update(input.title(), input.isbn(), input.publishYear(),
                input.synopsis(), input.genre(), author);

        return bookMapper.toDto(bookRepository.save(book));
    }

    @Transactional
    public boolean delete(Long id) {
        bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        bookRepository.deleteById(id);
        return true;
    }
}

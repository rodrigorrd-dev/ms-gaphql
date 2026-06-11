package com.biblioteca.digital.infrastructure.persistence;

import com.biblioteca.digital.domain.model.Book;
import com.biblioteca.digital.domain.model.vo.Genre;
import com.biblioteca.digital.domain.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRepositoryAdapter implements BookRepository {

    private final JpaBookRepository jpa;

    @Override
    public Book save(Book book) { return jpa.save(book); }

    @Override
    public Optional<Book> findById(Long id) { return jpa.findById(id); }

    @Override
    public Optional<Book> findByIsbn(String isbn) { return jpa.findByIsbn(isbn); }

    @Override
    public List<Book> findAll() { return jpa.findAll(); }

    @Override
    public List<Book> findByGenre(Genre genre) { return jpa.findByGenre(genre); }

    @Override
    public List<Book> findByAuthorId(Long authorId) { return jpa.findByAuthorId(authorId); }

    @Override
    public List<Book> findAvailable() { return jpa.findAvailable(); }

    @Override
    public boolean existsByIsbn(String isbn) { return jpa.existsByIsbn(isbn); }

    @Override
    public void deleteById(Long id) { jpa.deleteById(id); }
}

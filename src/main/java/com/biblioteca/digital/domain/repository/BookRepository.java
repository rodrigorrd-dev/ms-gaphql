package com.biblioteca.digital.domain.repository;

import com.biblioteca.digital.domain.model.Book;
import com.biblioteca.digital.domain.model.vo.Genre;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    Book save(Book book);

    Optional<Book> findById(Long id);

    Optional<Book> findByIsbn(String isbn);

    List<Book> findAll();

    List<Book> findByGenre(Genre genre);

    List<Book> findByAuthorId(Long authorId);

    List<Book> findAvailable();

    boolean existsByIsbn(String isbn);

    void deleteById(Long id);
}

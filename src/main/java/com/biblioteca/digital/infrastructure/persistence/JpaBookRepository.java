package com.biblioteca.digital.infrastructure.persistence;

import com.biblioteca.digital.domain.model.Book;
import com.biblioteca.digital.domain.model.vo.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

interface JpaBookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByGenre(Genre genre);

    @Query("SELECT b FROM Book b WHERE b.author.id = :authorId")
    List<Book> findByAuthorId(Long authorId);

    @Query("SELECT b FROM Book b WHERE b.available = true")
    List<Book> findAvailable();

    boolean existsByIsbn(String isbn);
}

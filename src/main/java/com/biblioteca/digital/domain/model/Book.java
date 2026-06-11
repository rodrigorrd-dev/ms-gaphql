package com.biblioteca.digital.domain.model;

import com.biblioteca.digital.domain.exception.BookNotAvailableException;
import com.biblioteca.digital.domain.exception.DomainException;
import com.biblioteca.digital.domain.model.vo.Genre;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"author", "loans"})
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(unique = true, nullable = false, length = 13)
    private String isbn;

    @Column(nullable = false)
    private Integer publishYear;

    @Column(length = 2000)
    private String synopsis;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genre genre;

    @Column(nullable = false)
    private Boolean available = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Loan> loans = new ArrayList<>();

    private Book(String title, String isbn, Integer publishYear, String synopsis, Genre genre, Author author) {
        this.title = title;
        this.isbn = isbn;
        this.publishYear = publishYear;
        this.synopsis = synopsis;
        this.genre = genre;
        this.author = author;
        this.available = true;
    }

    public static Book create(String title, String isbn, Integer publishYear,
                               String synopsis, Genre genre, Author author) {
        validateTitle(title);
        validateIsbn(isbn);
        if (author == null) throw new DomainException("Autor é obrigatório");
        return new Book(title, isbn, publishYear, synopsis, genre, author);
    }

    public void update(String title, String isbn, Integer publishYear,
                       String synopsis, Genre genre, Author author) {
        validateTitle(title);
        validateIsbn(isbn);
        this.title = title;
        this.isbn = isbn;
        if (publishYear != null) this.publishYear = publishYear;
        this.synopsis = synopsis;
        if (genre != null) this.genre = genre;
        if (author != null) this.author = author;
    }

    public void markAsUnavailable() {
        if (!this.available) throw new BookNotAvailableException(this.id);
        this.available = false;
    }

    public void markAsAvailable() {
        this.available = true;
    }

    public List<Loan> getLoans() {
        return Collections.unmodifiableList(loans);
    }

    private static void validateTitle(String title) {
        if (title == null || title.isBlank()) throw new DomainException("Título do livro é obrigatório");
    }

    private static void validateIsbn(String isbn) {
        if (isbn == null || isbn.isBlank()) throw new DomainException("ISBN é obrigatório");
        String normalized = isbn.replaceAll("[\\s-]", "");
        if (normalized.length() != 13) throw new DomainException("ISBN deve conter 13 dígitos");
    }
}

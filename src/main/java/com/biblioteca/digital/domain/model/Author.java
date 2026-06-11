package com.biblioteca.digital.domain.model;

import com.biblioteca.digital.domain.exception.DomainException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "authors")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@ToString(exclude = "books")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nationality;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(length = 2000)
    private String biography;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books = new ArrayList<>();

    private Author(String name, String nationality, LocalDate birthDate, String biography, String email) {
        this.name = name;
        this.nationality = nationality;
        this.birthDate = birthDate;
        this.biography = biography;
        this.email = email;
    }

    public static Author create(String name, String nationality, LocalDate birthDate, String biography, String email) {
        validateName(name);
        validateNationality(nationality);
        if (birthDate == null) throw new DomainException("Data de nascimento é obrigatória");
        return new Author(name, nationality, birthDate, biography, email);
    }

    public void update(String name, String nationality, LocalDate birthDate, String biography, String email) {
        validateName(name);
        validateNationality(nationality);
        this.name = name;
        this.nationality = nationality;
        if (birthDate != null) this.birthDate = birthDate;
        this.biography = biography;
        this.email = email;
    }

    public List<Book> getBooks() {
        return Collections.unmodifiableList(books);
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) throw new DomainException("Nome do autor é obrigatório");
    }

    private static void validateNationality(String nationality) {
        if (nationality == null || nationality.isBlank()) throw new DomainException("Nacionalidade é obrigatória");
    }
}

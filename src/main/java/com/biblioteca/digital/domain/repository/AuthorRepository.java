package com.biblioteca.digital.domain.repository;

import com.biblioteca.digital.domain.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {

    Author save(Author author);

    Optional<Author> findById(Long id);

    Optional<Author> findByEmail(String email);

    List<Author> findAll();

    List<Author> findByNationality(String nationality);

    boolean existsByEmail(String email);

    void deleteById(Long id);
}

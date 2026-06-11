package com.biblioteca.digital.infrastructure.persistence;

import com.biblioteca.digital.domain.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface JpaAuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByEmail(String email);

    List<Author> findByNationality(String nationality);

    boolean existsByEmail(String email);
}

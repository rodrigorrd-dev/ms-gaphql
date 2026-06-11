package com.biblioteca.digital.infrastructure.persistence;

import com.biblioteca.digital.domain.model.Author;
import com.biblioteca.digital.domain.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorRepositoryAdapter implements AuthorRepository {

    private final JpaAuthorRepository jpa;

    @Override
    public Author save(Author author) { return jpa.save(author); }

    @Override
    public Optional<Author> findById(Long id) { return jpa.findById(id); }

    @Override
    public Optional<Author> findByEmail(String email) { return jpa.findByEmail(email); }

    @Override
    public List<Author> findAll() { return jpa.findAll(); }

    @Override
    public List<Author> findByNationality(String nationality) { return jpa.findByNationality(nationality); }

    @Override
    public boolean existsByEmail(String email) { return jpa.existsByEmail(email); }

    @Override
    public void deleteById(Long id) { jpa.deleteById(id); }
}

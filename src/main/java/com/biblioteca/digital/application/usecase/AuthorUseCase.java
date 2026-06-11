package com.biblioteca.digital.application.usecase;

import com.biblioteca.digital.application.dto.AuthorDto;
import com.biblioteca.digital.application.dto.input.CreateAuthorInput;
import com.biblioteca.digital.application.dto.input.UpdateAuthorInput;
import com.biblioteca.digital.application.mapper.AuthorMapper;
import com.biblioteca.digital.domain.exception.AuthorNotFoundException;
import com.biblioteca.digital.domain.exception.DomainException;
import com.biblioteca.digital.domain.model.Author;
import com.biblioteca.digital.domain.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorUseCase {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream()
                .map(authorMapper::toDto)
                .toList();
    }

    public AuthorDto findById(Long id) {
        return authorRepository.findById(id)
                .map(authorMapper::toDto)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    public List<AuthorDto> findByNationality(String nationality) {
        return authorRepository.findByNationality(nationality).stream()
                .map(authorMapper::toDto)
                .toList();
    }

    @Transactional
    public AuthorDto create(CreateAuthorInput input) {
        if (input.email() != null && authorRepository.existsByEmail(input.email())) {
            throw new DomainException("Já existe um autor com o email: " + input.email());
        }
        Author author = Author.create(
                input.name(),
                input.nationality(),
                input.birthDate(),
                input.biography(),
                input.email()
        );
        return authorMapper.toDto(authorRepository.save(author));
    }

    @Transactional
    public AuthorDto update(Long id, UpdateAuthorInput input) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));

        author.update(
                input.name(),
                input.nationality(),
                input.birthDate(),
                input.biography(),
                input.email()
        );
        return authorMapper.toDto(authorRepository.save(author));
    }

    @Transactional
    public boolean delete(Long id) {
        authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
        authorRepository.deleteById(id);
        return true;
    }
}

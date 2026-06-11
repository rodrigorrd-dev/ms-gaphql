package com.biblioteca.digital.application.mapper;

import com.biblioteca.digital.application.dto.AuthorDto;
import com.biblioteca.digital.domain.model.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    public AuthorDto toDto(Author author) {
        if (author == null) return null;
        return new AuthorDto(
                author.getId(),
                author.getName(),
                author.getNationality(),
                author.getBirthDate(),
                author.getBiography(),
                author.getEmail()
        );
    }
}

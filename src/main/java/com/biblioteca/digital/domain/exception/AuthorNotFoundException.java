package com.biblioteca.digital.domain.exception;

public class AuthorNotFoundException extends DomainException {

    public AuthorNotFoundException(Long id) {
        super("Autor não encontrado com ID: " + id);
    }
}

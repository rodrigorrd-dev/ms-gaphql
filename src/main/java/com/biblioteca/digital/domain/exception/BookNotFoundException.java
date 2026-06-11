package com.biblioteca.digital.domain.exception;

public class BookNotFoundException extends DomainException {

    public BookNotFoundException(Long id) {
        super("Livro não encontrado com ID: " + id);
    }

    public BookNotFoundException(String isbn) {
        super("Livro não encontrado com ISBN: " + isbn);
    }
}

package com.biblioteca.digital.domain.exception;

public class BookNotAvailableException extends DomainException {

    public BookNotAvailableException(Long bookId) {
        super("Livro com ID " + bookId + " não está disponível para empréstimo");
    }
}

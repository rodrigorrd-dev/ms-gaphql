package com.biblioteca.digital.domain.exception;

public class LoanNotFoundException extends DomainException {

    public LoanNotFoundException(Long id) {
        super("Empréstimo não encontrado com ID: " + id);
    }
}

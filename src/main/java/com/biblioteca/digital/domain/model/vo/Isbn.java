package com.biblioteca.digital.domain.model.vo;

import java.util.regex.Pattern;

/**
 * Value Object que garante a validade de um ISBN-13.
 * Imutável por design — qualquer instância de Isbn representa um código validado.
 */
public record Isbn(String value) {

    private static final Pattern ISBN_13_PATTERN = Pattern.compile("^(978|979)-\\d{1,5}-\\d{1,7}-\\d{1,7}-\\d$");

    public Isbn {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ISBN não pode ser nulo ou vazio");
        }
        String normalized = value.replaceAll("[\\s-]", "");
        if (normalized.length() != 13 || !normalized.chars().allMatch(Character::isDigit)) {
            throw new IllegalArgumentException("ISBN inválido: " + value);
        }
    }

    public String normalized() {
        return value.replaceAll("[\\s-]", "");
    }

    @Override
    public String toString() {
        return value;
    }
}

package com.biblioteca.digital.application.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record UpdateAuthorInput(
        @NotBlank(message = "Nome é obrigatório") String name,
        @NotBlank(message = "Nacionalidade é obrigatória") String nationality,
        LocalDate birthDate,
        String biography,
        @Email(message = "Email inválido") String email
) {}

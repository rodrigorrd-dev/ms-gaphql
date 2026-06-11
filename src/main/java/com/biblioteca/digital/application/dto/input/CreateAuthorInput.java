package com.biblioteca.digital.application.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateAuthorInput(
        @NotBlank(message = "Nome é obrigatório") String name,
        @NotBlank(message = "Nacionalidade é obrigatória") String nationality,
        @NotNull(message = "Data de nascimento é obrigatória") LocalDate birthDate,
        String biography,
        @Email(message = "Email inválido") String email
) {}

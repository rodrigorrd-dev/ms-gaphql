package com.biblioteca.digital.application.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateLoanInput(
        @NotNull(message = "ID do livro é obrigatório") Long bookId,
        @NotBlank(message = "Nome do membro é obrigatório") String memberName,
        @Email(message = "Email inválido") @NotBlank(message = "Email é obrigatório") String memberEmail,
        @NotNull(message = "Data prevista de devolução é obrigatória") LocalDate expectedReturnDate
) {}

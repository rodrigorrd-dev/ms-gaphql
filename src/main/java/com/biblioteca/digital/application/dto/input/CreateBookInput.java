package com.biblioteca.digital.application.dto.input;

import com.biblioteca.digital.domain.model.vo.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateBookInput(
        @NotBlank(message = "Título é obrigatório") String title,
        @NotBlank(message = "ISBN é obrigatório") String isbn,
        @NotNull(message = "Ano de publicação é obrigatório") @Positive Integer publishYear,
        String synopsis,
        @NotNull(message = "Gênero é obrigatório") Genre genre,
        @NotNull(message = "ID do autor é obrigatório") Long authorId
) {}

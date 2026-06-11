package com.biblioteca.digital.application.dto.input;

import com.biblioteca.digital.domain.model.vo.Genre;
import jakarta.validation.constraints.NotBlank;

public record UpdateBookInput(
        @NotBlank(message = "Título é obrigatório") String title,
        @NotBlank(message = "ISBN é obrigatório") String isbn,
        Integer publishYear,
        String synopsis,
        Genre genre,
        Long authorId
) {}

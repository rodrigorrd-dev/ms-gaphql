package com.biblioteca.digital.application.dto;

import com.biblioteca.digital.domain.model.vo.Genre;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "book")
public record BookDto(
        @JacksonXmlProperty(isAttribute = true) Long id,
        String title,
        String isbn,
        Integer publishYear,
        String synopsis,
        Genre genre,
        Boolean available,
        AuthorDto author
) {}

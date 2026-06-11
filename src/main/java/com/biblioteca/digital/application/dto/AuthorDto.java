package com.biblioteca.digital.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.time.LocalDate;

@JacksonXmlRootElement(localName = "author")
public record AuthorDto(
        @JacksonXmlProperty(isAttribute = true) Long id,
        String name,
        String nationality,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate birthDate,
        String biography,
        String email
) {}

package com.biblioteca.digital.application.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "library")
public record LibraryExportDto(
        @JacksonXmlElementWrapper(localName = "authors")
        @JacksonXmlProperty(localName = "author")
        List<AuthorDto> authors,

        @JacksonXmlElementWrapper(localName = "books")
        @JacksonXmlProperty(localName = "book")
        List<BookDto> books,

        @JacksonXmlElementWrapper(localName = "loans")
        @JacksonXmlProperty(localName = "loan")
        List<LoanDto> loans
) {}

package com.biblioteca.digital.application.dto;

import com.biblioteca.digital.domain.model.vo.LoanStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.time.LocalDate;

@JacksonXmlRootElement(localName = "loan")
public record LoanDto(
        @JacksonXmlProperty(isAttribute = true) Long id,
        Long bookId,
        String bookTitle,
        String memberName,
        String memberEmail,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate loanDate,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate expectedReturnDate,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate actualReturnDate,
        LoanStatus status
) {}

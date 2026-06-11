package com.biblioteca.digital.application.mapper;

import com.biblioteca.digital.application.dto.LoanDto;
import com.biblioteca.digital.domain.model.Loan;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {

    public LoanDto toDto(Loan loan) {
        if (loan == null) return null;
        return new LoanDto(
                loan.getId(),
                loan.getBook().getId(),
                loan.getBook().getTitle(),
                loan.getMemberName(),
                loan.getMemberEmail(),
                loan.getLoanDate(),
                loan.getExpectedReturnDate(),
                loan.getActualReturnDate(),
                loan.getStatus()
        );
    }
}

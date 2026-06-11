package com.biblioteca.digital.api.graphql;

import com.biblioteca.digital.application.dto.LoanDto;
import com.biblioteca.digital.application.dto.input.CreateLoanInput;
import com.biblioteca.digital.application.usecase.LoanUseCase;
import com.biblioteca.digital.domain.model.vo.LoanStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LoanGraphQLController {

    private final LoanUseCase loanUseCase;

    @QueryMapping
    public List<LoanDto> loans() {
        return loanUseCase.findAll();
    }

    @QueryMapping
    public LoanDto loanById(@Argument Long id) {
        return loanUseCase.findById(id);
    }

    @QueryMapping
    public List<LoanDto> loansByMember(@Argument String email) {
        return loanUseCase.findByMemberEmail(email);
    }

    @QueryMapping
    public List<LoanDto> loansByStatus(@Argument LoanStatus status) {
        return loanUseCase.findByStatus(status);
    }

    @QueryMapping
    public List<LoanDto> overdueLoans() {
        return loanUseCase.findOverdue();
    }

    @MutationMapping
    public LoanDto createLoan(@Argument @Valid CreateLoanInput input) {
        return loanUseCase.create(input);
    }

    @MutationMapping
    public LoanDto returnBook(@Argument Long loanId) {
        return loanUseCase.returnBook(loanId);
    }

    @MutationMapping
    public LoanDto cancelLoan(@Argument Long loanId) {
        return loanUseCase.cancelLoan(loanId);
    }

    @MutationMapping
    public Boolean deleteLoan(@Argument Long id) {
        return loanUseCase.delete(id);
    }
}

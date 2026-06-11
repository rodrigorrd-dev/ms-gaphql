package com.biblioteca.digital.domain.service;

import com.biblioteca.digital.domain.model.Loan;
import com.biblioteca.digital.domain.model.vo.LoanStatus;
import com.biblioteca.digital.domain.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Domain Service: lógica de negócio que não pertence a uma única entidade.
 * Responsável por regras que envolvem múltiplos agregados ou cálculos de domínio.
 */
@Service
@RequiredArgsConstructor
public class LoanDomainService {

    private final LoanRepository loanRepository;

    public void checkAndMarkOverdueLoans() {
        List<Loan> activeLoans = loanRepository.findByStatus(LoanStatus.ACTIVE);
        activeLoans.forEach(loan -> {
            loan.markAsOverdue();
            loanRepository.save(loan);
        });
    }

    public boolean memberHasActiveLoans(String memberEmail) {
        return !loanRepository.findByMemberEmail(memberEmail)
                .stream()
                .filter(l -> l.getStatus() == LoanStatus.ACTIVE || l.getStatus() == LoanStatus.OVERDUE)
                .toList()
                .isEmpty();
    }

    public List<Loan> findOverdueLoans() {
        return loanRepository.findOverdue(LocalDate.now());
    }
}

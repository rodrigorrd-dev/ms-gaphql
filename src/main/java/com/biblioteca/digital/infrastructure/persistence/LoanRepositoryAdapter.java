package com.biblioteca.digital.infrastructure.persistence;

import com.biblioteca.digital.domain.model.Loan;
import com.biblioteca.digital.domain.model.vo.LoanStatus;
import com.biblioteca.digital.domain.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LoanRepositoryAdapter implements LoanRepository {

    private final JpaLoanRepository jpa;

    @Override
    public Loan save(Loan loan) { return jpa.save(loan); }

    @Override
    public Optional<Loan> findById(Long id) { return jpa.findById(id); }

    @Override
    public List<Loan> findAll() { return jpa.findAll(); }

    @Override
    public List<Loan> findByMemberEmail(String memberEmail) { return jpa.findByMemberEmail(memberEmail); }

    @Override
    public List<Loan> findByStatus(LoanStatus status) { return jpa.findByStatus(status); }

    @Override
    public List<Loan> findByBookId(Long bookId) { return jpa.findByBookId(bookId); }

    @Override
    public List<Loan> findOverdue(LocalDate referenceDate) {
        return jpa.findByStatusAndExpectedReturnDateBefore(LoanStatus.ACTIVE, referenceDate);
    }

    @Override
    public void deleteById(Long id) { jpa.deleteById(id); }
}

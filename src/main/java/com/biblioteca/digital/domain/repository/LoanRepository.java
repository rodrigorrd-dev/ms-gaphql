package com.biblioteca.digital.domain.repository;

import com.biblioteca.digital.domain.model.Loan;
import com.biblioteca.digital.domain.model.vo.LoanStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LoanRepository {

    Loan save(Loan loan);

    Optional<Loan> findById(Long id);

    List<Loan> findAll();

    List<Loan> findByMemberEmail(String memberEmail);

    List<Loan> findByStatus(LoanStatus status);

    List<Loan> findByBookId(Long bookId);

    List<Loan> findOverdue(LocalDate referenceDate);

    void deleteById(Long id);
}

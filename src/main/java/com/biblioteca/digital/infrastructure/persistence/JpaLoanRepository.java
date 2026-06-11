package com.biblioteca.digital.infrastructure.persistence;

import com.biblioteca.digital.domain.model.Loan;
import com.biblioteca.digital.domain.model.vo.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

interface JpaLoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByMemberEmail(String memberEmail);

    List<Loan> findByStatus(LoanStatus status);

    @Query("SELECT l FROM Loan l WHERE l.book.id = :bookId")
    List<Loan> findByBookId(Long bookId);

    List<Loan> findByStatusAndExpectedReturnDateBefore(LoanStatus status, LocalDate referenceDate);
}

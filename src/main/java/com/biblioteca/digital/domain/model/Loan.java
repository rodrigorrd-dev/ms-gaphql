package com.biblioteca.digital.domain.model;

import com.biblioteca.digital.domain.exception.DomainException;
import com.biblioteca.digital.domain.model.vo.LoanStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "loans")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@ToString(exclude = "book")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private String memberName;

    @Column(nullable = false)
    private String memberEmail;

    @Column(nullable = false)
    private LocalDate loanDate;

    @Column(nullable = false)
    private LocalDate expectedReturnDate;

    private LocalDate actualReturnDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status;

    private Loan(Book book, String memberName, String memberEmail,
                 LocalDate loanDate, LocalDate expectedReturnDate) {
        this.book = book;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.loanDate = loanDate;
        this.expectedReturnDate = expectedReturnDate;
        this.status = LoanStatus.ACTIVE;
    }

    public static Loan create(Book book, String memberName, String memberEmail, LocalDate expectedReturnDate) {
        if (book == null) throw new DomainException("Livro é obrigatório para empréstimo");
        if (memberName == null || memberName.isBlank()) throw new DomainException("Nome do membro é obrigatório");
        if (memberEmail == null || memberEmail.isBlank()) throw new DomainException("Email do membro é obrigatório");
        if (expectedReturnDate == null) throw new DomainException("Data prevista de devolução é obrigatória");

        LocalDate today = LocalDate.now();
        if (expectedReturnDate.isBefore(today)) {
            throw new DomainException("Data prevista de devolução não pode ser no passado");
        }

        book.markAsUnavailable();
        return new Loan(book, memberName, memberEmail, today, expectedReturnDate);
    }

    public void returnBook() {
        if (this.status == LoanStatus.RETURNED) {
            throw new DomainException("Este empréstimo já foi devolvido");
        }
        this.actualReturnDate = LocalDate.now();
        this.status = LoanStatus.RETURNED;
        this.book.markAsAvailable();
    }

    public void markAsOverdue() {
        if (this.status == LoanStatus.ACTIVE && LocalDate.now().isAfter(this.expectedReturnDate)) {
            this.status = LoanStatus.OVERDUE;
        }
    }

    public void cancel() {
        if (this.status != LoanStatus.ACTIVE) {
            throw new DomainException("Somente empréstimos ativos podem ser cancelados");
        }
        this.status = LoanStatus.CANCELLED;
        this.book.markAsAvailable();
    }
}

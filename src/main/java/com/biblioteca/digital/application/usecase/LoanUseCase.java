package com.biblioteca.digital.application.usecase;

import com.biblioteca.digital.application.dto.LoanDto;
import com.biblioteca.digital.application.dto.input.CreateLoanInput;
import com.biblioteca.digital.application.mapper.LoanMapper;
import com.biblioteca.digital.domain.exception.BookNotFoundException;
import com.biblioteca.digital.domain.exception.LoanNotFoundException;
import com.biblioteca.digital.domain.model.Book;
import com.biblioteca.digital.domain.model.Loan;
import com.biblioteca.digital.domain.model.vo.LoanStatus;
import com.biblioteca.digital.domain.repository.BookRepository;
import com.biblioteca.digital.domain.repository.LoanRepository;
import com.biblioteca.digital.domain.service.LoanDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoanUseCase {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final LoanMapper loanMapper;
    private final LoanDomainService loanDomainService;

    public List<LoanDto> findAll() {
        return loanRepository.findAll().stream()
                .map(loanMapper::toDto)
                .toList();
    }

    public LoanDto findById(Long id) {
        return loanRepository.findById(id)
                .map(loanMapper::toDto)
                .orElseThrow(() -> new LoanNotFoundException(id));
    }

    public List<LoanDto> findByMemberEmail(String email) {
        return loanRepository.findByMemberEmail(email).stream()
                .map(loanMapper::toDto)
                .toList();
    }

    public List<LoanDto> findByStatus(LoanStatus status) {
        return loanRepository.findByStatus(status).stream()
                .map(loanMapper::toDto)
                .toList();
    }

    public List<LoanDto> findOverdue() {
        loanDomainService.checkAndMarkOverdueLoans();
        return loanDomainService.findOverdueLoans().stream()
                .map(loanMapper::toDto)
                .toList();
    }

    @Transactional
    public LoanDto create(CreateLoanInput input) {
        Book book = bookRepository.findById(input.bookId())
                .orElseThrow(() -> new BookNotFoundException(input.bookId()));

        Loan loan = Loan.create(book, input.memberName(), input.memberEmail(), input.expectedReturnDate());
        bookRepository.save(book);
        return loanMapper.toDto(loanRepository.save(loan));
    }

    @Transactional
    public LoanDto returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));
        loan.returnBook();
        bookRepository.save(loan.getBook());
        return loanMapper.toDto(loanRepository.save(loan));
    }

    @Transactional
    public LoanDto cancelLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));
        loan.cancel();
        bookRepository.save(loan.getBook());
        return loanMapper.toDto(loanRepository.save(loan));
    }

    @Transactional
    public boolean delete(Long id) {
        loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException(id));
        loanRepository.deleteById(id);
        return true;
    }
}

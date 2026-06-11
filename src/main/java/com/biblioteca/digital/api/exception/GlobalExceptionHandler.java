package com.biblioteca.digital.api.exception;

import com.biblioteca.digital.domain.exception.BookNotAvailableException;
import com.biblioteca.digital.domain.exception.DomainException;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @ExceptionHandler(DomainException.class)
    public ProblemDetail handleDomainException(DomainException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        problem.setTitle("Erro de Domínio");
        problem.setType(URI.create("https://biblioteca-digital.local/errors/domain"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(BookNotAvailableException.class)
    public ProblemDetail handleBookNotAvailable(BookNotAvailableException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Livro Indisponível");
        problem.setType(URI.create("https://biblioteca-digital.local/errors/book-unavailable"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @Override
    protected GraphQLError resolveToSingleError(@NonNull Throwable ex, @NonNull DataFetchingEnvironment env) {
        if (ex instanceof DomainException domainEx) {
            return GraphQLError.newError()
                    .errorType(ex instanceof BookNotAvailableException
                            ? ErrorType.BAD_REQUEST
                            : ErrorType.NOT_FOUND)
                    .message(domainEx.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        }
        return null;
    }
}

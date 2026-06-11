package com.biblioteca.digital.infrastructure.client;

public class GraphQLClientException extends RuntimeException {

    public GraphQLClientException(String message, Throwable cause) {
        super(message, cause);
    }
}

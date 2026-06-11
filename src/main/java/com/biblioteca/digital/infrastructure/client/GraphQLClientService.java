package com.biblioteca.digital.infrastructure.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 * Cliente GraphQL HTTP — simula o papel de uma aplicação cliente externa
 * consumindo o servidor GraphQL desta mesma aplicação.
 * Demonstra o padrão de integração descrito na Prática 7 (Parte 2).
 */
@Service
@Slf4j
public class GraphQLClientService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public GraphQLClientService(
            @Value("${app.graphql.endpoint:http://localhost:8080/graphql}") String endpoint,
            ObjectMapper objectMapper) {
        this.restClient = RestClient.builder()
                .baseUrl(endpoint)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.objectMapper = objectMapper;
    }

    public JsonNode executeQuery(String query) {
        return executeQuery(query, null);
    }

    public JsonNode executeQuery(String query, Map<String, Object> variables) {
        Map<String, Object> body = variables != null
                ? Map.of("query", query, "variables", variables)
                : Map.of("query", query);

        log.debug("Executando query GraphQL: {}", query.substring(0, Math.min(query.length(), 80)));

        String response = restClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(String.class);

        try {
            JsonNode root = objectMapper.readTree(response);
            if (root.has("errors")) {
                log.warn("GraphQL retornou erros: {}", root.get("errors"));
            }
            return root.path("data");
        } catch (Exception e) {
            log.error("Erro ao processar resposta GraphQL", e);
            throw new GraphQLClientException("Falha ao processar resposta GraphQL", e);
        }
    }

    public static final String QUERY_ALL_BOOKS = """
            query {
              books {
                id title isbn publishYear synopsis genre available
                author { id name nationality }
              }
            }
            """;

    public static final String QUERY_ALL_AUTHORS = """
            query {
              authors {
                id name nationality birthDate biography email
              }
            }
            """;

    public static final String QUERY_ALL_LOANS = """
            query {
              loans {
                id bookId bookTitle memberName memberEmail
                loanDate expectedReturnDate actualReturnDate status
              }
            }
            """;

    public static final String QUERY_BOOK_BY_ID = """
            query BookById($id: ID!) {
              bookById(id: $id) {
                id title isbn publishYear synopsis genre available
                author { id name nationality birthDate email }
              }
            }
            """;
}

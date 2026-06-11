package com.biblioteca.digital.api.rest;

import com.biblioteca.digital.application.usecase.DataExportUseCase;
import com.biblioteca.digital.infrastructure.client.GraphQLClientService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller que expõe:
 * - GET  /api/export          → exporta todos os dados em JSON e XML (salva arquivos)
 * - GET  /api/export/json     → retorna apenas o JSON
 * - GET  /api/export/xml      → retorna apenas o XML
 * - POST /api/client/query    → executa query GraphQL arbitrária (simula cliente externo)
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DataExportRestController {

    private final DataExportUseCase dataExportUseCase;
    private final GraphQLClientService graphQLClientService;

    @GetMapping("/export")
    public ResponseEntity<Map<String, String>> exportAll() {
        DataExportUseCase.ExportResult result = dataExportUseCase.export();
        return ResponseEntity.ok(Map.of(
                "json", result.json(),
                "xml", result.xml(),
                "message", "Arquivos salvos em exports/dados.json e exports/dados.xml"
        ));
    }

    @GetMapping(value = "/export/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> exportJson() {
        DataExportUseCase.ExportResult result = dataExportUseCase.export();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.json());
    }

    @GetMapping(value = "/export/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> exportXml() {
        DataExportUseCase.ExportResult result = dataExportUseCase.export();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .body(result.xml());
    }

    @PostMapping("/client/query")
    public ResponseEntity<JsonNode> clientQuery(@RequestBody ClientQueryRequest request) {
        JsonNode result = request.variables() != null
                ? graphQLClientService.executeQuery(request.query(), request.variables())
                : graphQLClientService.executeQuery(request.query());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/client/books")
    public ResponseEntity<JsonNode> clientFetchBooks() {
        JsonNode result = graphQLClientService.executeQuery(GraphQLClientService.QUERY_ALL_BOOKS);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/client/authors")
    public ResponseEntity<JsonNode> clientFetchAuthors() {
        JsonNode result = graphQLClientService.executeQuery(GraphQLClientService.QUERY_ALL_AUTHORS);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/client/loans")
    public ResponseEntity<JsonNode> clientFetchLoans() {
        JsonNode result = graphQLClientService.executeQuery(GraphQLClientService.QUERY_ALL_LOANS);
        return ResponseEntity.ok(result);
    }

    public record ClientQueryRequest(String query, Map<String, Object> variables) {}
}

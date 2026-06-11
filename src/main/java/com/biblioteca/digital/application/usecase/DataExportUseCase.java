package com.biblioteca.digital.application.usecase;

import com.biblioteca.digital.application.dto.LibraryExportDto;
import com.biblioteca.digital.infrastructure.converter.JsonDataConverter;
import com.biblioteca.digital.infrastructure.converter.XmlDataConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Orquestra a exportação completa da biblioteca:
 * 1. Coleta todos os dados via use cases de domínio
 * 2. Serializa para JSON (dados.json)
 * 3. Converte JSON → XML (dados.xml)
 * 4. Retorna ambos os formatos para exibição
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DataExportUseCase {

    private final AuthorUseCase authorUseCase;
    private final BookUseCase bookUseCase;
    private final LoanUseCase loanUseCase;
    private final JsonDataConverter jsonConverter;
    private final XmlDataConverter xmlConverter;

    private static final Path EXPORT_DIR = Paths.get("exports");

    public ExportResult export() {
        LibraryExportDto data = new LibraryExportDto(
                authorUseCase.findAll(),
                bookUseCase.findAll(),
                loanUseCase.findAll()
        );

        String json = jsonConverter.toJson(data);
        String xml = xmlConverter.toXml(data);

        persistFiles(json, xml);
        log.info("Exportação concluída: {} autores, {} livros, {} empréstimos",
                data.authors().size(), data.books().size(), data.loans().size());

        return new ExportResult(json, xml);
    }

    private void persistFiles(String json, String xml) {
        try {
            Files.createDirectories(EXPORT_DIR);
            Files.writeString(EXPORT_DIR.resolve("dados.json"), json);
            Files.writeString(EXPORT_DIR.resolve("dados.xml"), xml);
            log.info("Arquivos persistidos em: {}", EXPORT_DIR.toAbsolutePath());
        } catch (IOException e) {
            log.error("Erro ao persistir arquivos de exportação", e);
        }
    }

    public record ExportResult(String json, String xml) {}
}

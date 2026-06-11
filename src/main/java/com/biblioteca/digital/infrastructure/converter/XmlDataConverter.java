package com.biblioteca.digital.infrastructure.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class XmlDataConverter {

    private final XmlMapper xmlMapper;

    public XmlDataConverter() {
        this.xmlMapper = XmlMapper.builder()
                .addModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true)
                .build();
    }

    public String toXml(Object data) {
        try {
            return xmlMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("Erro ao serializar para XML", e);
            throw new ConversionException("Falha na serialização XML", e);
        }
    }

    public <T> T fromXml(String xml, Class<T> type) {
        try {
            return xmlMapper.readValue(xml, type);
        } catch (JsonProcessingException e) {
            log.error("Erro ao desserializar XML", e);
            throw new ConversionException("Falha na desserialização XML", e);
        }
    }
}

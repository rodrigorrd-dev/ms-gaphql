package com.biblioteca.digital.infrastructure.graphql;

import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@Configuration
public class DateScalarConfiguration {

    private static final GraphQLScalarType DATE_SCALAR = GraphQLScalarType.newScalar()
            .name("Date")
            .description("ISO-8601 date scalar (yyyy-MM-dd)")
            .coercing(new Coercing<LocalDate, String>() {

                @Override
                public String serialize(Object input, GraphQLContext ctx, Locale locale)
                        throws CoercingSerializeException {
                    if (input instanceof LocalDate date) {
                        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
                    }
                    throw new CoercingSerializeException(
                            "Esperado LocalDate, recebido: " + input.getClass().getSimpleName());
                }

                @Override
                public LocalDate parseValue(Object input, GraphQLContext ctx, Locale locale)
                        throws CoercingParseValueException {
                    try {
                        return LocalDate.parse(input.toString(), DateTimeFormatter.ISO_LOCAL_DATE);
                    } catch (DateTimeParseException e) {
                        throw new CoercingParseValueException("Data inválida: " + input);
                    }
                }

                @Override
                public LocalDate parseLiteral(Value<?> input, CoercedVariables variables,
                                               GraphQLContext ctx, Locale locale)
                        throws CoercingParseLiteralException {
                    if (input instanceof StringValue sv) {
                        try {
                            return LocalDate.parse(sv.getValue(), DateTimeFormatter.ISO_LOCAL_DATE);
                        } catch (DateTimeParseException e) {
                            throw new CoercingParseLiteralException("Data inválida: " + sv.getValue());
                        }
                    }
                    throw new CoercingParseLiteralException("Tipo literal inválido para Date");
                }
            })
            .build();

    @Bean
    public RuntimeWiringConfigurer dateScalarConfigurer() {
        return wiringBuilder -> wiringBuilder.scalar(DATE_SCALAR);
    }
}

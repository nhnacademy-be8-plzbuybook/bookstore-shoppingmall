package com.nhnacademy.book.converter;

import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

@Converter
@Component
public class LocalDateToLongConverter implements AttributeConverter<LocalDate, Long> {

    @Override
    public Long convertToDatabaseColumn(LocalDate attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    @Override
    public LocalDate convertToEntityAttribute(Long dbData) {
        if (dbData == null) {
            return null;
        }
        return LocalDate.ofInstant(Instant.ofEpochMilli(dbData), ZoneId.systemDefault());
    }
}

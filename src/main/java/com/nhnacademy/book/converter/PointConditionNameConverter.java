package com.nhnacademy.book.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.nhnacademy.book.point.domain.PointConditionName;

@Converter
public class PointConditionNameConverter implements AttributeConverter<PointConditionName, String> {

    @Override
    public String convertToDatabaseColumn(PointConditionName attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public PointConditionName convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        return PointConditionName.valueOf(dbData);
    }
}

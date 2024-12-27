package com.nhnacademy.book.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.nhnacademy.book.point.domain.PointConditionName;

@Converter
public class PointConditionNameConverter implements AttributeConverter<PointConditionName, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PointConditionName attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.ordinal(); // Enum의 ordinal 값을 DB에 저장
    }

    @Override
    public PointConditionName convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return PointConditionName.values()[dbData]; // ordinal 값을 Enum으로 변환
    }
}

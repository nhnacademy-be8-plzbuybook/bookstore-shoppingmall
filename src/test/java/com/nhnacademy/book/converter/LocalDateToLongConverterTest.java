package com.nhnacademy.book.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
class LocalDateToLongConverterTest {


    private final LocalDateToLongConverter converter = new LocalDateToLongConverter();

    @Test
    @DisplayName("LocalDate -> Long 변환 테스트")
    void convertToDatabaseColumnTest() {
        LocalDate localDate = LocalDate.of(2025, 1, 20);
        ZoneId zoneId = ZoneId.systemDefault(); // 테스트 환경의 기본 시간대

        Long result = converter.convertToDatabaseColumn(localDate);

        long expected = localDate.atStartOfDay(zoneId).toInstant().toEpochMilli();
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Long -> LocalDate 변환 테스트")
    void convertToEntityAttributeTest() {
        Long epochMilli = 1737331200000L;

        LocalDate result = converter.convertToEntityAttribute(epochMilli);

        assertNotNull(result);
        assertEquals(LocalDate.of(2025, 1, 20), result);
    }

    @Test
    @DisplayName("null 입력에 대한 변환 테스트 (LocalDate -> Long)")
    void convertToDatabaseColumnNullTest() {
        Long result = converter.convertToDatabaseColumn(null);

        assertNull(result);
    }

    @Test
    @DisplayName("null 입력에 대한 변환 테스트 (Long -> LocalDate)")
    void convertToEntityAttributeNullTest() {
        LocalDate result = converter.convertToEntityAttribute(null);

        assertNull(result);
    }
}

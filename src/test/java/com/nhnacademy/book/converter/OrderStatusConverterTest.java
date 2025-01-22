package com.nhnacademy.book.converter;

import com.nhnacademy.book.order.enums.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderStatusConverterTest {

    private final OrderStatusConverter converter = new OrderStatusConverter();

    @Test
    @DisplayName("OrderStatus -> Integer 변환 테스트")
    void convertToDatabaseColumnTest() {
        OrderStatus status = OrderStatus.SHIPPED;

        Integer code = converter.convertToDatabaseColumn(status);

        assertNotNull(code);
        assertEquals(2, code);
    }

    @Test
    @DisplayName("Integer -> OrderStatus 변환 테스트")
    void convertToEntityAttributeTest() {
        Integer code = 4;

        OrderStatus status = converter.convertToEntityAttribute(code);

        assertNotNull(status);
        assertEquals(OrderStatus.DELIVERED, status); // 4는 DELIVERED에 매핑
    }
}
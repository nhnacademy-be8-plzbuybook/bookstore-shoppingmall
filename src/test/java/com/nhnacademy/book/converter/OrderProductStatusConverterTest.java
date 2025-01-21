package com.nhnacademy.book.converter;

import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderProductStatusConverterTest {

    private final OrderProductStatusConverter converter = new OrderProductStatusConverter();

    @Test
    @DisplayName("OrderProductStatus -> Integer 변환 테스트")
    void convertToDatabaseColumnTest() {
        OrderProductStatus status = OrderProductStatus.SHIPPED;

        Integer code = converter.convertToDatabaseColumn(status);

        assertNotNull(code);
        assertEquals(2, code);
    }

    @Test
    @DisplayName("Integer -> OrderProductStatus 변환 테스트")
    void convertToEntityAttributeTest() {
        Integer code = 3;

        OrderProductStatus status = converter.convertToEntityAttribute(code);


        assertNotNull(status);
        assertEquals(OrderProductStatus.DELIVERING, status);
    }


}
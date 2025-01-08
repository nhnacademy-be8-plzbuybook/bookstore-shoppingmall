package com.nhnacademy.book.converter;

import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class OrderProductStatusConverter implements AttributeConverter<OrderProductStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(OrderProductStatus status) {
        return status.getCode();
    }

    @Override
    public OrderProductStatus convertToEntityAttribute(Integer code) {
        return OrderProductStatus.fromCode(code);
    }
}

package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.orderRequests.OrderProductAppliedCouponDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.dto.validatedDtos.ValidateCouponDto;
import com.nhnacademy.book.order.dto.validatedDtos.ValidatedOrderDto;
import com.nhnacademy.book.order.dto.validatedDtos.ValidatedOrderProductDto;
import com.nhnacademy.book.order.dto.validatedDtos.ValidatedWrappingPaperDto;
import com.nhnacademy.book.orderProduct.dto.OrderProductWrappingDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface OrderValidationService {
    void validateOrder(OrderRequestDto order);
    void validateOrderProduct(OrderProductRequestDto orderProduct);
    void validateDeliveryFee(OrderRequestDto order);
    void validateSellingBook(OrderProductRequestDto orderProduct);
    void validateWrappingPaper(OrderProductWrappingDto orderProductWrapping);
    void validateCoupon(OrderProductAppliedCouponDto appliedCoupon);
    void validateDeliveryWishDate(LocalDate deliveryWishDate);
    void validatePoint(int usedPoint);

}

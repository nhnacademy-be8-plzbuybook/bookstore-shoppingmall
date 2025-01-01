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
    ValidatedOrderDto validateOrder(OrderRequestDto order);
    ValidatedOrderProductDto validateOrderProduct(OrderProductRequestDto orderProduct);
    BigDecimal validateDeliveryFee(OrderRequestDto order);
    ValidatedOrderProductDto validateSellingBook(OrderProductRequestDto orderProduct);
    ValidatedWrappingPaperDto validateWrappingPaper(OrderProductWrappingDto orderProductWrapping);
    ValidateCouponDto validateCoupon(OrderProductAppliedCouponDto appliedCoupon);
    LocalDate validateDeliveryWishDate(LocalDate deliveryWishDate);
    int validatePoint(int usedPoint);

}

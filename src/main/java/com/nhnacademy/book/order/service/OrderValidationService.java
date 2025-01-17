package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.orderRequests.OrderProductAppliedCouponDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.orderProduct.dto.OrderProductWrappingDto;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;

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

    void validateOrderProductForReturning(OrderProduct orderProduct);

    void validateOrderProductForReturnCompletion(OrderProduct orderProduct);

    void validateOrderProductForCanceling(OrderProduct orderProduct);

}

package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.dto.orderResponse.OrderResponseDto;
import com.nhnacademy.book.order.dto.validatedDtos.ValidatedOrderDto;
import com.nhnacademy.book.order.dto.validatedDtos.ValidatedOrderProductDto;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.order.service.OrderCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 주문 CRUD 서비스
 */
@RequiredArgsConstructor
@Service
public class OrderCrudServiceImpl implements OrderCrudService {
    private final OrderRepository orderRepository;

    @Transactional
    @Override
    public OrderResponseDto createOrder(OrderRequestDto orderRequest) {
        // 주문 생성
        LocalDateTime currentTime = LocalDateTime.now();
        Orders order = Orders.builder()
                .id(generateOrderId())
                .number(generateOrderNumber(currentTime))
                .name(generateOrderName(orderRequest))
                .usedPoint(orderRequest.getUsedPoint())
                .deliveryWishDate(orderRequest.getDeliveryWishDate())
                .orderedAt(currentTime)
                .orderPrice(orderRequest.getOrderPrice())
                .status(OrderStatus.PAYMENT_PENDING)
                .build();

        // 주문 저장
        Orders savedOrder = orderRepository.save(order);
        BigDecimal paymentPrice = orderRequest.getOrderPrice().add(orderRequest.getDeliveryFee());
        return new OrderResponseDto(savedOrder.getId(), paymentPrice, savedOrder.getName());
    }

    /**
     * 주문이름 생성
     *
     * @param order 주문 요청
     * @return 생성된 주문이름
     */
//    private String generateOrderName(OrderRequestDto order) {
//        List<OrderProductRequestDto> orderProducts = order.getOrderProducts();
//        BookDetailResponseDto book = sellingBookService.getSellingBook(orderProducts.getFirst().getProductId());
//        if (orderProducts.size() > 1) {
//            return String.format("%s 외 %d 건", book.getBookTitle(), orderProducts.size());
//        }
//        return book.getBookTitle();
//    }
    //TODO: 임시
    private String generateOrderName(OrderRequestDto order) {
        List<OrderProductRequestDto> orderProducts = order.getOrderProducts();
        if (orderProducts.size() > 1) {
            return String.format("%s 외 %d 건", "수학의 정석", orderProducts.size());
        }
        return "수학의 정석";
    }

    private String generateOrderName(ValidatedOrderDto order) {
        List<ValidatedOrderProductDto> orderProducts = order.getOrderProducts();
        if (orderProducts.size() > 1) {
            return String.format("%s 외 %d 건", "수학의 정석", orderProducts.size());
        }
        return "수학의 정석";
    }

    /**
     * 주문번호 생성
     *
     * @param orderedAt 주문일시
     * @return 생성된 주문번호
     */
    private String generateOrderNumber(LocalDateTime orderedAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd-HHmmssSSSS");
        String orderedAtString = orderedAt.format(formatter);
        int randomValue = new Random().nextInt(9999); // 0~9999 랜덤 값

        return orderedAtString + "-" + String.format("%04d", randomValue);
    }

    /**
     * UUID를 기반으로 주문 ID 생성
     *
     * @return 생성된 주문 ID
     */
    private String generateOrderId() {
        return UUID.randomUUID().toString();
    }

//
//    private BigDecimal calculateOrderPrice(OrderRequestDto order) {
//        BigDecimal orderPrice = BigDecimal.ZERO;
//
//        for (OrderProductRequestDto orderProduct : order.getOrderProducts()) {
//            // 주문상품 가격 계산
//            orderPrice = orderPrice.add(orderProduct.getPrice().multiply(BigDecimal.valueOf(orderProduct.getQuantity())));
//            // 주문상품 포장지 가격 계산
//            if (orderProduct.getWrapping() != null) {
//                orderPrice = orderPrice.add(orderProduct.getWrapping().getPrice().multiply(BigDecimal.valueOf(orderProduct.getWrapping().getQuantity())));
//            }
//        }
//        return orderPrice;
//    }
//
//
//    private BigDecimal calculateCouponDiscount(OrderRequestDto order) {
//        BigDecimal couponDiscount = BigDecimal.ZERO;
//
//        for (OrderProductRequestDto orderProduct : order.getOrderProducts()) {
//            // 주문상품 적용 쿠폰 계산
//            if (orderProduct.getAppliedCoupons() != null && !orderProduct.getAppliedCoupons().isEmpty()) {
//                for (OrderProductAppliedCouponDto coupon : orderProduct.getAppliedCoupons()) {
//                    couponDiscount = couponDiscount.add(coupon.getDiscount());
//                }
//            }
//        }
//        return couponDiscount;
//    }
}

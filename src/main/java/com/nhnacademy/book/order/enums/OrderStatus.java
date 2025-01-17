package com.nhnacademy.book.order.enums;

import com.nhnacademy.book.deliveryFeePolicy.exception.ConflictException;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import lombok.Getter;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 나중에 로케일을 사용한 국제화
@Getter
public enum OrderStatus {
    PAYMENT_PENDING(0, "결제대기"),
    PAYMENT_COMPLETED(1, "결제완료"),
    SHIPPED(2, "발송완료"),
    DELIVERING(3, "배송중"),
    DELIVERED(4, "배송완료"),
    PARTIALLY_CANCELED(5, "부분취소"),
    ORDER_CANCELLED(6, "주문취소"),
    RETURN_REQUESTED(7, "반품요청"),
    RETURN_COMPLETED(8, "반품완료");

    private final int code;
    private final String status;

    OrderStatus(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public static OrderStatus fromCode(int code) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid payment status code: " + code);
    }

    public static OrderStatus fromStatus(String status) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.getStatus().equals(status)) {
                return orderStatus;
            }
        }
        throw new IllegalArgumentException("Invalid payment status code: " + status);
    }

    /**
     * 결제대기 - 상품 중 1개라도 [결제대기] 상태일 때
     * 결제완료 - 모든 상품이 결제가 완료되었을 때
     * 발송완료 - 모든 상품이 [발송완료]일 때, 상품 중 1개라도 [발송완료]인 경우
     * 배송중 - 모든 상품이 [배송중]일 때, 상품 중 1개라도 [배송중]인 경우
     * 배송완료 - 모든 상품이 [배송완료]
     * 부분취소 - 상품 중 1개라도 [주문취소] 또는 [반품완료]일 때,
     * 주문취소 - 모든 상품이 [주문취소]일 때
     * 반품요청 - 상품 중 1개라도 [반품요청]일 때
     * 반품완료 - 모든 상품이 [반품완료]일 때
     */
    public static OrderStatus fromOrderProductStatus(List<OrderProductStatus> productStatuses) {
        if (productStatuses == null || productStatuses.isEmpty()) {
            throw new ConflictException("주문 상품 상태 목록이 비어있습니다.");
        }

        // EnumMap 사용으로 일반 HashMap보다 더 효율적
        EnumMap<OrderProductStatus, Long> statusCount = productStatuses.stream()
                .collect(Collectors.groupingBy(
                        status -> status,
                        () -> new EnumMap<>(OrderProductStatus.class),
                        Collectors.counting()
                ));

        int totalCount = productStatuses.size();

        // 우선순위가 높은 단일 상품 상태 체크
        for (OrderProductStatus status : statusCount.keySet()) {
            switch (status) {
                case RETURN_REQUESTED:
                    return OrderStatus.RETURN_REQUESTED;
                case PAYMENT_PENDING:
                    return OrderStatus.PAYMENT_PENDING;
                default:
                    continue;
            }
        }

        // 취소 관련 상태 체크
        long cancelledCount = statusCount.getOrDefault(OrderProductStatus.ORDER_CANCELLED, 0L);
        long returnedCount = statusCount.getOrDefault(OrderProductStatus.RETURN_COMPLETED, 0L);

        if (cancelledCount + returnedCount > 0) {
            if (cancelledCount == totalCount) {
                return OrderStatus.ORDER_CANCELLED;
            }
            if (returnedCount == totalCount) {
                return OrderStatus.RETURN_COMPLETED;
            }
            return OrderStatus.PARTIALLY_CANCELED;
        }

        // 배송 관련 상태 체크
        if (statusCount.getOrDefault(OrderProductStatus.SHIPPED, 0L) > 0
                || statusCount.getOrDefault(OrderProductStatus.DELIVERING, 0L) > 0) {
            return OrderStatus.SHIPPED;
        }

        // 전체 상품 상태 체크
        if (statusCount.getOrDefault(OrderProductStatus.DELIVERED, 0L) == totalCount) {
            return OrderStatus.DELIVERING;
        }
        if (statusCount.getOrDefault(OrderProductStatus.PAYMENT_COMPLETED, 0L) == totalCount) {
            return OrderStatus.PAYMENT_COMPLETED;
        }

        throw new ConflictException("해당하는 주문상태가 존재하지 않습니다.");
    }
//    public static OrderStatus fromOrderProductStatus(List<OrderProductStatus> orderProductStatusList) {
//
//        Map<OrderProductStatus, Integer> statusCountMap = new HashMap<>();
//        for (OrderProductStatus orderProductStatus: orderProductStatusList) {
//            statusCountMap.compute(orderProductStatus, (key, value) -> (value == null) ? 0 : value + 1);
//        }
//
//        if (statusCountMap.containsKey(OrderProductStatus.RETURN_REQUESTED) && statusCountMap.get(OrderProductStatus.RETURN_REQUESTED) > 0) {
//            return OrderStatus.RETURN_REQUESTED;
//        }
//        if (statusCountMap.containsKey(OrderProductStatus.PAYMENT_PENDING) && statusCountMap.get(OrderProductStatus.PAYMENT_PENDING) > 0) {
//            return OrderStatus.PAYMENT_PENDING;
//        }
//        if (statusCountMap.containsKey(OrderProductStatus.ORDER_CANCELLED) && statusCountMap.get(OrderProductStatus.ORDER_CANCELLED) > 0 && statusCountMap.get(OrderProductStatus.ORDER_CANCELLED) < orderProductStatusList.size()) {
//            return OrderStatus.PARTIALLY_CANCELED;
//        }
//        if (statusCountMap.containsKey(OrderProductStatus.RETURN_COMPLETED) && statusCountMap.get(OrderProductStatus.RETURN_COMPLETED) > 0 && statusCountMap.get(OrderProductStatus.RETURN_COMPLETED) < orderProductStatusList.size()) {
//            return OrderStatus.PARTIALLY_CANCELED;
//        }
//        if (statusCountMap.containsKey(OrderProductStatus.SHIPPED) && statusCountMap.get(OrderProductStatus.SHIPPED) > 0) {
//            return OrderStatus.SHIPPED;
//        }
//        if (statusCountMap.containsKey(OrderProductStatus.DELIVERING) && statusCountMap.get(OrderProductStatus.DELIVERING) > 0) {
//            return OrderStatus.SHIPPED;
//        }
//        if (statusCountMap.containsKey(OrderProductStatus.PAYMENT_COMPLETED) && statusCountMap.get(OrderProductStatus.PAYMENT_COMPLETED) == orderProductStatusList.size()) {
//            return OrderStatus.PAYMENT_COMPLETED;
//        }
//        if (statusCountMap.containsKey(OrderProductStatus.DELIVERED) && statusCountMap.get(OrderProductStatus.DELIVERED) == orderProductStatusList.size()) {
//            return OrderStatus.DELIVERING;
//        }
//        if (statusCountMap.containsKey(OrderProductStatus.RETURN_COMPLETED) && statusCountMap.get(OrderProductStatus.RETURN_COMPLETED) == orderProductStatusList.size()) {
//            return OrderStatus.RETURN_COMPLETED;
//        }
//        if (statusCountMap.containsKey(OrderProductStatus.ORDER_CANCELLED) && statusCountMap.get(OrderProductStatus.ORDER_CANCELLED) == orderProductStatusList.size()) {
//            return OrderStatus.ORDER_CANCELLED;
//        }
//        throw new ConflictException("해당하는 주문상태가 존재하지 않습니다.");
//    }
}


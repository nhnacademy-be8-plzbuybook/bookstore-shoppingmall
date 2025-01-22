package com.nhnacademy.book.order.controller;

import com.nhnacademy.book.order.dto.OrderProductStatusPatchRequestDto;
import com.nhnacademy.book.order.dto.OrderStatusModifyRequestDto;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.order.service.OrderService;
import com.nhnacademy.book.orderProduct.service.OrderProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.nhnacademy.book.order.enums.OrderStatus.DELIVERED;

@RequestMapping("/api/orders")
@RequiredArgsConstructor
@RestController
public class OrderStatusController {
    private final OrderProductService orderProductService;
    private final OrderService orderService;

    /**
     * 주문상태 목록조회
     *
     * @return 주문상태 리스트
     */
    @GetMapping("/order-status")
    public ResponseEntity<List<OrderStatus>> getOrderStatuses() {
        List<OrderStatus> orderStatuses = List.of(OrderStatus.values());
        return ResponseEntity.ok(orderStatuses);
    }

    /**
     * 주문상태 수정
     *
     * @param orderId       주문 아이디
     * @param modifyRequest 주문상태 수정 DTO
     * @return 상태코드
     */
    @PutMapping("/{order-id}/status")
    public ResponseEntity<Void> patchOrderStatus(@PathVariable("order-id") String orderId,
                                                 @RequestBody OrderStatusModifyRequestDto modifyRequest) {
        if (modifyRequest.getStatus() == DELIVERED) {
            orderService.orderDelivered(orderId);
        } else {
            orderService.modifyStatus(orderId, modifyRequest);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /**
     * 주문상품상태 수정
     *
     * @param orderProductId 주문상품 아이디
     * @param patchRequest   주문상품상태 수정 DTO
     * @return 상태코드
     */
    @PutMapping("/order-products/{order-product-id}/status")
    public ResponseEntity<Void> patchOrderProductsStatus(@PathVariable("order-product-id") Long orderProductId,
                                                         @RequestBody OrderProductStatusPatchRequestDto patchRequest) {
        orderProductService.patchStatus(orderProductId, patchRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

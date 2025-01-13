package com.nhnacademy.book.order.controller;

import com.nhnacademy.book.order.dto.OrderProductStatusPatchRequestDto;
import com.nhnacademy.book.order.dto.OrderStatusModifyRequestDto;
import com.nhnacademy.book.order.service.OrderService;
import com.nhnacademy.book.orderProduct.service.OrderProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.nhnacademy.book.order.enums.OrderStatus.DELIVERED;

@RequiredArgsConstructor
@RestController
public class OrderManagementController {
    private final OrderProductService orderProductService;
    private final OrderService orderService;

    @PutMapping("/api/orders/{order-id}/status")
    public ResponseEntity<Void> patchOrderStatus(@PathVariable("order-id") String orderId,
                                                 @RequestBody OrderStatusModifyRequestDto modifyRequest) {
        if (modifyRequest.getStatus() == DELIVERED) {
            orderService.orderDelivered(orderId);
        }
       orderService.modifyStatus(orderId, modifyRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/api/orders/order-products/{order-product-id}/status")
    public ResponseEntity<Void> patchOrderProductsStatus(@PathVariable("order-product-id") Long orderProductId,
                                                         @RequestBody OrderProductStatusPatchRequestDto patchRequest) {
        orderProductService.patchStatus(orderProductId, patchRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

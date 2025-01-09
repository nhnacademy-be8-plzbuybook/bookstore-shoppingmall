package com.nhnacademy.book.order.controller;

import com.nhnacademy.book.order.dto.OrderProductStatusPatchRequestDto;
import com.nhnacademy.book.orderProduct.service.OrderProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrderManagementController {
    private final OrderProductService orderProductService;

    @PatchMapping("/api/orders/order-products/{order-product-id}/status")
    public ResponseEntity<Void> patchOrderProductsStatus(@PathVariable("order-product-id") Long orderProductId,
                                                         @RequestBody OrderProductStatusPatchRequestDto patchRequest) {
        orderProductService.patchStatus(orderProductId, patchRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

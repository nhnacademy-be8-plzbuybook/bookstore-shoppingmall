package com.nhnacademy.book.order.controller;

import com.nhnacademy.book.order.dto.OrderDeliveryRegisterRequestDto;
import com.nhnacademy.book.order.service.OrderDeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrderDeliveryController {
    private final OrderDeliveryService orderDeliveryService;

    @PostMapping("/api/orders/{order-id}/deliveries")
    public ResponseEntity<?> registerOrderDelivery(@PathVariable("order-id") String orderId,
                                                   @Valid @RequestBody OrderDeliveryRegisterRequestDto registerRequest) {
        registerRequest.setOrderId(orderId);
        orderDeliveryService.registerOrderDelivery(registerRequest);

        return ResponseEntity.ok("주문 배송이 정상적으로 등록되었습니다.");
    }


}

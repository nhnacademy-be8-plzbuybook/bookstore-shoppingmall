package com.nhnacademy.book.order.controller;

import com.nhnacademy.book.order.dto.OrderDeliveryRegisterRequestDto;
import com.nhnacademy.book.order.service.OrderDeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/orders")
@RequiredArgsConstructor
@RestController
public class OrderDeliveryController {
    private final OrderDeliveryService orderDeliveryService;


    /**
     * 주문배송 등록
     *
     * @param orderId
     * @param registerRequest
     * @return ResponseEntity
     */
    @PostMapping("/{order-id}/deliveries")
    public ResponseEntity<Void> registerOrderDelivery(@PathVariable("order-id") String orderId,
                                                   @Valid @RequestBody OrderDeliveryRegisterRequestDto registerRequest) {
        orderDeliveryService.registerOrderDelivery(orderId, registerRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 주문배송 완료처리
     *
     * @param orderId
     * @param deliveryId
     * @return
     */
    @PostMapping("/{order-id}/deliveries/{delivery-id}/complete")
    public ResponseEntity<Void> completeOrderDelivery(@PathVariable("order-id") String orderId,
                                                      @PathVariable("delivery-id") Long deliveryId) {
        orderDeliveryService.completeOrderDelivery(orderId, deliveryId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

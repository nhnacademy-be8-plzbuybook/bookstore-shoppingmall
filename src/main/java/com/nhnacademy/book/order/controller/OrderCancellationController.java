package com.nhnacademy.book.order.controller;

import com.nhnacademy.book.order.dto.OrderCancelRequestDto;
import com.nhnacademy.book.order.service.OrderCancellationService;
import com.nhnacademy.book.order.dto.OrderProductCancelRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/orders")
@RequiredArgsConstructor
@RestController
public class OrderCancellationController {
    private final OrderCancellationService orderCancellationService;


    /**
     * 전체 주문취소
     *
     * @param orderId
     * @return
     */
//    @PostMapping("/{order-id}/cancel")
//    public ResponseEntity<?> cancelOrder(@PathVariable("order-id") String orderId,
//                                         @RequestBody OrderCancelRequestDto cancelRequest) {
//        orderCancellationService.cancelOrder(orderId, cancelRequest);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }


    /**
     * 주문상품 취소
     *
     * @param orderId                   주문아이디
     * @param orderProductId            주문상품 아이디
     * @param orderProductCancelRequest 주문상품취소 요청 DTO
     * @return void
     */
    @PostMapping("/{order-id}/order-products/{order-product-id}/cancel")
    public ResponseEntity<Void> cancelOrderProduct(@PathVariable("order-id") String orderId,
                                                   @PathVariable("order-product-id") Long orderProductId,
                                                   @RequestBody OrderProductCancelRequestDto orderProductCancelRequest) {
        orderCancellationService.cancelOrderProduct(orderId, orderProductId, orderProductCancelRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

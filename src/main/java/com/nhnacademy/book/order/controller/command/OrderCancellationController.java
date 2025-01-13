package com.nhnacademy.book.order.controller.command;

import com.nhnacademy.book.order.dto.OrderCancelRequestDto;
import com.nhnacademy.book.order.service.OrderProcessService;
import com.nhnacademy.book.orderProduct.service.OrderProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/orders")
@RequiredArgsConstructor
@RestController
public class OrderCancellationController {
    private final OrderProcessService orderProcessService;
    private final OrderProductService orderProductService;


    /**
     * 전체 주문취소
     *
     * @param orderId
     * @return
     */
    @PostMapping("/{order-id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable("order-id") String orderId,
                                         @RequestBody OrderCancelRequestDto cancelRequest) {
        orderProcessService.cancelOrder(orderId, cancelRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /**
     * 주문상품 취소
     *
     * @param orderProductId 주문상품 아이디
     * @param quantity       취소할 수량
     * @return
     */
    @PostMapping("/order-products/{order-product-id}/cancel")
    public ResponseEntity<?> cancelOrderProduct(@PathVariable("order-product-id") Long orderProductId,
                                                @RequestParam(value = "quantity", required = false, defaultValue = "1") Integer quantity) {
        orderProductService.cancelOrderProduct(orderProductId, quantity);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

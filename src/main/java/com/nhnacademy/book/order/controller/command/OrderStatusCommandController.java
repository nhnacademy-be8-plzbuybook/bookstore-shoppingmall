package com.nhnacademy.book.order.controller.command;

import com.nhnacademy.book.order.dto.OrderProductStatusPatchRequestDto;
import com.nhnacademy.book.order.dto.OrderStatusModifyRequestDto;
import com.nhnacademy.book.order.service.OrderService;
import com.nhnacademy.book.orderProduct.service.OrderProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.nhnacademy.book.order.enums.OrderStatus.DELIVERED;

@RequestMapping("/api/orders")
@RequiredArgsConstructor
@RestController
public class OrderStatusCommandController {
    private final OrderProductService orderProductService;
    private final OrderService orderService;

    /**
     * 주문상태 수정
     *
     * @param orderId
     * @param modifyRequest
     * @return
     */
    @PutMapping("{order-id}/status")
    public ResponseEntity<Void> patchOrderStatus(@PathVariable("order-id") String orderId,
                                                 @RequestBody OrderStatusModifyRequestDto modifyRequest) {
        if (modifyRequest.getStatus() == DELIVERED) {
            orderService.orderDelivered(orderId);
        }
       orderService.modifyStatus(orderId, modifyRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /**
     * 주문상품상태 수정
     *
     * @param orderProductId
     * @param patchRequest
     * @return
     */
    @PutMapping("/order-products/{order-product-id}/status")
    public ResponseEntity<Void> patchOrderProductsStatus(@PathVariable("order-product-id") Long orderProductId,
                                                         @RequestBody OrderProductStatusPatchRequestDto patchRequest) {
        orderProductService.patchStatus(orderProductId, patchRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

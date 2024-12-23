package com.nhnacademy.book.order.controller;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.deliveryFeePolicy.exception.StockNotEnoughException;
import com.nhnacademy.book.order.dto.OrderSaveRequestDto;
import com.nhnacademy.book.order.dto.OrderSaveResponseDto;
import com.nhnacademy.book.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderService orderService;
    @PostMapping("/api/orders")
    public ResponseEntity<?> createOrder(@RequestBody OrderSaveRequestDto saveRequest) {
        try {
            OrderSaveResponseDto response = orderService.createOrder(saveRequest);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 에러 응답처리
        } catch (StockNotEnoughException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // 에러 응답처리
        }
    }

}

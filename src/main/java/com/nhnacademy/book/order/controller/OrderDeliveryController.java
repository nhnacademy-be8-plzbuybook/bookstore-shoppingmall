package com.nhnacademy.book.order.controller;

import com.nhnacademy.book.order.service.OrderDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrderDeliveryController {
    private final OrderDeliveryService orderDeliveryService;


}

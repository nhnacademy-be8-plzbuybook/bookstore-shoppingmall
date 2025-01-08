package com.nhnacademy.book.cart.controller;

import com.nhnacademy.book.cart.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/bookstore/guests/carts")
    public ResponseEntity<Long> createGuestCart() {
        return ResponseEntity.ok(cartService.createGuestCart());
    }
}

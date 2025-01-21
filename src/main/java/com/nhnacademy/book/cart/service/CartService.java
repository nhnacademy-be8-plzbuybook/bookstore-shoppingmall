package com.nhnacademy.book.cart.service;

import com.nhnacademy.book.member.domain.Cart;

import java.util.Optional;

public interface CartService {
    Long createCart(String email);

    Optional<Cart> getCart(Long memberId);
}

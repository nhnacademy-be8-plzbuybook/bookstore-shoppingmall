package com.nhnacademy.book.cart.service;

import com.nhnacademy.book.member.domain.Cart;

import java.util.Optional;

public interface CartService {
    Cart createCart(String email);

    Optional<Cart> getCart(Long memberId);

    // 요구사항이 회원을 삭제하면 정보는 남아있고 상태로서 탈퇴 처리를 하기 때문에 필요 x
    // void deleteCart(Long cartId);
}

package com.nhnacademy.book.cart.service.impl;

import com.nhnacademy.book.cart.repository.CartRepository;
import com.nhnacademy.book.cart.service.CartService;
import com.nhnacademy.book.member.domain.Cart;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;

    @Override
    public Cart createCart(String email) {
        Long memberId = memberRepository.getMemberIdByEmail(email);

        return cartRepository.findByMember_MemberId(memberId)
                .orElseGet(() -> {
                    // 존재하지 않는 경우 새로운 장바구니 생성
                    Cart cart = new Cart(memberRepository.findById(memberId).orElseThrow());
                    cartRepository.save(cart);
                    return cart; // 생성된 장바구니 객체 반환
                });
    }

    @Override
    public Optional<Cart> getCart(Long memberId) {
        return cartRepository.findByMember_MemberId(memberId);
    }

}

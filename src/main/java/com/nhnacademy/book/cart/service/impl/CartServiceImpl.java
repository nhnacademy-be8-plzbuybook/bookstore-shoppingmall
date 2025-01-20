package com.nhnacademy.book.cart.service.impl;

import com.nhnacademy.book.cart.exception.CartAlreadyExistsException;
import com.nhnacademy.book.cart.repository.CartRepository;
import com.nhnacademy.book.cart.service.CartService;
import com.nhnacademy.book.member.domain.Cart;
import com.nhnacademy.book.member.domain.exception.MemberNotFoundException;
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
    public Long createCart(String email) {
        Long memberId = memberRepository.getMemberIdByEmail(email);
        if (cartRepository.existsById(memberId)) {
            throw new CartAlreadyExistsException("이미 장바구니가 존재합니다.");
        }
        Cart cart = new Cart(memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("해당하는 회원이 존재하지 않습니다.")));
        cartRepository.save(cart);
        return cart.getCartId();

    }

    @Override
    public Optional<Cart> getCart(Long memberId) {
        return cartRepository.findByMember_MemberId(memberId);
    }

}

package com.nhnacademy.book.cart.service.impl;

import com.nhnacademy.book.cart.exception.CartAlreadyExistsException;
import com.nhnacademy.book.cart.repository.CartRepository;
import com.nhnacademy.book.member.domain.Cart;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.exception.MemberNotFoundException;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private final String email = "test@test.com";
    private final Long memberId = 1L;
    private Member member;
    private Cart cart;

    @BeforeEach
    void setUp() {
        member = new Member();
        cart = new Cart(member);
    }

    @Test
    @DisplayName("장바구니 생성 성공")
    void createCart() {
        when(memberRepository.getMemberIdByEmail(email)).thenReturn(memberId);
        when(cartRepository.existsById(memberId)).thenReturn(false);
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));

        Long result = cartService.createCart(email);

        assertThat(result).isEqualTo(cart.getCartId());
        verify(cartRepository).save(any());
    }

    @Test
    @DisplayName("이미 존재하는 장바구니 생성 시 에외 발생 테스트")
    void createCart_CartAlreadyExistsException() {
        when(memberRepository.getMemberIdByEmail(email)).thenReturn(memberId);
        when(cartRepository.existsById(memberId)).thenReturn(true);

        assertThatThrownBy(() -> cartService.createCart(email))
                .isInstanceOf(CartAlreadyExistsException.class)
                .hasMessage("이미 장바구니가 존재합니다.");
    }

    @Test
    @DisplayName("존재하지 않는 회원의 장바구니 생성 시 예외 발생 테스트")
    void createCart_MemberNotFoundException() {
        when(memberRepository.getMemberIdByEmail(email)).thenReturn(memberId);
        when(cartRepository.existsById(memberId)).thenReturn(false);
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.createCart(email))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage("해당하는 회원이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("장바구니 조회 성공")
    void getCart_Success() {
        given(cartRepository.findByMember_MemberId(memberId)).willReturn(Optional.of(cart));

        Optional<Cart> result = cartService.getCart(memberId);

        assertThat(result).isPresent();
        assertThat(result).isEqualTo(Optional.of(cart));

    }


}
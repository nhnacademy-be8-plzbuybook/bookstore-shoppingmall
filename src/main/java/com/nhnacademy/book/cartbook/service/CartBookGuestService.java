package com.nhnacademy.book.cartbook.service;

import com.nhnacademy.book.cartbook.dto.response.ReadCartBookResponseDto;
import com.nhnacademy.book.member.domain.Cart;

import java.util.List;
import java.util.Optional;

public interface CartBookGuestService {
    Optional<Cart> readCartBook(Long cartId, Long memberId);

    Long createCartBook(Long sellingBookId, Long cartId, int quantity);

    Long updateCartBook(Long sellingBookId, Long cartId, int quantity);

    Long deleteCartBook(Long cartBookId, Long cartId);

    String deleteAllGuestBookCart(Long cartId);

    List<ReadCartBookResponseDto> readAllCartBook(Long cartId);
}

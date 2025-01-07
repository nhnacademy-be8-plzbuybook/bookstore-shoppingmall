package com.nhnacademy.book.cartbook.service;

import com.nhnacademy.book.cartbook.dto.response.ReadGuestCartBookResponseDto;
import com.nhnacademy.book.member.domain.Cart;

import java.util.List;
import java.util.Optional;

public interface CartBookGuestService {
    Optional<Cart> readCartBook(Long cartId, Long memberId);
//    Long createCartBook(Long sellingBookId, int quantity);

    Long createCartBook(Long sellingBookId, Long cartId, int quantity);

    Long updateCartBook(Long sellingBookId, Long cartId, int quantity);
    Long deleteCartBook(Long cartBookId, Long cartId);

//    Long deleteCartBook(Long cartBookId);

    String deleteAllGuestBookCart(Long cartId);

    List<ReadGuestCartBookResponseDto> readAllCartBook(Long cartId);
}

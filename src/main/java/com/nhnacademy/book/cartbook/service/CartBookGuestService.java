package com.nhnacademy.book.cartbook.service;

import com.nhnacademy.book.cartbook.dto.request.CreateCartBookRequestDto;
import com.nhnacademy.book.cartbook.dto.response.ReadCartBookResponseDto;

import java.util.List;

public interface CartBookGuestService {
    Long AddToGuestCart(CreateCartBookRequestDto createCartBookRequestDto, String sessionId);

    Long updateGuestCartItem(Long sellingBookId, int quantity, String sessionId);

    Long removeItemFromGuestCart(Long sellingBookId, String sessionId);

    void clearGuestCart(String sessionId);

    List<ReadCartBookResponseDto> getGuestCart(String sessionId);
}

package com.nhnacademy.book.cartbook.service;

import com.nhnacademy.book.cartbook.dto.request.CreateCartBookRequestDto;
import com.nhnacademy.book.cartbook.dto.response.ReadCartBookResponseDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface CartBookGuestService {

    Long AddtoGuestCart(CreateCartBookRequestDto createCartBookRequestDto, HttpServletRequest request);

    Long updateGuestCartItem(Long sellingBookId, int quantity, HttpServletRequest request);

    Long removeItemFromGuestCart(Long sellingBookId, HttpServletRequest request);

    void clearGuestCart(HttpServletRequest request);

    List<ReadCartBookResponseDto> getGuestCart(HttpServletRequest request);
}

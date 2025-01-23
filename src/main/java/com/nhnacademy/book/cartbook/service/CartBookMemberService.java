package com.nhnacademy.book.cartbook.service;

import com.nhnacademy.book.cartbook.dto.request.CreateCartBookRequestDto;
import com.nhnacademy.book.cartbook.dto.request.UpdateCartBookRequestDto;
import com.nhnacademy.book.cartbook.dto.response.ReadCartBookResponseDto;

import java.util.List;

public interface CartBookMemberService {
    List<ReadCartBookResponseDto> readAllCartMember(String email);

    Long createBookCartMember(CreateCartBookRequestDto createCartBookRequestDto, String email);

    Long updateBookCartMember(UpdateCartBookRequestDto updateCartBookRequestDto, String email);

    String deleteBookCartMember(Long cartBookId, String email);

    String  deleteAllBookCartMember(String email);

}

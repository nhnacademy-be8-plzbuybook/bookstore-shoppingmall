package com.nhnacademy.book.cartbook.service;

import com.nhnacademy.book.cartbook.dto.request.*;
import com.nhnacademy.book.cartbook.dto.response.ReadAllMemberCartBookResponseDto;


import java.util.List;

public interface CartBookMemberService {
    List<ReadAllMemberCartBookResponseDto> readAllCartMember(String email);

    Long createBookCartMember(CreateCartBookRequestDto createCartBookRequestDto, String email);

    Long updateBookCartMember(UpdateCartBookRequestDto updateCartBookRequestDto, String email);

    String deleteBookCartMember(Long cartBookId, String email);

    String  deleteAllBookCartMember(String email);

}

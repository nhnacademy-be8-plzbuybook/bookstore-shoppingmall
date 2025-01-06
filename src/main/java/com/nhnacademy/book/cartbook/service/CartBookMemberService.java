package com.nhnacademy.book.cartbook.service;

import com.nhnacademy.book.cartbook.dto.request.*;
import com.nhnacademy.book.cartbook.dto.response.CreateMemberCartBookResponseDto;
import com.nhnacademy.book.cartbook.dto.response.ReadAllMemberCartBookResponseDto;
import com.nhnacademy.book.cartbook.dto.response.ReadMemberCartBookResponseDto;
import com.nhnacademy.book.cartbook.dto.response.UpdateMemberCartBookResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CartBookMemberService {
    List<ReadAllMemberCartBookResponseDto> readAllCartMember(String email);

    ReadMemberCartBookResponseDto readMemberCartBook(Long cartBookId, String email);

    CreateMemberCartBookResponseDto createBookCartMember(CreateCartBookRequestDto createCartBookRequestDto, String email);

    UpdateMemberCartBookResponseDto updateBookCartMember(UpdateCartBookRequestDto updateCartBookRequestDto, String email);

    ResponseEntity<Long> deleteBookCartMember(Long cartBookId, String email);

    ResponseEntity<String>  deleteAllBookCartMember(String email);

}

package com.nhnacademy.book.cartbook.dto.request;

import lombok.Builder;

@Builder
public record ReadMemberCartBookRequestDto(
        Long cartBookId // 책 장바구니 ID
) {}
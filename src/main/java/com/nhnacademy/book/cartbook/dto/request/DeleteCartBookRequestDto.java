package com.nhnacademy.book.cartbook.dto.request;

import lombok.Builder;

@Builder
public record DeleteCartBookRequestDto(
        Long cartBookId,
        Long cartId
) {}

package com.nhnacademy.book.cartbook.dto.request;

import lombok.Builder;

@Builder
public record UpdateCartBookRequestDto(
        Long cartBookId,
        Long sellingBookId,
        int quantity ) {}

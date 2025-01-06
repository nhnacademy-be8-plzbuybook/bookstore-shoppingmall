package com.nhnacademy.book.cartbook.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record UpdateMemberCartBookResponseDto(
        Long bookCartId,
        Long sellingBookId,
        BigDecimal sellingBookPrice,
        String url,
        String title,
        int quantity,
        int sellingBookStock ) {
}

package com.nhnacademy.book.book.dto;

import com.querydsl.core.annotations.QueryProjection;

public record SellingBookSimpleResponseDto(Long id, String image, String bookTitle) {
    @QueryProjection
    public SellingBookSimpleResponseDto {
    }
}

package com.nhnacademy.book.book.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public record SellingBookSimpleDto(Long id, String image, String bookTitle) {
    @QueryProjection
    public SellingBookSimpleDto {
    }
}

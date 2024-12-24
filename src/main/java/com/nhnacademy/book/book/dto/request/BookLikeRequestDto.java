package com.nhnacademy.book.book.dto.request;

import lombok.Data;

@Data
public class BookLikeRequestDto {
    private Long memberId;
    private Long sellingBookId;
}

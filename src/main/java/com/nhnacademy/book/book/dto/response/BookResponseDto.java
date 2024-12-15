package com.nhnacademy.book.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookResponseDto {
    private Long id;
    private String title;
    private String author;
    private String category;
    private int originalPrice;
    private int salePrice;
}

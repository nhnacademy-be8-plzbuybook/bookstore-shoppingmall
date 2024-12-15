package com.nhnacademy.book.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookDetailResponseDto {
    private Long id;
    private String title;
    private String tableOfContents;
    private String description;
    private String author;
    private String publisher;
    private String publishDate;
    private String isbn;
    private int originalPrice;
    private int salePrice;
    private int discountRate;
    private boolean giftWrapAvailable;
}


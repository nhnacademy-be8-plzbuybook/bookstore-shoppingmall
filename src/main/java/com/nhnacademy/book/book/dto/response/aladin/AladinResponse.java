package com.nhnacademy.book.book.dto.response.aladin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AladinResponse {
    private String title;
    private String author;
    private String pubDate;
    private String isbn13;
    private String publisher;
    private int priceStandard;
    private String cover;
    private String categoryName;
    private String description; // description 필드 추가
}

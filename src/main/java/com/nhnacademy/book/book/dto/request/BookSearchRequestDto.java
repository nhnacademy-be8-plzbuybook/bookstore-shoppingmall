package com.nhnacademy.book.book.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookSearchRequestDto {
    private String title;
    private String author;
    private String category;

    //추가버전
    private String keyword;
    private Long categoryId;
    private Boolean onlyAvailable;
}


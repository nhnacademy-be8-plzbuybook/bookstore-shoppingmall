package com.nhnacademy.book.book.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookSearchRequestDto {
    private String title;
    private String author;
    private String category;
}


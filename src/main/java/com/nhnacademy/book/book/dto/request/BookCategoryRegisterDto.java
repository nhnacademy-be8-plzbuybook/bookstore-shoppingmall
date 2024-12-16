package com.nhnacademy.book.book.dto.request;

import lombok.Data;

@Data
public class BookCategoryRegisterDto {
    private String categoryName;
    private Long parentCategoryId;
}

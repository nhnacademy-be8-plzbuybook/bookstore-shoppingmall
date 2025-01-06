package com.nhnacademy.book.book.dto.request;


import lombok.Data;

@Data
public class CategoryRegisterDto {

    private Long parentCategoryId;
    private String parentCategoryName;
    private String newCategoryName;

}

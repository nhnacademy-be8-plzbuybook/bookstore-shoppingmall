package com.nhnacademy.book.book.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class CategorySimpleResponseDto {

    private Long categoryId;           // 카테고리 ID
    private String categoryName;       // 카테고리 이름

}

package com.nhnacademy.book.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {

    private Long categoryId;           // 카테고리 ID
    private String categoryName;       // 카테고리 이름
    private Integer categoryDepth;     // 카테고리 깊이
    private Long parentCategoryId;    // 부모 카테고리 ID (부모 카테고리가 없을 경우 null)
    private List<CategoryResponseDto> childrenCategories;  // 자식 카테고리 목록
}

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

    // 부모 카테고리 정보를 포함하는 생성자
//    public CategoryResponseDto(Long categoryId, String categoryName, Integer categoryDepth,
//                               Long parentCategoryId, List<CategoryResponseDto> childrenCategories) {
//        this.categoryId = categoryId;
//        this.categoryName = categoryName;
//        this.categoryDepth = categoryDepth;
//        this.parentCategoryId = parentCategoryId;
//        this.childrenCategories = childrenCategories;
//    }
//
//    public CategoryResponseDto(String newCategory, int i, Long categoryId) {
//        this.categoryName = newCategory;
//        this.categoryDepth = i;
//        this.parentCategoryId = categoryId;
//    }
}

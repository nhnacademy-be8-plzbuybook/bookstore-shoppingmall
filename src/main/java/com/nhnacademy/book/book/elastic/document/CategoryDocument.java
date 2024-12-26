package com.nhnacademy.book.book.elastic.document;

import com.nhnacademy.book.book.entity.BookCategory;
import com.nhnacademy.book.book.entity.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Document(indexName = "category")

public class CategoryDocument {

    @org.springframework.data.annotation.Id
    private Long categoryId;

    private Category parentCategory;

    private List<Category> childrenCategory = new ArrayList<>();

    private String categoryName;

    private Integer categoryDepth;

    private List<BookCategory> bookCategories = new ArrayList<>();


//    //생성자
//    public Category(String categoryName, Integer categoryDepth, Category parentCategory) {
//        this.categoryName = categoryName;
//        this.categoryDepth = categoryDepth;
//        this.parentCategory = parentCategory;
//    }
//
//    // 자식 카테고리 추가
//    public void addChildCategory(Category childCategory) {
//        childCategory.setParentCategory(this);
//        childCategory.setCategoryDepth(this.categoryDepth + 1); // 부모의 깊이 + 1
//        this.childrenCategory.add(childCategory);
//    }
}

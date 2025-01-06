package com.nhnacademy.book.book.elastic.document;

import com.nhnacademy.book.book.entity.BookCategory;
import com.nhnacademy.book.book.entity.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Document(indexName = "category_4")

public class CategoryDocument {

    @org.springframework.data.annotation.Id
    private Long categoryId;

    @Field(name="parent_category")
    private Category parentCategory;

    private List<Category> childrenCategory = new ArrayList<>();
    @Field(type = FieldType.Text, name="category_name")
    private String categoryName;
    @Field(name="category_depth")
    private Integer categoryDepth;



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

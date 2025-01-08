package com.nhnacademy.book.book.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "category_parent_id", nullable = true)
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Category> childrenCategory = new ArrayList<>();

    @Column(length = 100)
    private String categoryName;

    @Column(nullable = false)
    private Integer categoryDepth;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookCategory> bookCategories = new ArrayList<>();


    //생성자
    public Category(String categoryName, Integer categoryDepth, Category parentCategory) {
        this.categoryName = categoryName;
        this.categoryDepth = categoryDepth;
        this.parentCategory = parentCategory;
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    // 자식 카테고리 추가
    public void addChildCategory(Category childCategory) {
        childCategory.setParentCategory(this);
        childCategory.setCategoryDepth(this.categoryDepth + 1); // 부모의 깊이 + 1
        this.childrenCategory.add(childCategory);
    }
}

package com.nhnacademy.book.book.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@RequiredArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "category_parent_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> childrenCategory = new ArrayList<>();

    @Column(length = 20)
    private String categoryName;

    @Column(nullable = false)
    private Integer categoryDepth;

    @ManyToMany(mappedBy = "categories")
    private List<Book> books = new ArrayList<>();
}

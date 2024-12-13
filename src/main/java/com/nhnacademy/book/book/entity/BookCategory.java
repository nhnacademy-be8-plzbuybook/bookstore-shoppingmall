package com.nhnacademy.book.book.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "book_category")
@RequiredArgsConstructor
@AllArgsConstructor
public class BookCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}


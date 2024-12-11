package com.nhnacademy.book.book.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "book_category")
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


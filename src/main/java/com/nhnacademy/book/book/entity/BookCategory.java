package com.nhnacademy.book.book.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book_category")
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class BookCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Setter
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public BookCategory(Book book, Category category) {
        this.book = book;
        this.category = category;
    }
}


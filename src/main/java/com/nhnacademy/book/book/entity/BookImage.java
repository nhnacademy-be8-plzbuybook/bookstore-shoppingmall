package com.nhnacademy.book.book.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "book_image")
public class BookImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookImageId;

    @ManyToOne
    @JoinColumn(name = "book_image_book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private Long imageId;
}


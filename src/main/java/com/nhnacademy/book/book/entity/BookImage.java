package com.nhnacademy.book.book.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "book_image")
@RequiredArgsConstructor
@AllArgsConstructor
public class BookImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_image_book_id", nullable = false)
    private Book book;

    @Column(name = "image_path")
    private String imageUrl;

    public BookImage(Book book, String imageUrl) {
        this.book = book;
        this.imageUrl = imageUrl;
    }
}


package com.nhnacademy.book.book.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "book_author")
@RequiredArgsConstructor
@AllArgsConstructor
public class BookAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
}

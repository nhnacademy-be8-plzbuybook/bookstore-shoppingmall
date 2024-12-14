package com.nhnacademy.book.book.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book_author")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class BookAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Setter
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
}

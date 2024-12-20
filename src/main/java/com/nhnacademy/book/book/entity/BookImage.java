package com.nhnacademy.book.book.entity;

import com.nhnacademy.book.member.domain.Image;
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

    @ManyToOne
    @JoinColumn(name = "bi_book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "bi_id", nullable = false)
    private Image image;
}


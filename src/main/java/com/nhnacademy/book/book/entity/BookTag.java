package com.nhnacademy.book.book.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@RequiredArgsConstructor
@Setter
public class BookTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookTagId;

    @ManyToOne
    @JoinColumn(name = "bt_tag_id")
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "bt_book_id")
    private Book book;


}

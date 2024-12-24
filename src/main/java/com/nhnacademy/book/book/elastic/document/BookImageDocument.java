package com.nhnacademy.book.book.elastic.document;

import com.nhnacademy.book.book.entity.Book;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter


@RequiredArgsConstructor
@AllArgsConstructor
@Document(indexName = "book_image")

public class BookImageDocument {
    @org.springframework.data.annotation.Id
    private Long bookImageId;

    @ManyToOne
    @JoinColumn(name = "book_image_book_id", nullable = false)
    private Book book;

    @Column(name = "image_path")
    private String imageUrl;
}


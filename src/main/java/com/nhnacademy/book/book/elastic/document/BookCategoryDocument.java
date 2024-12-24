package com.nhnacademy.book.book.elastic.document;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Document(indexName = "book_category")
public class BookCategoryDocument {

    @org.springframework.data.annotation.Id
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Setter
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public BookCategoryDocument(Book book, Category category) {
        this.book = book;
        this.category = category;
    }
}


package com.nhnacademy.book.book.elastic.document;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Document(indexName = "book_category")
public class BookCategoryDocument {

    @org.springframework.data.annotation.Id
    private Long id;

    @Setter
    @Field(name="book_id")
    private Long bookId;

    @Setter
    @Field(name="category_id")
    private Long categoryId;


}


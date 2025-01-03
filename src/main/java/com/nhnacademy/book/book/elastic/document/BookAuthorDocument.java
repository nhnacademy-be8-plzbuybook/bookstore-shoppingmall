package com.nhnacademy.book.book.elastic.document;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;


@Getter
@Setter
@RequiredArgsConstructor
@Document(indexName = "book_author_4")
public class BookAuthorDocument {

    @org.springframework.data.annotation.Id
    private Long id;
    @Field(name="author_id")
    private Long authorId;
    @Field(name="book_id")
    private Long bookId;
}

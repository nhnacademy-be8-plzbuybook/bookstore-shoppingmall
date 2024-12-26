package com.nhnacademy.book.book.elastic.document;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;


@Getter
@Setter
@RequiredArgsConstructor
@Document(indexName = "book_author")
public class BookAuthorDocument {

    @org.springframework.data.annotation.Id
    private Long id;
    private Long authorId;
    private Long bookId;
}

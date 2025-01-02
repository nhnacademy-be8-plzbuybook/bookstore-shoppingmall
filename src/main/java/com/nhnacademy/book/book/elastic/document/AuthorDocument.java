package com.nhnacademy.book.book.elastic.document;


import com.nhnacademy.book.book.entity.BookAuthor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Document(indexName = "author")
@Getter
@Setter
public class AuthorDocument {

    @org.springframework.data.annotation.Id
    private Long authorId;
    @Field(type = FieldType.Text)
    private String authorName;

    private List<BookAuthor> bookAuthors = new ArrayList<>();

}

package com.nhnacademy.book.book.elastic.document;


import com.nhnacademy.book.book.entity.Book;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.ArrayList;
import java.util.List;

@Setter
@Document(indexName = "publisher_4")
@Getter
@RequiredArgsConstructor
public class PublisherDocument {


    @org.springframework.data.annotation.Id
    private long publisherId;

    private String publisherName;

    private List<Book> books = new ArrayList<>();


}

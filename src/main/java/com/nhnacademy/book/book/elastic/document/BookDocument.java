package com.nhnacademy.book.book.elastic.document;


import com.nhnacademy.book.converter.LocalDateToLongConverter;
import com.thoughtworks.xstream.converters.time.LocalDateConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.mapping.*;

@Document(indexName = "book1")
@Getter
@Setter
@RequiredArgsConstructor
public class BookDocument {

    @org.springframework.data.annotation.Id
    private long bookId;
    @Field(type = FieldType.Text, name="book_title")
    private String bookTitle;
    @Field(type = FieldType.Text, name="book_index")
    private String bookIndex;
    @Field(type = FieldType.Text, name="book_description")
    private String bookDescription;
//    @Convert(converter = LocalDateToLongConverter.class)
    @Field(type = FieldType.Date, pattern = "yyyy-MM-dd", name="book_pub_date")
    private LocalDate bookPubDate;
    @Field(name="book_price_standard")
    private BigDecimal bookPriceStandard;
    @Field(name="book_isbn13")
    private String bookIsbn13;
    @Field(name="publisher_id")
    private Long publisherId; // Publisher의 이름을 저장하는 필드 추가
    private String imageUrl;







    // 추가적인 필드를 Elasticsearch에서 필요로 하는 형태로 추가

    @Override
    public String toString() {
        return "bookId : " + bookId + ", bookTitle : " + bookTitle + "'";
    }
}

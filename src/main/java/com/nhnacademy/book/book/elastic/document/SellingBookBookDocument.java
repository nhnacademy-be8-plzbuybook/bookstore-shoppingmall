package com.nhnacademy.book.book.elastic.document;


import com.nhnacademy.book.book.entity.SellingBook;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Document(indexName = "selling_book_book")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class SellingBookBookDocument {

    @org.springframework.data.annotation.Id
    private Long bookId;

    @Field(type = FieldType.Text)
    private String bookTitle;

    @Field(type = FieldType.Keyword)
    private String bookIsbn13;

    @Field(type = FieldType.Double)
    private BigDecimal bookPriceStandard;

    @Field(type = FieldType.Long)
    private Long sellingBookId;

    @Field(type = FieldType.Double)
    private BigDecimal sellingBookPrice;

    @Field(type = FieldType.Integer)
    private Integer sellingBookStock;

    @Field(type = FieldType.Keyword)
    private SellingBook.SellingBookStatus sellingBookStatus;

    @Field(type = FieldType.Text)
    private String bookIndex;

    @Field(type = FieldType.Text)
    private String bookDescription;

    @Field(type = FieldType.Date)
    private LocalDate bookPubDate;

    @Field(type = FieldType.Long)
    private long totalElements;

    @Field(type = FieldType.Keyword)
    private List<String> authorName;

    @Field(type = FieldType.Keyword)
    private List<String> categoryName;

    @Field(type = FieldType.Keyword)
    private List<String> imageUrl;

}

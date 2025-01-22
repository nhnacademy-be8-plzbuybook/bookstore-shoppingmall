package com.nhnacademy.book.book.elastic.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.math.BigDecimal;


@Getter
@Setter
@Document(indexName = "book_info_4")
public class BookInfoDocument {

    @org.springframework.data.annotation.Id
    private Long bookId; // 책 ID (book 테이블의 book_id와 연결)
    @Field(name = "book_title")
    private String bookTitle; // 책 제목 (book 테이블의 book_title)

    @Field(name = "publisher_name")
    private String publisherName; // 출판사 이름 (publisher 테이블의 publisher_name)

    @Field(name = "category_name")
    private String categoryName; // 카테고리 이름 (category 테이블의 category_name)

    @Field(name = "author_names")
    private String authorName; // 작가 이름 (author 테이블의 author_name)

    @Field(name = "selling_book_price")
    private BigDecimal sellingBookPrice; // 판매 가격 (selling_book 테이블의 selling_book_price)

    @Field(name = "book_price_standard")
    private BigDecimal bookPriceStandard; // 표준 가격 (book 테이블의 book_price_standard)

    @Field(name = "imgurl")
    private String imageUrl; // 이미지 URL
    @Field(name = "selling_book_id")
    private Long sellingBookId;




    // 생성자, Getter, Setter, ToString 등 추가
    public BookInfoDocument(Long bookId, String bookTitle, String publisherName, String categoryName,
                            String authorName, BigDecimal sellingBookPrice, BigDecimal bookPriceStandard, String imageUrl, Long sellingBookId) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.publisherName = publisherName;
        this.categoryName = categoryName;
        this.authorName = authorName;
        this.sellingBookPrice = sellingBookPrice;
        this.bookPriceStandard = bookPriceStandard;
        this.imageUrl = imageUrl;
        this.sellingBookId = sellingBookId;
    }
}


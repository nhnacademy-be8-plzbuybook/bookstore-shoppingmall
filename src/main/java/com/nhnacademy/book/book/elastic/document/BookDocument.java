package com.nhnacademy.book.book.elastic.document;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.elasticsearch.core.mapping.*;

@Document(indexName = "book")
@Getter
@Setter
@RequiredArgsConstructor
public class BookDocument {

    @org.springframework.data.annotation.Id
    private long bookId;
    private String bookTitle;
    private String bookIndex;
    private String bookDescription;
    private LocalDate bookPubDate;
    private BigDecimal bookPriceStandard;
    private String bookIsbn13;
    private Long publisherId; // Publisher의 이름을 저장하는 필드 추가
    private List<String> authors; // BookAuthor에서 Author 이름을 가져와서 리스트로 저장

    public BookDocument(Long bookId, String bookTitle, String bookDescription, LocalDate bookPubDate, BigDecimal bookPriceStandard, String bookIsbn13, Long publisherId, List<String> authors) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookDescription = bookDescription;
        this.bookPubDate = bookPubDate;
        this.bookPriceStandard = bookPriceStandard;
        this.bookIsbn13 = bookIsbn13;
        this.publisherId = publisherId;
        this.authors = authors;

    }


    // 추가적인 필드를 Elasticsearch에서 필요로 하는 형태로 추가

    @Override
    public String toString() {
        return "bookId : " + bookId + ", bookTitle : " + bookTitle + "'";
    }
}

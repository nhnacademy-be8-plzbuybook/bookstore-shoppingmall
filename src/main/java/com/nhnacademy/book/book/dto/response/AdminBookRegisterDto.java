package com.nhnacademy.book.book.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class AdminBookRegisterDto {
    private String bookTitle;          // 책 제목
    private String bookIndex;          // 목차
    private String bookDescription;    // 설명
    private LocalDate bookPubDate;     // 출판일자
    private BigDecimal bookPriceStandard; // 정가
    private String bookIsbn13;         // ISBN13
    private String publisherName;      // 출판사 이름

    // 생성자
    public AdminBookRegisterDto(String bookTitle, String bookIndex, String bookDescription,
                                LocalDate bookPubDate, BigDecimal bookPriceStandard,
                                String bookIsbn13, String publisherName) {
        this.bookTitle = bookTitle;
        this.bookIndex = bookIndex;
        this.bookDescription = bookDescription;
        this.bookPubDate = bookPubDate;
        this.bookPriceStandard = bookPriceStandard;
        this.bookIsbn13 = bookIsbn13;
        this.publisherName = publisherName;
    }

//    // Getter & Setter
//    public String getBookTitle() { return bookTitle; }
//    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
//
//    public String getBookIndex() { return bookIndex; }
//    public void setBookIndex(String bookIndex) { this.bookIndex = bookIndex; }
//
//    public String getBookDescription() { return bookDescription; }
//    public void setBookDescription(String bookDescription) { this.bookDescription = bookDescription; }
//
//    public LocalDate getBookPubDate() { return bookPubDate; }
//    public void setBookPubDate(LocalDate bookPubDate) { this.bookPubDate = bookPubDate; }
//
//    public BigDecimal getBookPriceStandard() { return bookPriceStandard; }
//    public void setBookPriceStandard(BigDecimal bookPriceStandard) { this.bookPriceStandard = bookPriceStandard; }
//
//    public String getBookIsbn13() { return bookIsbn13; }
//    public void setBookIsbn13(String bookIsbn13) { this.bookIsbn13 = bookIsbn13; }
//
//    public String getPublisherName() { return publisherName; }
//    public void setPublisherName(String publisherName) { this.publisherName = publisherName; }
}


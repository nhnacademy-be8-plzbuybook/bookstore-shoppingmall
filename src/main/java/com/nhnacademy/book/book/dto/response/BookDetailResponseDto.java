package com.nhnacademy.book.book.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Getter
@Setter
public class BookDetailResponseDto {
    private Long bookId;
    private String bookTitle;
    private String bookIndex;
    private String bookDescription;
    private LocalDate bookPubDate;
    private BigDecimal bookPriceStandard;
    private String bookIsbn13;
    private Long publisherId;
    private String imageUrl;



    public BookDetailResponseDto() {

    }


    public BookDetailResponseDto(long bookId, String bookTitle, String bookIndex, String bookDescription, LocalDate bookPubDate, BigDecimal bookPriceStandard, String bookIsbn13, Long publisherId,String imageUrl) {

        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookIndex = bookIndex;
        this.bookDescription = bookDescription;
        this.bookPubDate = bookPubDate;
        this.bookPriceStandard = bookPriceStandard;
        this.bookIsbn13 = bookIsbn13;
        this.publisherId = publisherId;
        this.imageUrl = imageUrl;

    }
}


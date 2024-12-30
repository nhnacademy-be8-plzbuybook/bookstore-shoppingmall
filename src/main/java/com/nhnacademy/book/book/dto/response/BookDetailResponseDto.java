package com.nhnacademy.book.book.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@Getter
@Setter
public class BookDetailResponseDto {
    private Long bookId;
    private Long sellingBookId;         // 판매책 ID
    private String bookTitle;           // 책 제목
    private String bookIndex;           // 목차
    private String bookDescription;     // 설명
    private LocalDate bookPubDate;      // 출판일
    private BigDecimal bookPriceStandard; // 정가
    private BigDecimal sellingPrice;    // 판매가 (추가)
    private String bookIsbn13;          // ISBN
    private Long publisherId;           // 출판사 ID
    private String publisher;           // 출판사 이름 (추가)
    private String imageUrl;            // 이미지 URL
    private List<String> categories;    // 카테고리 목록
    private List<String> authorName;    // 작가 목록
    private String status;              // 판매 상태 (추가)
    private Long likeCount;              // 좋아요 수 (추가)


//    public List<String> getCategories() {
//        return categories;
//    }
//
//    public void setCategories(List<String> categories) {
//        this.categories = categories;
//    }

    public BookDetailResponseDto() {

    }


    public BookDetailResponseDto(long bookId,  String bookTitle, String bookIndex, String bookDescription, LocalDate bookPubDate, BigDecimal bookPriceStandard, String bookIsbn13, Long publisherId,String imageUrl) {

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

    public BookDetailResponseDto(Long bookId, Long aLong, String bookTitle, String bookIndex, String bookDescription, LocalDate bookPubDate, BigDecimal bookPriceStandard, String bookIsbn13, Long publisherId, String imageUrl) {
    }
}


package com.nhnacademy.book.book.dto.response;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 관리자 페이지에서 리스트 보일때랑 수정할때 사용하는 DTO
 */
@Getter
@Setter
public class BookRegisterDto {
    private Long bookId;
    private String bookTitle; // 제목
    private LocalDate bookPubDate; // 출판일
    private String publisher; // 출판사
    private String bookIsbn13; // ISBN
    private BigDecimal standardBookPrice; // 정가
    private List<String> imageUrl; // 이미지 URL
    private List<CategorySimpleResponseDto> categories; // 카테고리 정보
    private List<AuthorResponseDto> authors; // 작가 정보

    public BookRegisterDto(
            Long bookId,
            String bookTitle,                       // 제목
            LocalDate bookPubDate,                  // 출판일
            String publisher,                       // 출판사
            String bookIsbn13,                      // ISBN
            BigDecimal standardBookPrice,            // 정가
            List<String> imageUrl,                        // 이미지 URL
            List<AuthorResponseDto> authors,
            List<CategorySimpleResponseDto> categories

    ) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookPubDate = bookPubDate;
        this.publisher = publisher;
        this.bookIsbn13 = bookIsbn13;
        this.standardBookPrice = standardBookPrice;
        this.imageUrl = imageUrl;
        this.authors = authors;
        this.categories = categories;

    }

}


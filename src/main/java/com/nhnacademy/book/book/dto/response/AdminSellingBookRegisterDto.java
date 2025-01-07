package com.nhnacademy.book.book.dto.response;
import com.nhnacademy.book.book.entity.SellingBook;
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
public class AdminSellingBookRegisterDto {
    private Long sellingBookId; // 판매도서 ID
    private String bookTitle; // 제목
    private LocalDate bookPubDate; // 출판일
    private String publisher; // 출판사
    private String bookIsbn13; // ISBN
    private BigDecimal sellingBookPrice; // 판매가
    private Boolean sellingBookPackageable; // 포장 가능 여부
    private Integer sellingBookStock; // 재고
    private SellingBook.SellingBookStatus sellingBookStatus; // 판매 상태
    private Long sellingBookViewCount; // 조회수
    private String imageUrl; // 이미지 URL
    private List<String> categories; // 카테고리 정보
    private List<String> authors; // 작가 정보

    public AdminSellingBookRegisterDto(
            Long sellingBookId,                     // 판매도서 ID
            String bookTitle,                       // 제목
            LocalDate bookPubDate,                  // 출판일
            String publisher,                       // 출판사
            String bookIsbn13,                      // ISBN
            BigDecimal sellingBookPrice,            // 판매가
            Boolean sellingBookPackageable,         // 포장 가능 여부
            Integer sellingBookStock,               // 재고
            SellingBook.SellingBookStatus sellingBookStatus, // 판매 상태
            Long sellingBookViewCount,              // 조회수
            String imageUrl,                        // 이미지 URL
            List<String> categories,                // 카테고리 정보
            List<String> authors                    // 작가 정보
    ) {
        this.sellingBookId = sellingBookId;
        this.bookTitle = bookTitle;
        this.bookPubDate = bookPubDate;
        this.publisher = publisher;
        this.bookIsbn13 = bookIsbn13;
        this.sellingBookPrice = sellingBookPrice;
        this.sellingBookPackageable = sellingBookPackageable;
        this.sellingBookStock = sellingBookStock;
        this.sellingBookStatus = sellingBookStatus;
        this.sellingBookViewCount = sellingBookViewCount;
        this.imageUrl = imageUrl;
        this.categories = categories;
        this.authors = authors;
    }

}


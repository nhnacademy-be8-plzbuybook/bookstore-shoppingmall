package com.nhnacademy.book.book.dto.response;

import com.nhnacademy.book.book.entity.SellingBook;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class AdminBookAndSellingBookRegisterDto {

    private Long sellingBookId;
    private String bookTitle; // 제목
    private String bookIsbn13; // ISBN
    private String publisher; // 출판사 - 가져오기 , 적어서 넘기기
    private LocalDate bookPubDate; // 출판일
    private String bookIndex; //목차
    private String bookDescription; // 설명
    private BigDecimal bookPriceStandard; //정가
    private BigDecimal sellingBookPrice; // 판매가 - 가져오기 판매책 테이블
    private Boolean sellingBookPackageable; // 포장 가능 여부 - 가져오기 판매책 테이블


    private Integer sellingBookStock; // 재고 - 가져오기 판매책 테이블
    private String imageUrl; // 이미지 URL
    private String sellingBookStatus; // "SELLING", "SELLEND", "DELETEBOOK" 등의 상태
    private List<String> categories; // 카테고리 정보
    private List<String> authors; // 작가 정보

    public AdminBookAndSellingBookRegisterDto(Long sellingBookId, String bookTitle,
                                              String bookIsbn13, LocalDate bookPubDate,
                                              String bookIndex, String bookDescription, BigDecimal bookPriceStandard,
                                              BigDecimal sellingBookPrice, Boolean sellingBookPackageable,
                                              Object publisher, Integer sellingBookStock, String imageUrl,
                                              SellingBook.SellingBookStatus sellingBookStatus,
                                              List<String> categoryNames, List<String> authorNames) {
        this.sellingBookId = sellingBookId;
        this.bookTitle = bookTitle;
        this.bookIsbn13 = bookIsbn13;
        this.publisher = publisher != null ? publisher.toString() : null;
        this.bookPubDate = bookPubDate;
        this.bookIndex = bookIndex;
        this.bookDescription = bookDescription;
        this.bookPriceStandard = bookPriceStandard;
        this.sellingBookPrice = sellingBookPrice;
        this.sellingBookPackageable = sellingBookPackageable;
        this.sellingBookStock = sellingBookStock;
        this.imageUrl = imageUrl;
        this.sellingBookStatus = sellingBookStatus != null ? sellingBookStatus.name() : null;
        this.categories = categoryNames;
        this.authors = authorNames;
    }

}

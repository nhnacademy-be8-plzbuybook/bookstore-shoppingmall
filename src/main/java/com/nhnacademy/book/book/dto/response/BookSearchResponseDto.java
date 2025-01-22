package com.nhnacademy.book.book.dto.response;

import com.nhnacademy.book.book.entity.SellingBook;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class BookSearchResponseDto {

    private Long bookId;
    private String bookTitle;
    private String bookIsbn13;
    private BigDecimal bookPriceStandard;
    private Long sellingBookId;
    private BigDecimal sellingBookPrice;
    private Integer sellingBookStock;
    private SellingBook.SellingBookStatus sellingBookStatus;
    private String bookIndex;
    private String bookDescription;
    private LocalDate bookPubDate;
    private long totalElements;  // 전체 검색 결과 수

    // 추가된 필드
    private List<String> authorName;
    private List<String> categoryName;
    private List<String> imageUrl;

    // 생성자, getter, setter
    public BookSearchResponseDto(Long bookId, String bookTitle, String bookIsbn13, BigDecimal bookPriceStandard,
                                 Long sellingBookId, BigDecimal sellingBookPrice, Integer sellingBookStock,
                                 SellingBook.SellingBookStatus sellingBookStatus, List<String> author, List<String> category, List<String> image,
                                 String bookIndex, String bookDescription, LocalDate bookPubDate) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookIsbn13 = bookIsbn13;
        this.bookPriceStandard = bookPriceStandard;
        this.sellingBookId = sellingBookId;
        this.sellingBookPrice = sellingBookPrice;
        this.sellingBookStock = sellingBookStock;
        this.sellingBookStatus = sellingBookStatus;
        this.authorName = author;
        this.categoryName = category;
        this.imageUrl = image;
        this.bookIndex = bookIndex;
        this.bookDescription = bookDescription;
        this.bookPubDate = bookPubDate;

    }

    // getter, setter 생략
}

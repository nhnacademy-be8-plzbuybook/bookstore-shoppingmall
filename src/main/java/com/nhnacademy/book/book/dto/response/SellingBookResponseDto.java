package com.nhnacademy.book.book.dto.response;

import com.nhnacademy.book.book.entity.SellingBook.SellingBookStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SellingBookResponseDto {

    private Long sellingBookId;
    private Long bookId;
    private String bookTitle;
    private BigDecimal sellingBookPrice;
    private Boolean sellingBookPackageable;
    private Integer sellingBookStock;
    private SellingBookStatus sellingBookStatus;
    private Boolean used;
    private Long sellingBookViewCount;
    private String imageUrl;
    private String publisher;        // 출판사 정보
    private List<String> categories; // 카테고리 정보
    private List<String> authors;    // 작가 정보
}

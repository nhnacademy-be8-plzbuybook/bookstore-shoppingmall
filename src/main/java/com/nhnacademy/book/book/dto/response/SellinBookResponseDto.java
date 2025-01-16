package com.nhnacademy.book.book.dto.response;

import com.nhnacademy.book.book.entity.SellingBook;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
@Getter
@Setter
public class SellinBookResponseDto {

    private Long sellingBookId;
    private Long bookId;
    private BigDecimal sellingBookPrice;
    private Boolean sellingBookPackageable;
    private Integer sellingBookStock;
    private SellingBook.SellingBookStatus sellingBookStatus;
    private Boolean used;
    private Long sellingBookViewCount;

}

package com.nhnacademy.book.book.dto.response;

import com.nhnacademy.book.book.entity.SellingBook.SellingBookStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Getter
@Setter
public class SellingBookResponseDto {

    private Long sellingBookId;
    private Long bookId;
    private BigDecimal sellingBookPrice;
    private Boolean sellingBookPackageable;
    private Integer sellingBookStock;
    private SellingBookStatus sellingBookStatus;
    private Boolean used;
    private Long sellingBookViewCount;
    private String imageUrl;
}

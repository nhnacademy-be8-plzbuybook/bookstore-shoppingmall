package com.nhnacademy.book.book.dto.request;

import com.nhnacademy.book.book.entity.SellingBook.SellingBookStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data

@Getter
@Setter
public class SellingBookRegisterDto {
    private Long bookId; // 책 ID
    private BigDecimal price; // 판매가
    private Boolean packageable; // 선물 포장 가능 여부
    private Integer stock; // 재고
    private SellingBookStatus status;
    private Boolean used; // 중고 여부
}

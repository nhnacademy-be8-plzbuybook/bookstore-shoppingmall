package com.nhnacademy.book.cartbook.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ReadCartBookResponseDto(
        Long cartBookId,          // 장바구니 항목 ID (SellingBook.sellingBookId)
        Long sellingBookId,              // 책 ID (Book.bookId)
        String bookTitle,         // 책 제목 (Book.bookTitle)
        BigDecimal sellingBookPrice, // 책 가격 (SellingBook.sellingBookPrice)
        String imageUrl,          // 책 이미지 URL (BookImage.imageUrl)
        int quantity,             // 장바구니에 담은 수량 (추가 정보)
        int sellingBookStock ) {}     // 재고 수량 (SellingBook.sellingBookStock)


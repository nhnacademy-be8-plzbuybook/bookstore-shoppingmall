package com.nhnacademy.book.cartbook.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ReadAllMemberCartBookResponseDto(
        Long cartBookId, // 장바구니 항목 ID
        Long sellingBookId, // 판매 책 id
        String bookTitle, // 판매 책 제목
        BigDecimal sellingBookPrice, // 책 가격
        String imageUrl, // 책 이미지 url
        int quantity, // 장바구니에 담을 수량
        int sellingBookStock // 판매책의 남은 재고
) {}

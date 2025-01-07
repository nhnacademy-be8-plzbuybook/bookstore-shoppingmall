package com.nhnacademy.book.cartbook.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record  ReadMemberCartBookResponseDto(
                Long cartBookId,
                Long sellingBookId,
                BigDecimal sellingBookPrice,
                String url,
                String title,
                int quantity,
                int sellingBookStock,
                boolean used) {}
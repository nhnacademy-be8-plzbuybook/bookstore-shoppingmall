package com.nhnacademy.book.wrappingPaper.dto;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public record WrappingPaperUpdateRequestDto(String name, BigDecimal price, Long stock, String imagePath) {
}

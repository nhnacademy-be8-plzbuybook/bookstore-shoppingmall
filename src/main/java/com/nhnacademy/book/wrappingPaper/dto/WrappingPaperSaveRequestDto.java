package com.nhnacademy.book.wrappingPaper.dto;

import com.nhnacademy.book.wrappingPaper.entity.WrappingPaper;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class WrappingPaperSaveRequestDto{
    @NotBlank
    private String name;

    @NotNull @Min(0)
    private BigDecimal price;

    @NotNull @Min(0)
    private Long stock;

    public WrappingPaper toEntity(String imagePath) {
        return new WrappingPaper(name, price, stock, LocalDateTime.now(), imagePath);
    }
}

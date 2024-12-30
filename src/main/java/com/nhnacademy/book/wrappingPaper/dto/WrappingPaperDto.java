package com.nhnacademy.book.wrappingPaper.dto;

import com.nhnacademy.book.wrappingPaper.entity.WrappingPaper;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class WrappingPaperDto {
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long stock;
    private final String imagePath;

    public WrappingPaperDto(WrappingPaper entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.price = entity.getPrice();
        this.stock = entity.getStock();
        this.imagePath = entity.getImagePath();
    }

}

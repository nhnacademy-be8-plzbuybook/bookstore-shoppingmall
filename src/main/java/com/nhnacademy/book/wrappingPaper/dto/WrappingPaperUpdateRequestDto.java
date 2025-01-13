package com.nhnacademy.book.wrappingPaper.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public record WrappingPaperUpdateRequestDto(@NotBlank String name,
                                            @NotNull BigDecimal price,
                                            @NotNull Long stock,
                                            @Nullable MultipartFile imageFile) {
}

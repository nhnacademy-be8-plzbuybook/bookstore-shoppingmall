package com.nhnacademy.book.order.dto;

import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderProductStatusPatchRequestDto {
    @NotBlank
    private OrderProductStatus status;
}

package com.nhnacademy.book.order.dto;

import com.nhnacademy.book.order.entity.OrderDeliveryAddress;
import com.nhnacademy.book.order.entity.Orders;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class OrderDeliveryAddressSaveRequestDto {
    @NotBlank
    private String locationAddress;

    @NotBlank
    private String zipCode;

    @NotBlank
    private String detailAddress;

    @NotBlank
    private String recipient;

    @NotBlank
    private String recipientPhone;

    public OrderDeliveryAddressSaveRequestDto(String locationAddress, String zipCode, String detailAddress, String recipient, String recipientPhone) {
        this.locationAddress = locationAddress;
        this.zipCode = zipCode;
        this.detailAddress = detailAddress;
        this.recipient = recipient;
        this.recipientPhone = recipientPhone;
    }

    public OrderDeliveryAddress toEntity(Orders order) {
        return new OrderDeliveryAddress(locationAddress, zipCode, detailAddress, recipient, recipientPhone, order);
    }
}

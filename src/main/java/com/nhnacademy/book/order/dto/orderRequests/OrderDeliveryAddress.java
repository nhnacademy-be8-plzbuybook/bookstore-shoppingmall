package com.nhnacademy.book.order.dto.orderRequests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class OrderDeliveryAddress {
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

    public OrderDeliveryAddress(String locationAddress, String zipCode, String detailAddress, String recipient, String recipientPhone) {
        this.locationAddress = locationAddress;
        this.zipCode = zipCode;
        this.detailAddress = detailAddress;
        this.recipient = recipient;
        this.recipientPhone = recipientPhone;
    }
}

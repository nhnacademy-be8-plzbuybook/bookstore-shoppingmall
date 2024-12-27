package com.nhnacademy.book.deliveryFeePolicy.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public record DeliveryFeePolicySaveResponseDto(@JsonProperty("policyId") long id) {
}

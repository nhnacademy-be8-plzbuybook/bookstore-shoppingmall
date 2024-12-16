package com.nhnacademy.book.deliveryFeePolicy.service;

import com.nhnacademy.book.deliveryFeePolicy.dto.DeliveryFeeCalculateRequestDto;
import com.nhnacademy.book.deliveryFeePolicy.dto.DeliveryFeePolicySaveRequestDto;
import com.nhnacademy.book.deliveryFeePolicy.dto.DeliveryFeePolicyUpdateRequestDto;
import com.nhnacademy.book.deliveryFeePolicy.entity.DeliveryFeePolicy;

import java.math.BigDecimal;

public interface DeliveryFeePolicyService {
    long createDeliveryFeePolicy(DeliveryFeePolicySaveRequestDto saveRequest);
    DeliveryFeePolicy getDeliveryFeePolicy(long id);
    long modifyDeliveryFeePolicy(long id, DeliveryFeePolicyUpdateRequestDto updateRequest);
    void removeDeliveryFeePolicy(long id);
    // 결제금액 기반으로 지불할 배송비 반환
    BigDecimal getCalculatedDeliveryFee(Long id, DeliveryFeeCalculateRequestDto calculateRequest);
}

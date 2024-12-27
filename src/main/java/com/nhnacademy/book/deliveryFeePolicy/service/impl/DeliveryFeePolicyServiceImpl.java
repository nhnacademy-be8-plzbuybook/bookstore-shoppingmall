package com.nhnacademy.book.deliveryFeePolicy.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.dto.DeliveryFeeCalculateRequestDto;
import com.nhnacademy.book.deliveryFeePolicy.dto.DeliveryFeePolicySaveRequestDto;
import com.nhnacademy.book.deliveryFeePolicy.dto.DeliveryFeePolicyUpdateRequestDto;
import com.nhnacademy.book.deliveryFeePolicy.entity.DeliveryFeePolicy;
import com.nhnacademy.book.deliveryFeePolicy.exception.ConflictException;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.deliveryFeePolicy.repository.DeliveryFeePolicyRepository;
import com.nhnacademy.book.deliveryFeePolicy.service.DeliveryFeePolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DeliveryFeePolicyServiceImpl implements DeliveryFeePolicyService {
    private final DeliveryFeePolicyRepository deliveryFeePolicyRepository;

    @Override
    public long createDeliveryFeePolicy(DeliveryFeePolicySaveRequestDto saveRequest) {
        DeliveryFeePolicy deliveryFeePolicy = saveRequest.toEntity();

        boolean isDuplicated = deliveryFeePolicyRepository.existsByDefaultDeliveryFeeAndFreeDeliveryThreshold(
                deliveryFeePolicy.getDefaultDeliveryFee(), deliveryFeePolicy.getFreeDeliveryThreshold());
        if (isDuplicated) {
            throw new ConflictException("duplicated delivery fee policy");
        }

        DeliveryFeePolicy savedPolicy = deliveryFeePolicyRepository.save(deliveryFeePolicy);
        return savedPolicy.getId();
    }

    @Override
    public DeliveryFeePolicy getDeliveryFeePolicy(long id) {
        Optional<DeliveryFeePolicy> optionalPolicy = deliveryFeePolicyRepository.findById(id);
        return optionalPolicy.orElseThrow(() -> new NotFoundException(id + " policy not found!"));
    }

    @Override
    public long modifyDeliveryFeePolicy(long id, DeliveryFeePolicyUpdateRequestDto updateRequest) {
        Optional<DeliveryFeePolicy> optionalPolicy = deliveryFeePolicyRepository.findById(id);

        if (optionalPolicy.isEmpty()) {
            throw new NotFoundException(id + " policy not found!");
        }
        DeliveryFeePolicy deliveryFeePolicy = optionalPolicy.get();

        deliveryFeePolicy.update(updateRequest.defaultDeliveryFee(), updateRequest.freeDeliveryThreshold());
        return deliveryFeePolicy.getId();
    }

    @Override
    public void removeDeliveryFeePolicy(long id) {
        Optional<DeliveryFeePolicy> optionalPolicy = deliveryFeePolicyRepository.findById(id);

        if (optionalPolicy.isEmpty()) {
            throw new NotFoundException(id + " policy not found!");
        }
        DeliveryFeePolicy deliveryFeePolicy = optionalPolicy.get();

        deliveryFeePolicyRepository.delete(deliveryFeePolicy);
    }

    @Override
    public BigDecimal getCalculatedDeliveryFee(Long id, DeliveryFeeCalculateRequestDto calculateRequest) {
        Optional<DeliveryFeePolicy> optionalPolicy = deliveryFeePolicyRepository.findById(id);
        BigDecimal price = calculateRequest.price();

        if (optionalPolicy.isEmpty()) {
            throw new NotFoundException(id + " policy not found!");
        }
        DeliveryFeePolicy deliveryFeePolicy = optionalPolicy.get();

        BigDecimal freeThreshold = deliveryFeePolicy.getFreeDeliveryThreshold();
        // 주문금액이 무료배송기준 이상일 때
        if (price.compareTo(freeThreshold) >= 0) {
            return BigDecimal.ZERO;
        }
        return deliveryFeePolicy.getDefaultDeliveryFee();
    }
}

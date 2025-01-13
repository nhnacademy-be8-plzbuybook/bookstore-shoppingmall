package com.nhnacademy.book.deliveryFeePolicy.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.dto.DeliveryFeeCalculateRequestDto;
import com.nhnacademy.book.deliveryFeePolicy.dto.DeliveryFeePolicyDto;
import com.nhnacademy.book.deliveryFeePolicy.dto.DeliveryFeePolicySaveRequestDto;
import com.nhnacademy.book.deliveryFeePolicy.dto.DeliveryFeePolicyUpdateRequestDto;
import com.nhnacademy.book.deliveryFeePolicy.entity.DeliveryFeePolicy;
import com.nhnacademy.book.deliveryFeePolicy.exception.ConflictException;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.deliveryFeePolicy.repository.DeliveryFeePolicyRepository;
import com.nhnacademy.book.deliveryFeePolicy.service.DeliveryFeePolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DeliveryFeePolicyServiceImpl implements DeliveryFeePolicyService {
    private final DeliveryFeePolicyRepository deliveryFeePolicyRepository;

    @Override
    public long createDeliveryFeePolicy(DeliveryFeePolicySaveRequestDto saveRequest) {
        if (isPolicyExists(saveRequest.getName())) {
            throw new ConflictException("이미 존재하는 배송비정책입니다.");
        }
        DeliveryFeePolicy savedPolicy = deliveryFeePolicyRepository.save(saveRequest.toEntity());
        return savedPolicy.getId();
    }

    @Override
    public List<DeliveryFeePolicyDto> getDeliveryPolicies() {
        List<DeliveryFeePolicy> policies = deliveryFeePolicyRepository.findAll();
        return policies.stream().map(p ->
                new DeliveryFeePolicyDto(p.getId(), p.getName(), p.getDefaultDeliveryFee(), p.getFreeDeliveryThreshold())).toList();
    }

    @Override
    public DeliveryFeePolicyDto getDeliveryFeePolicy(long id) {
        DeliveryFeePolicy policy = deliveryFeePolicyRepository.findById(id).orElseThrow(()
                -> new NotFoundException(id + " policy not found!"));
        return new DeliveryFeePolicyDto(policy.getId(), policy.getName(), policy.getDefaultDeliveryFee(), policy.getFreeDeliveryThreshold());
    }

    @Transactional
    @Override
    public long modifyDeliveryFeePolicy(long id, DeliveryFeePolicyUpdateRequestDto updateRequest) {
        DeliveryFeePolicy policy = deliveryFeePolicyRepository.findById(id).orElseThrow(()
                -> new NotFoundException(id + " policy not found!"));

        policy.update(updateRequest.name(), updateRequest.defaultDeliveryFee(), updateRequest.freeDeliveryThreshold());
        return policy.getId();
    }

    @Override
    public void removeDeliveryFeePolicy(long id) {
        if (isPolicyExists(id)) {
            throw new NotFoundException(id + " policy not found!");
        }
        deliveryFeePolicyRepository.deleteById(id);
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

    private boolean isPolicyExists(Long id) {
        return deliveryFeePolicyRepository.existsById(id);
    }

    private boolean isPolicyExists(String name) {
        return deliveryFeePolicyRepository.existsByName(name);
    }
}

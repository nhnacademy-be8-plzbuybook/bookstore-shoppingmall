package com.nhnacademy.book.deliveryFeePolicy.repository;

import com.nhnacademy.book.deliveryFeePolicy.dto.DeliveryFeePolicyDto;
import com.nhnacademy.book.deliveryFeePolicy.entity.DeliveryFeePolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface DeliveryFeePolicyRepository extends JpaRepository<DeliveryFeePolicy, Long> {
    boolean existsByDefaultDeliveryFeeAndFreeDeliveryThreshold(BigDecimal defaultDeliveryFee, BigDecimal freeDeliveryThreshold);
    boolean existsByName(String name);
}

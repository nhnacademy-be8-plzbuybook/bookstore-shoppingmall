package com.nhnacademy.book.deliveryFeePolicy.repository;

import com.nhnacademy.book.deliveryFeePolicy.entity.DeliveryFeePolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryFeePolicyRepository extends JpaRepository<DeliveryFeePolicy, Long> {
    boolean existsByName(String name);
}

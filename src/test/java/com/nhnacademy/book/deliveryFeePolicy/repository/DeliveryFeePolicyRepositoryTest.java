package com.nhnacademy.book.deliveryFeePolicy.repository;//package com.nhnacademy.book.deliveryFeePolicy.repository;
//
//import com.nhnacademy.book.deliveryFeePolicy.dto.DeliveryFeePolicyUpdateRequestDto;
//import com.nhnacademy.book.deliveryFeePolicy.entity.DeliveryFeePolicy;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.math.BigDecimal;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@DataJpaTest
//class DeliveryFeePolicyRepositoryTest {
//    @Autowired
//    private DeliveryFeePolicyRepository deliveryFeePolicyRepository;
//
//    @BeforeEach
//    void setup() {
//        DeliveryFeePolicy deliveryFeePolicy = new DeliveryFeePolicy(1L,new BigDecimal("3000"), new BigDecimal("30000"));
//        deliveryFeePolicyRepository.save(deliveryFeePolicy);
//    }
//
//    @Test
//    void findById() {
//        Optional<DeliveryFeePolicy> optionalResult = deliveryFeePolicyRepository.findById(1L);
//
//        assertTrue(optionalResult.isPresent());
//
//        DeliveryFeePolicy result = optionalResult.get();
//        assertEquals(3000, result.getDefaultDeliveryFee());
//        assertEquals(30_000, result.getFreeDeliveryThreshold());
//    }
//
//    @Test
//    void save() {
//        BigDecimal defaultDeliveryFee = new BigDecimal(3000) ;
//        BigDecimal freeDeliveryThreshold = new BigDecimal(30000);
//        DeliveryFeePolicy deliveryFeePolicy = new DeliveryFeePolicy(2L,defaultDeliveryFee, freeDeliveryThreshold);
//        DeliveryFeePolicy result = deliveryFeePolicyRepository.save(deliveryFeePolicy);
//
//        assertEquals(defaultDeliveryFee, result.getDefaultDeliveryFee());
//        assertEquals(freeDeliveryThreshold, result.getFreeDeliveryThreshold());
//    }
//
//    @Test
//    void update() {
//        DeliveryFeePolicy target = deliveryFeePolicyRepository.findById(1L).get();
//        DeliveryFeePolicyUpdateRequestDto updateRequest = new DeliveryFeePolicyUpdateRequestDto(new BigDecimal("4000"), new BigDecimal("40000"));
//
//        target.update(updateRequest);
//
//        DeliveryFeePolicy result = deliveryFeePolicyRepository.findById(1L).get();
//        assertEquals(updateRequest.defaultDeliveryFee(), result.getDefaultDeliveryFee());
//        assertEquals(updateRequest.freeDeliveryThreshold(), result.getFreeDeliveryThreshold());
//    }
//
//    @Test
//    void delete() {
//        deliveryFeePolicyRepository.deleteById(1L);
//
//        Optional<DeliveryFeePolicy> result = deliveryFeePolicyRepository.findById(1L);
//        assertTrue(result.isEmpty());
//    }
//
//}

import com.nhnacademy.book.deliveryFeePolicy.entity.DeliveryFeePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class DeliveryFeePolicyRepositoryTest {
    @Autowired
    private DeliveryFeePolicyRepository deliveryFeePolicyRepository;

    private static final BigDecimal INITIAL_DEFAULT_DELIVERY_FEE = new BigDecimal("3000");
    private static final BigDecimal INITIAL_FREE_DELIVERY_THRESHOLD = new BigDecimal("30000");
    private static final BigDecimal UPDATED_DEFAULT_DELIVERY_FEE = new BigDecimal("4000");
    private static final BigDecimal UPDATED_FREE_DELIVERY_THRESHOLD = new BigDecimal("40000");
    private DeliveryFeePolicy existingDeliveryFeePolicy;

    @BeforeEach
    void setup() {
        DeliveryFeePolicy deliveryFeePolicy = new DeliveryFeePolicy(1L, INITIAL_DEFAULT_DELIVERY_FEE, INITIAL_FREE_DELIVERY_THRESHOLD);
        existingDeliveryFeePolicy = deliveryFeePolicyRepository.save(deliveryFeePolicy);
    }

    @Test
    void findById() {
        long id = existingDeliveryFeePolicy.getId();
        Optional<DeliveryFeePolicy> optionalResult = deliveryFeePolicyRepository.findById(id);
        assertTrue(optionalResult.isPresent());

        DeliveryFeePolicy result = optionalResult.get();
        assertEquals(0, INITIAL_DEFAULT_DELIVERY_FEE.compareTo(result.getDefaultDeliveryFee()));
        assertEquals(0, INITIAL_FREE_DELIVERY_THRESHOLD.compareTo(result.getFreeDeliveryThreshold()));
    }

    @Test
    void save() {
        BigDecimal defaultDeliveryFee = new BigDecimal("3000");
        BigDecimal freeDeliveryThreshold = new BigDecimal("30000");
        DeliveryFeePolicy deliveryFeePolicy = new DeliveryFeePolicy(defaultDeliveryFee, freeDeliveryThreshold);
        DeliveryFeePolicy result = deliveryFeePolicyRepository.save(deliveryFeePolicy);

        assertEquals(0, defaultDeliveryFee.compareTo(result.getDefaultDeliveryFee()));
        assertEquals(0, freeDeliveryThreshold.compareTo(result.getFreeDeliveryThreshold()));
    }

    @Test
    void update() {
        long existingId = existingDeliveryFeePolicy.getId();
        DeliveryFeePolicy target = deliveryFeePolicyRepository.findById(existingId).get();
        target.update(UPDATED_DEFAULT_DELIVERY_FEE, UPDATED_FREE_DELIVERY_THRESHOLD);

        deliveryFeePolicyRepository.save(target);

        DeliveryFeePolicy result = deliveryFeePolicyRepository.findById(existingId).get();
        assertEquals(0, UPDATED_DEFAULT_DELIVERY_FEE.compareTo(result.getDefaultDeliveryFee()));
        assertEquals(0, UPDATED_FREE_DELIVERY_THRESHOLD.compareTo(result.getFreeDeliveryThreshold()));
    }

    @Test
    void delete() {
        deliveryFeePolicyRepository.deleteById(1L);

        Optional<DeliveryFeePolicy> result = deliveryFeePolicyRepository.findById(1L);
        assertTrue(result.isEmpty());
    }
}
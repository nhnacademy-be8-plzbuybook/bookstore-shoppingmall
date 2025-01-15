package com.nhnacademy.book.deliveryFeePolicy.repository;//package com.nhnacademy.book.deliveryFeePolicy.repository;

import com.nhnacademy.book.config.JpaRepositoryConfig;
import com.nhnacademy.book.deliveryFeePolicy.entity.DeliveryFeePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(JpaRepositoryConfig.class)
class DeliveryFeePolicyRepositoryTest {
    @Autowired
    private DeliveryFeePolicyRepository deliveryFeePolicyRepository;
    @Autowired
    private TestEntityManager entityManager;

    private static final String INITIAL_POLICY_NAME = "배송정책1";
    private static final BigDecimal INITIAL_DEFAULT_DELIVERY_FEE = new BigDecimal("3000");
    private static final BigDecimal INITIAL_FREE_DELIVERY_THRESHOLD = new BigDecimal("30000");
    private static final String UPDATED_POLICY_NAME = "배송정책2";
    private static final BigDecimal UPDATED_DEFAULT_DELIVERY_FEE = new BigDecimal("4000");
    private static final BigDecimal UPDATED_FREE_DELIVERY_THRESHOLD = new BigDecimal("40000");
    private DeliveryFeePolicy existingDeliveryFeePolicy;

    @BeforeEach
    void setup() {
        DeliveryFeePolicy deliveryFeePolicy = new DeliveryFeePolicy(1L, INITIAL_POLICY_NAME, INITIAL_DEFAULT_DELIVERY_FEE, INITIAL_FREE_DELIVERY_THRESHOLD);
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
        String name = "배송비정책저장테스트";
        BigDecimal defaultDeliveryFee = new BigDecimal("3000");
        BigDecimal freeDeliveryThreshold = new BigDecimal("30000");
        DeliveryFeePolicy deliveryFeePolicy = new DeliveryFeePolicy(name, defaultDeliveryFee, freeDeliveryThreshold);
        DeliveryFeePolicy result = deliveryFeePolicyRepository.save(deliveryFeePolicy);

        entityManager.flush();
        entityManager.clear();

        Optional<DeliveryFeePolicy> optionalFoundPolicy = deliveryFeePolicyRepository.findById(result.getId());

        assertTrue(optionalFoundPolicy.isPresent());
        DeliveryFeePolicy foundPolicy = optionalFoundPolicy.get();

        assertEquals(name, foundPolicy.getName());
        assertEquals(0, defaultDeliveryFee.compareTo(foundPolicy.getDefaultDeliveryFee()));
        assertEquals(0, freeDeliveryThreshold.compareTo(foundPolicy.getFreeDeliveryThreshold()));
    }

    @DisplayName("배송비정책 저장 - 정책이름 유니크")
    @Test
    void save_duplicated() {
        String name = "배송정책1";
        BigDecimal defaultDeliveryFee = new BigDecimal("3000");
        BigDecimal freeDeliveryThreshold = new BigDecimal("30000");
        DeliveryFeePolicy deliveryFeePolicy = new DeliveryFeePolicy(name, defaultDeliveryFee, freeDeliveryThreshold);

        assertThrows(DataIntegrityViolationException.class, () -> deliveryFeePolicyRepository.save(deliveryFeePolicy));
    }

    @Test
    void update() {
        long existingId = existingDeliveryFeePolicy.getId();
        DeliveryFeePolicy target = deliveryFeePolicyRepository.findById(existingId).get();
        target.update(UPDATED_POLICY_NAME, UPDATED_DEFAULT_DELIVERY_FEE, UPDATED_FREE_DELIVERY_THRESHOLD);

        entityManager.flush();
        entityManager.clear();

        Optional<DeliveryFeePolicy> optionalResult = deliveryFeePolicyRepository.findById(existingId);


        assertTrue(optionalResult.isPresent());
        DeliveryFeePolicy result = optionalResult.get();
        assertEquals(0, UPDATED_DEFAULT_DELIVERY_FEE.compareTo(result.getDefaultDeliveryFee()));
        assertEquals(0, UPDATED_FREE_DELIVERY_THRESHOLD.compareTo(result.getFreeDeliveryThreshold()));
    }

    @Test
    void delete() {
        deliveryFeePolicyRepository.deleteById(1L);

        entityManager.flush();
        entityManager.clear();

        Optional<DeliveryFeePolicy> result = deliveryFeePolicyRepository.findById(1L);
        assertTrue(result.isEmpty());
    }
}
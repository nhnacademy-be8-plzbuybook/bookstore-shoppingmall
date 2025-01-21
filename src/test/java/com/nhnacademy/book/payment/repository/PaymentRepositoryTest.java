package com.nhnacademy.book.payment.repository;

import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.payment.entity.Payment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@DataJpaTest
class PaymentRepositoryTest {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TestEntityManager entityManager;
    private Orders savedOrder;

    @BeforeEach
    void setup() {
        savedOrder = orderRepository.save(Orders.builder()
                .id("orderId")
                .number("orderNumber")
                .name("orderName")
                .deliveryWishDate(LocalDate.now())
                .usedPoint(1000)
                .deliveryFee(BigDecimal.valueOf(3_000))
                .orderedAt(LocalDateTime.now())
                .status(OrderStatus.PAYMENT_COMPLETED)
                .orderPrice(BigDecimal.valueOf(10_000))
                .build());
    }


    @Test
    void create_DONE() {
        String status = "DONE";
        String paymentKey = "paymentKey";
        LocalDateTime recordedAt = LocalDateTime.now();
        BigDecimal amount = BigDecimal.valueOf(1000);
        String method = "간편결제";
        String easyPayProvider = "토스페이";

        Payment savedPayment = paymentRepository.save(Payment.builder()
                .id(1L)
                .status(status)
                .paymentKey(paymentKey)
                .recordedAt(recordedAt)
                .amount(amount)
                .method(method)
                .easyPayProvider(easyPayProvider)
                .orders(savedOrder)
                .build());

        entityManager.flush();
        entityManager.clear();

        Assertions.assertNotNull(savedPayment);
        Assertions.assertEquals(status, savedPayment.getStatus());
        Assertions.assertEquals(recordedAt, savedPayment.getRecordedAt());
    }

    @Test
    void create_CANCEL() {
        String status = "CANCEL";
        String paymentKey = "paymentKey";
        LocalDateTime recordedAt = LocalDateTime.now();
        BigDecimal amount = BigDecimal.valueOf(1000);
        String method = "간편결제";
        String easyPayProvider = "토스페이";

        Payment savedPayment = paymentRepository.save(Payment.builder()
                .status(status)
                .paymentKey(paymentKey)
                .recordedAt(recordedAt)
                .amount(amount)
                .method(method)
                .easyPayProvider(easyPayProvider)
                .orders(savedOrder)
                .build());

        entityManager.flush();
        entityManager.clear();

        Assertions.assertNotNull(savedPayment);
        Assertions.assertEquals(status, savedPayment.getStatus());
        Assertions.assertEquals(recordedAt, savedPayment.getRecordedAt());

    }

    @Test
    void findOldestByOrderId() {
        String status = "DONE";
        String paymentKey = "paymentKey";

        LocalDateTime oldestRecordedAt = LocalDateTime.now();
        BigDecimal oldestAmount = BigDecimal.valueOf(1000);
        String method = "간편결제";
        String easyPayProvider = "토스페이";

        Payment oldestPayment = paymentRepository.save(Payment.builder()
                .status(status)
                .paymentKey(paymentKey)
                .recordedAt(oldestRecordedAt)
                .amount(oldestAmount)
                .method(method)
                .easyPayProvider(easyPayProvider)
                .orders(savedOrder)
                .build());

        for (int i = 1; i < 10; i++) {
            paymentRepository.save(Payment.builder()
                    .status("CANCELED")
                    .paymentKey(paymentKey)
                    .recordedAt(oldestRecordedAt)
                    .amount(BigDecimal.valueOf(i))
                    .method(method)
                    .easyPayProvider(easyPayProvider)
                    .orders(savedOrder)
                    .build());
        }
        entityManager.flush();
        entityManager.clear();

        Optional<Payment> optionalPayment = paymentRepository.findOldestByOrderId(savedOrder.getId());

        assertTrue(optionalPayment.isPresent());
        Payment foundPayment = optionalPayment.get();
        assertEquals(0, oldestAmount.compareTo(foundPayment.getAmount()));
        assertEquals(
                oldestRecordedAt.truncatedTo(ChronoUnit.MILLIS),
                foundPayment.getRecordedAt().truncatedTo(ChronoUnit.MILLIS)
        );
    }
}
package com.nhnacademy.book.payment.repository;


import com.nhnacademy.book.payment.entity.Payment;
import jakarta.ws.rs.QueryParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT paymentKey FROM Payment WHERE orders.id = :orderId ORDER BY recordedAt DESC LIMIT 1")
    Optional<String> findOldestPaymentKeyByOrdersId(@QueryParam("orderId") String orderId);
}

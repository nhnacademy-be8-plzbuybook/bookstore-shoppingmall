package com.nhnacademy.book.payment.repository;


import com.nhnacademy.book.payment.entity.Payment;
import jakarta.ws.rs.QueryParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p WHERE p.orders.id = :orderId ORDER BY p.recordedAt DESC LIMIT 1")
    Optional<Payment> findOldestByOrderId(@QueryParam("orderId") String orderId);



}

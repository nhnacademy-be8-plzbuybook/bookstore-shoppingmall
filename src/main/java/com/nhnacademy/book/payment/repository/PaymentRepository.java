package com.nhnacademy.book.payment.repository;


import com.nhnacademy.book.payment.dto.PaymentDto;
import com.nhnacademy.book.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    /**
     * @deprecated
     * @param ordersId
     * @return
     */
    Optional<PaymentDto> findByOrders_Id(String ordersId);
    Optional<Payment> findByOrdersId(String orderId);

}

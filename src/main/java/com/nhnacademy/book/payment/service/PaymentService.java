package com.nhnacademy.book.payment.service;

import com.nhnacademy.book.payment.dto.PaymentSaveRequestDto;
import com.nhnacademy.book.payment.entity.Payment;

public interface PaymentService {
    String createPayment(PaymentSaveRequestDto saveRequest);
    Payment getPaymentByOrderId(String orderId);
//    void removePayment(long paymentId);
}

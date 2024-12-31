package com.nhnacademy.book.payment.service;

import com.nhnacademy.book.payment.dto.PaymentConfirmRequestDto;
import com.nhnacademy.book.payment.dto.PaymentSaveRequestDto;
import com.nhnacademy.book.payment.dto.SaveAmountDto;
import com.nhnacademy.book.payment.entity.Payment;

public interface PaymentService {
    String recordPayment(PaymentSaveRequestDto saveRequest);
    Payment getByOrderId(String orderId);
    void verifyPayment(PaymentConfirmRequestDto confirmRequest);
//    void removePayment(long paymentId);
}
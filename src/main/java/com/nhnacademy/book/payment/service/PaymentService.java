package com.nhnacademy.book.payment.service;

import com.nhnacademy.book.payment.dto.PaymentCancelRequestDto;
import com.nhnacademy.book.payment.dto.PaymentConfirmRequestDto;
import com.nhnacademy.book.payment.dto.PaymentSaveRequestDto;

public interface PaymentService {
    String recordPayment(PaymentSaveRequestDto saveRequest);
    void verifyPayment(PaymentConfirmRequestDto confirmRequest);
    void cancelPayment(String paymentKey, PaymentCancelRequestDto cancelRequest);
}
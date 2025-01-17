package com.nhnacademy.book.payment.service;

import com.nhnacademy.book.payment.dto.PaymentCancelRequestDto;
import com.nhnacademy.book.payment.dto.PaymentConfirmRequestDto;
import com.nhnacademy.book.payment.dto.PaymentSaveRequestDto;
import org.json.simple.JSONObject;

public interface PaymentService {
    Long recordPayment(PaymentSaveRequestDto saveRequest);

    void removePayment(Long paymentId);

    void verifyPayment(PaymentConfirmRequestDto confirmRequest);

    Long cancelPayment(PaymentCancelRequestDto cancelRequest);

    JSONObject confirmPayment(PaymentConfirmRequestDto confirmRequest);
}
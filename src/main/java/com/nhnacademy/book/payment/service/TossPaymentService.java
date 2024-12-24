package com.nhnacademy.book.payment.service;

import com.nhnacademy.book.payment.dto.PaymentCancelRequestDto;
import com.nhnacademy.book.payment.dto.PaymentConfirmRequestDto;
import org.json.simple.JSONObject;

public interface TossPaymentService {

    JSONObject cancelPayment(String paymentKey, PaymentCancelRequestDto cancelRequest);
    JSONObject confirmPayment(PaymentConfirmRequestDto confirmRequest);
}

package com.nhnacademy.book.payment.service;

import com.nhnacademy.book.payment.dto.PaymentCancelRequestDto;
import com.nhnacademy.book.payment.dto.PaymentConfirmRequestDto;
import org.json.simple.JSONObject;

import java.util.LinkedHashMap;

public interface TossPaymentService {

    JSONObject cancelPayment(String paymentKey, PaymentCancelRequestDto cancelRequest);
    JSONObject confirmPayment(PaymentConfirmRequestDto confirmRequest);
    LinkedHashMap<String, Object> extractLatestCancel(JSONObject jsonObject);
}

package com.nhnacademy.book.payment.service.impl;

import com.nhnacademy.book.payment.dto.PaymentCancelRequestDto;
import com.nhnacademy.book.payment.dto.PaymentConfirmRequestDto;
import com.nhnacademy.book.payment.service.TossPaymentService;
import com.nhnacademy.book.webClient.TossPaymentClient;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;

@RequiredArgsConstructor
@Service
public class TossPaymentServiceImpl implements TossPaymentService {
    private final TossPaymentClient tossPaymentClient;

    @Value("${tossSecretKey}")
    private String tossSecretKey;

    @Override
    public JSONObject cancelPayment(String paymentKey, PaymentCancelRequestDto cancelRequest) {
        String basicToken = getBasicToken();
        return tossPaymentClient.cancelPayment(basicToken, paymentKey, cancelRequest);
    }

    @Override
    public JSONObject confirmPayment(PaymentConfirmRequestDto confirmRequest) {
        String basicToken = getBasicToken();
        return tossPaymentClient.confirmPayment(basicToken, confirmRequest);
    }

    @Override
    public LinkedHashMap<String, Object> extractLatestCancel(JSONObject jsonObject) {
        try {
            ArrayList<LinkedHashMap<String, Object>> cancels = (ArrayList<LinkedHashMap<String, Object>>) jsonObject.get("cancels");
            return cancels.getLast();
        } catch (Exception e) {
            throw new RuntimeException("결제취소정보를 가져오는 중 오류가 발생했습니다.");
        }
    }

    private String getBasicToken() {
        return "Basic " + Base64.getEncoder().encodeToString((tossSecretKey + ":").getBytes(StandardCharsets.UTF_8));
    }
}

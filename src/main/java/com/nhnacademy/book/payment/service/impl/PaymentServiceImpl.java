package com.nhnacademy.book.payment.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.payment.dto.*;
import com.nhnacademy.book.payment.entity.Payment;
import com.nhnacademy.book.payment.repository.PaymentRepository;
import com.nhnacademy.book.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private static final String SAVE_AMOUNT_KEY = "save_amount:";
    private final RedisTemplate<String, String> redisTemplate;
    private final PaymentRepository paymentRepository;

    @Override
    public String recordPayment(PaymentSaveRequestDto saveRequest) {
        Payment payment = saveRequest.toEntity();
        Payment savedPayment = paymentRepository.save(payment);

        return savedPayment.getOrderId();
    }

    @Override
    public Payment getByOrderId(String orderId) {
        Optional<Payment> optionalPayment = paymentRepository.findByOrderId(orderId);
        return optionalPayment.orElseThrow(() -> new NotFoundException("can not found payment info"));
    }

    public void saveAmountOnRedis(SaveAmountDto saveRequest) {
        String key = SAVE_AMOUNT_KEY + saveRequest.orderId();
        redisTemplate.opsForValue().set(key, saveRequest.amount());
    }

    public boolean verifyPayment(PaymentConfirmRequestDto confirmRequest) {
        String key = SAVE_AMOUNT_KEY + confirmRequest.getOrderId();
        String savedAmount = redisTemplate.opsForValue().get(key);

        return confirmRequest.getAmount().toString().equals(savedAmount);
    }


}

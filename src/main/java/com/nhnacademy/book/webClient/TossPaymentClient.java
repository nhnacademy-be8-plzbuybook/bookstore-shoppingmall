package com.nhnacademy.book.webClient;

import com.nhnacademy.book.payment.dto.PaymentCancelRequestDto;
import com.nhnacademy.book.payment.dto.PaymentConfirmRequestDto;
import org.json.simple.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "tossClient", url = "https://api.tosspayments.com/v1/payments")
public interface TossPaymentClient {

    @PostMapping(value = "/{paymentKey}/cancel", consumes = "application/json")
    JSONObject cancelPayment(@RequestHeader("Authorization") String basicToken,
                             @PathVariable("paymentKey") String paymentKey,
                             @RequestBody PaymentCancelRequestDto cancelRequest);

    @PostMapping(value = "/confirm", consumes = "application/json")
    JSONObject confirmPayment(@RequestHeader("Authorization") String basicToken, @RequestBody PaymentConfirmRequestDto confirmRequest);
}

package com.nhnacademy.book.payment.service;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.payment.dto.*;
import com.nhnacademy.book.payment.entity.Payment;
import com.nhnacademy.book.payment.repository.PaymentRepository;
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
public class PaymentServiceImpl implements PaymentService{
    private static final String SAVE_AMOUNT_KEY = "save_amount:";
    private final RedisTemplate<String, String> redisTemplate;
    private final PaymentRepository paymentRepository;

    @Override
    public String createPayment(PaymentSaveRequestDto saveRequest) {
        Payment payment = saveRequest.toEntity();
        Payment savedPayment = paymentRepository.save(payment);

        return savedPayment.getTossOrderId();
    }

    @Override
    public Payment getPaymentByOrderId(String orderId) {
        Optional<Payment> optionalPayment = paymentRepository.findByTossOrderId(orderId);
        return optionalPayment.orElseThrow(() -> new NotFoundException("can not found payment info"));
    }

//    @Override
//    public void removePayment(long paymentId) {
//        if (paymentRepository.existsById(paymentId)) {
//            paymentRepository.deleteById(paymentId);
//        }
//        throw new NotFoundException("can not found payment!");
//    }

    private HttpURLConnection createConnection(String secretKey, String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8)));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        return connection;
    }

    public JSONObject parseRequestData(String jsonBody) {
        try {
            return (JSONObject) new JSONParser().parse(jsonBody);
        } catch (ParseException e) {
            log.error("JSON Parsing Error", e);
            return new JSONObject();
        }
    }

    public JSONObject sendRequest(JSONObject requestData, String secretKey, String urlString) throws IOException {
        HttpURLConnection connection = createConnection(secretKey, urlString);
        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestData.toString().getBytes(StandardCharsets.UTF_8));
        }

        try (InputStream responseStream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream();
             Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {
            return (JSONObject) new JSONParser().parse(reader);
        } catch (Exception e) {
            log.error("Error reading response", e);
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("error", "Error reading response");
            return errorResponse;
        }
    }

    public void saveAmountOnRedis(SaveAmountDto saveRequest) {
        String key = SAVE_AMOUNT_KEY + saveRequest.orderId();
        redisTemplate.opsForValue().set(key, saveRequest.amount());
    }

    public boolean verifyPayment(JSONObject requestData) {
        String orderId = (String) requestData.get("orderId");
        String amount = (String) requestData.get("amount");

        String key = SAVE_AMOUNT_KEY + orderId;
        String savedAmount = redisTemplate.opsForValue().get(key);

        return amount.equals(savedAmount);
    }


}

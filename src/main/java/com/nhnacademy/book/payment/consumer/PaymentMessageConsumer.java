package com.nhnacademy.book.payment.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.payment.dto.PaymentCancelRequestDto;
import com.nhnacademy.book.payment.dto.PaymentSaveRequestDto;
import com.nhnacademy.book.payment.entity.Payment;
import com.nhnacademy.book.payment.service.PaymentService;
import com.nhnacademy.book.payment.service.TossPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.nhnacademy.book.payment.service.impl.PaymentMessageService.PAYMENT_APPROVED_ROUTING_KEY;
import static com.nhnacademy.book.payment.service.impl.PaymentMessageService.PAYMENT_CANCEL_ROUTING_KEY;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentMessageConsumer {
    private final PaymentService paymentService;
    private final TossPaymentService tossPaymentService;
    private final ObjectMapper objectMapper;
    private static final String PAYMENT_QUEUE_NAME = "pbb.payment.queue";

    @RabbitListener(queues = PAYMENT_QUEUE_NAME)
    public void consumePaymentQueue(Message message) throws IOException {
        log.info("received: {}", message);
        long deliveryCount = message.getMessageProperties().getDeliveryTag();

        String receivedRoutingKey = message.getMessageProperties().getReceivedRoutingKey();
        String jsonString = new String(message.getBody());

        try {
            switch (receivedRoutingKey) {
                case PAYMENT_APPROVED_ROUTING_KEY:
                    PaymentSaveRequestDto saveRequest = objectMapper.readValue(jsonString, PaymentSaveRequestDto.class);
                    paymentService.recordPayment(saveRequest);
                    break;

                case PAYMENT_CANCEL_ROUTING_KEY:
                    PaymentCancelRequestDto cancelRequestDto = objectMapper.readValue(jsonString, PaymentCancelRequestDto.class);
                    Payment payment = paymentService.getByOrderId(cancelRequestDto.getOrderId());
                    JSONObject response = tossPaymentService.cancelPayment(payment.getPaymentKey(), cancelRequestDto);
                    break;
            }
        } catch (NotFoundException e) {
            // 결제 정보를 찾을 수 없는 경우는 재시도해도 소용없으므로 바로 DLQ로
            log.error("Payment not found. Moving to DLQ: {}", e.getMessage());
            throw new AmqpRejectAndDontRequeueException(e);

        } catch (Exception e) {
            // 그 외 에러는 재시도 (설정된 최대 재시도 횟수까지)
            log.error("Error processing message: {}", e.getMessage());
            throw e;
        }
    }
}

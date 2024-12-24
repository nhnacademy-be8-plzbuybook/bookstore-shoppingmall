package com.nhnacademy.book.payment.service.impl;

import com.nhnacademy.book.payment.dto.PaymentCancelRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentMessageService {
    private final RabbitTemplate rabbitTemplate;
    private static final String ORDER_FANOUT_EXCHANGE = "pbb.order.exchange";
    private static final String ORDER_DIRECT_EXCHANGE = "pbb.order.direct";
    public static final String PAYMENT_APPROVED_ROUTING_KEY = "payment.approved";
    public static final String PAYMENT_CANCEL_ROUTING_KEY = "payment.canceled";

    public void sendConfirmMessage(Object obj) {
        rabbitTemplate.convertAndSend(ORDER_DIRECT_EXCHANGE, PAYMENT_APPROVED_ROUTING_KEY, obj);
    }

    public void sendCancelMessage(PaymentCancelRequestDto cancelRequest) {
        rabbitTemplate.convertAndSend(ORDER_DIRECT_EXCHANGE, PAYMENT_CANCEL_ROUTING_KEY, cancelRequest);
    }


}

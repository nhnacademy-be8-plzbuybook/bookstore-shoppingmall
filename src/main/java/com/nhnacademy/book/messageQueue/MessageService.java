package com.nhnacademy.book.messageQueue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageService {
    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(MessageDto messageDto) {
        log.info("message sent: {}", messageDto.toString());
        rabbitTemplate.convertAndSend("pbb.order.request.exchange", "pbb.*.order.request", messageDto);
    }
}

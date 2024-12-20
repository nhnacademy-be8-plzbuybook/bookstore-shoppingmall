package com.nhnacademy.book.messageQueue;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MessageController {
    private final MessageService messageService;

    @PostMapping(value = "/send/message")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDto messageDto) {
        messageService.sendMessage(messageDto);
        return ResponseEntity.ok("Message sent to RabbitMQ!🐇");
    }
}

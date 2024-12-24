package com.nhnacademy.book.messageQueue;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitMqConfigProperties {
    private String host;
    private int port;
    private String username;
    private String password;
    private String virtualHost;

}

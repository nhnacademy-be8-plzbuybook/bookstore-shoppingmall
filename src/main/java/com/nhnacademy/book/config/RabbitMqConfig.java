package com.nhnacademy.book.config;

import com.nhnacademy.book.messageQueue.RabbitMqConfigProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({RabbitMqConfigProperties.class})
public class RabbitMqConfig {
    private final RabbitMqConfigProperties rabbitMqConfigProperties;
    private static final String ORDER_REQUEST_ROUTING_KEY = "pbb.*.order.request";

    @Autowired
    public RabbitMqConfig(RabbitMqConfigProperties rabbitMqConfigProperties) {
        this.rabbitMqConfigProperties = rabbitMqConfigProperties;
    }

    @Bean
    public Queue paymentOrderRequestQueue() {
        return new Queue("pbb.payment.order.request.queue");
    }

    @Bean
    public Queue productOrderRequestQueue() {
        return new Queue("pbb.product.order.request.queue");
    }

    @Bean
    public TopicExchange orderRequestExchange() {
        return new TopicExchange("pbb.order.request.exchange");
    }

    @Bean
    public Binding paymentOrderRequestBiding(Queue paymentOrderRequestQueue, TopicExchange exchange) {
        return BindingBuilder.bind(paymentOrderRequestQueue).to(exchange).with(ORDER_REQUEST_ROUTING_KEY);
    }

    @Bean
    public Binding productOrderRequestBiding(Queue productOrderRequestQueue, TopicExchange exchange) {
        return BindingBuilder.bind(productOrderRequestQueue).to(exchange).with(ORDER_REQUEST_ROUTING_KEY);
    }


    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitMqConfigProperties.getHost());
        connectionFactory.setPort(rabbitMqConfigProperties.getPort());
        connectionFactory.setUsername(rabbitMqConfigProperties.getUsername());
        connectionFactory.setPassword(rabbitMqConfigProperties.getPassword());
        connectionFactory.setVirtualHost(rabbitMqConfigProperties.getVirtualHost());
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
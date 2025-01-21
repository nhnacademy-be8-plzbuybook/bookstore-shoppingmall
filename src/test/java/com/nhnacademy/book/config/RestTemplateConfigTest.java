package com.nhnacademy.book.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


class RestTemplateConfigTest {

    @Test
    void testRestTemplateBean() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RestTemplateConfig.class);

        RestTemplate restTemplate = context.getBean(RestTemplate.class);

        assertThat(restTemplate).isNotNull();

        context.close();
    }

    @Test
    void testXmlMessageConverter() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RestTemplateConfig.class);

        RestTemplate restTemplate = context.getBean(RestTemplate.class);

        HttpMessageConverter<?> xmlConverter = restTemplate.getMessageConverters().stream()
                .filter(converter -> converter instanceof MappingJackson2XmlHttpMessageConverter)
                .findFirst()
                .orElse(null);

        assertThat(xmlConverter).isNotNull();

        context.close();
    }
}


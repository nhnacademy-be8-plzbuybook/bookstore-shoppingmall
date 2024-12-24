package com.nhnacademy.book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients  // Feign 활성화
@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = {
        "com.nhnacademy.book.book.repository",
        "com.nhnacademy.book.member.domain.repository",
        "com.nhnacademy.book.wrappingPaper.repository",
        "com.nhnacademy.book.point.repository",
        "com.nhnacademy.book.deliveryFeePolicy.repository"
})
@EnableElasticsearchRepositories("com.nhnacademy.book.book.elastic.repository")
public class BookApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookApplication.class, args);
    }

}

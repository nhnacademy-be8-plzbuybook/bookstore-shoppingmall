package com.nhnacademy.book.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {
        "com.nhnacademy.book.book.repository",
        "com.nhnacademy.book.member.domain.repository",
        "com.nhnacademy.book.wrappingPaper.repository",
        "com.nhnacademy.book.point.repository",
        "com.nhnacademy.book.deliveryFeePolicy.repository",
        "com.nhnacademy.book.order.repository",
        "com.nhnacademy.book.orderProduct.repository",
        "com.nhnacademy.book.payment.repository",
        "com.nhnacademy.book.review.repository",
        "com.nhnacademy.book.skm.properties"
})
public class JpaRepositoryConfig {
}

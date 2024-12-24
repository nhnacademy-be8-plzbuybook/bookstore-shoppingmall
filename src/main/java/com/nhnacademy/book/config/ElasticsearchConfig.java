package com.nhnacademy.book.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
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
        "com.nhnacademy.book.review.repository"

})
@EnableElasticsearchRepositories("com.nhnacademy.book.book.elastic.repository")
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo("115.94.72.197:9200") // 원격 서버 URL
                .withBasicAuth("elastic", "nhnacademy123!") // 기본 인증 추가
                .build();
    }
}

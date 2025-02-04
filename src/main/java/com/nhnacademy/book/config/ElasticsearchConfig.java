package com.nhnacademy.book.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.nhnacademy.book.book.elastic.repository")
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    @Value("${spring.elasticsearch.uris}")
    private String url;

    @Value("${spring.elasticsearch.username}")
    private String username;

    @Value("${spring.elasticsearch.password}")
    private String password;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
//                .connectedTo("localhost:9200") // 원격 서버 URL
//                .connectedTo("220.67.216.14:9200") // 원격 서버 URL
//                .withBasicAuth("elastic", "nhnacademy123!") // 기본 인증 추가
                .connectedTo(url) // 원격 서버 URL
                .withBasicAuth(username, password) // 기본 인증 추가
                .build();
    }


}

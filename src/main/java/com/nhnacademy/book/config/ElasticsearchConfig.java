package com.nhnacademy.book.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.nhnacademy.book.book.elastic.repository")
public class ElasticsearchConfig extends ElasticsearchConfiguration {


    @Value("${elasticUrl}")
    private String elasticUrl;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
//                .connectedTo("localhost:9200") // 원격 서버 URL
                .connectedTo(elasticUrl) // 원격 서버 URL
                .withBasicAuth("elastic", "nhnacademy123!") // 기본 인증 추가
                .build();
    }




}

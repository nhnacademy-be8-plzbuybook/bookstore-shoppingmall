package com.nhnacademy.book.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.nhnacademy.book.book.elastic.repository")
public class ElasticsearchConfig extends ElasticsearchConfiguration {




    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
//                .connectedTo("localhost:9200") // 원격 서버 URL
                .connectedTo("10.116.64.14:9200") // 원격 서버 URL
                .withBasicAuth("elastic", "nhnacademy123!") // 기본 인증 추가
                .build();
    }




}

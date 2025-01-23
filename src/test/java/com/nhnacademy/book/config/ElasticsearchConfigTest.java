package com.nhnacademy.book.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations = "classpath:application-dev.yml")
@SpringJUnitConfig
@ContextConfiguration(classes = ElasticsearchConfig.class)
class ElasticsearchConfigTest {

    @Autowired
    private ElasticsearchConfig elasticsearchConfig;

    @Test
    void testElasticsearchConfig() {
        var clientConfig = elasticsearchConfig.clientConfiguration();

        String endpoints = clientConfig.getEndpoints().toString();  // 리스트를 문자열로 변환

        assertThat(endpoints).contains("220.67.216.14");
    }
}
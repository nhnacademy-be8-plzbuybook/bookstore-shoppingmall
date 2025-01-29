package com.nhnacademy.book.config;

import lombok.RequiredArgsConstructor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Bean
    public DataSource dataSource() {

        //복호화 된걸 db환경으로 설정
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://220.67.216.14:13306/project_be8_plzbuybook_bookstore");
        dataSource.setUsername("project_be8_plzbuybook");
        dataSource.setPassword("MOW6c#y4TVxi1P5b");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // dbcp2 설정
        dataSource.setInitialSize(20);
        dataSource.setMaxTotal(20);
        dataSource.setMaxIdle(20);
        dataSource.setMinIdle(20);

        return dataSource;
    }
}
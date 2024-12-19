//package com.nhnacademy.book.config;
//
//import com.nhnacademy.book.skm.properties.SKMProperties;
//import com.nhnacademy.book.skm.service.SecureKeyManagerService;
//import lombok.RequiredArgsConstructor;
//import org.apache.commons.dbcp2.BasicDataSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
//@Configuration
//@RequiredArgsConstructor
//public class DataSourceConfig {
//
//    private final SKMProperties skmProperties;
//    private final SecureKeyManagerService secureKeyManagerService;
//
//    @Bean
//    public DataSource dataSource(){
//        //암호 id로 된 걸 가져온다
//        String encryptedUrlKey = skmProperties.getDatabase().getUrl();
//        String encryptedUsernameKey = skmProperties.getDatabase().getUsername();
//        String encryptedPasswordKey = skmProperties.getDatabase().getPassword();
//
//        //복호화 한다
//        String decryptedUrl = secureKeyManagerService.fetchSecret(encryptedUrlKey);
//        String decryptedUserName = secureKeyManagerService.fetchSecret(encryptedUsernameKey);
//        String decryptedPassword = secureKeyManagerService.fetchSecret(encryptedPasswordKey);
//
//        //복호화 된걸 db환경으로 설정
//        BasicDataSource dataSource = new BasicDataSource();
//        dataSource.setUrl(decryptedUrl);
//        dataSource.setUsername(decryptedUserName);
//        dataSource.setPassword(decryptedPassword);
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//
//        return dataSource;
//    }
//}
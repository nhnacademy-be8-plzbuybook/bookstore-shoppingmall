//package com.nhnacademy.book.skm.properties;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.TestPropertySource;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@ActiveProfiles(value = {"back2"})
//class SKMPropertiesTest {
//
//    @Autowired
//    private SKMProperties skmProperties;
//
//    @Test
//    @DisplayName("yml을 잘읽어오는 가")
//    void ymlLoading_success() {
//        assertEquals("https://api-keymanager.nhncloudservice.com",skmProperties.getUrl());
//        assertEquals("qTQNj7LyHhdAazH3",skmProperties.getAppKey());
//        assertEquals("skm.p12",skmProperties.getKeystoreFile());
//        assertEquals("4444",skmProperties.getPassword());
//
//        assertEquals("1117490ed9294c8798e83f2cb162982d", skmProperties.getDatabase().getUrl());
//        assertEquals("7f2f713f0a6c4d82acb64ed3aa831cee", skmProperties.getDatabase().getUsername());
//        assertEquals("7ac2c65487d34a7fa73997fada11d730", skmProperties.getDatabase().getPassword());
//    }
//
//
//}
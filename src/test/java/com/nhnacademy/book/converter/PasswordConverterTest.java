//package com.nhnacademy.book.converter;
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotEquals;
//
//@Slf4j
//@ExtendWith(SpringExtension.class)
//public class PasswordConverterTest {
//
//    @Mock
//    private PasswordConverter passwordConverter;
//
//    private String plainPassword;
//
//    @BeforeEach
//    public void setUp() {
//        plainPassword = "password";
//    }
//
////    @Configuration
////    static class TestConfig {
////        @Bean
////        public PasswordConverter passwordConverter() {
////            return new PasswordConverter();
////        }
////    }
//
//    @Test
//    public void testEncryptAndDecrypt() {
//        // 암호화
//        String encryptedPassword = passwordConverter.convertToDatabaseColumn(plainPassword);
//        log.info("Encrypted password: {}", encryptedPassword);
//
//        assertNotEquals(plainPassword, encryptedPassword);
//
//        String decryptedPassword = passwordConverter.convertToEntityAttribute(encryptedPassword);
//        log.info("Decrypted password: {}", decryptedPassword);
//
//        assertEquals(plainPassword, decryptedPassword);
//    }
//
//}

package com.nhnacademy.book.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Converter
@Component
public class PasswordConverter implements AttributeConverter<String, String> {

    // 고정된 키를 얻어오는 메소드
    private final SecretKey secretKey = TwoWayEncryption.getFixedKey();

    @Override
    public String convertToDatabaseColumn(String password) {
        if (password == null) {
            return null;
        }
        try {
            // 암호화된 값을 데이터베이스에 저장
            return TwoWayEncryption.encrypt(password, secretKey);
        } catch (Exception e) {
            // 예외 처리
            throw new RuntimeException("Error encrypting password: " + e.getMessage(), e);
        }
    }

    @Override
    public String convertToEntityAttribute(String encryptedPassword) {
        if (encryptedPassword == null) {
            return null;
        }
        try {
            // 데이터베이스에서 가져온 암호를 복호화하여 반환
            return TwoWayEncryption.decrypt(encryptedPassword, secretKey);
        } catch (Exception e) {
            // 예외 처리
            throw new RuntimeException("Error decrypting password: " + e.getMessage(), e);
        }
    }
}

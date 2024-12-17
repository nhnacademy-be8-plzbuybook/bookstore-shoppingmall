package com.nhnacademy.book.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Converter
@Component
public class PasswordConverter implements AttributeConverter<String, String> {

    private final SecretKey secretKey = TwoWayEncryption.getFixedKey();

    @Override
    public String convertToDatabaseColumn(String password) {
        if (password == null) {
            return null;
        }
        try {
            return TwoWayEncryption.encrypt(password, secretKey);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting password: " + e.getMessage(), e);
        }
    }

    @Override
    public String convertToEntityAttribute(String encryptedPassword) {
        if (encryptedPassword == null) {
            return null;
        }
        try {
            return TwoWayEncryption.decrypt(encryptedPassword, secretKey);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting password: " + e.getMessage(), e);
        }
    }
}

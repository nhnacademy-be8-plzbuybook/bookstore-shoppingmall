package com.nhnacademy.book.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Converter
@Component
public class PasswordConverter implements AttributeConverter<String, String> {


    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return passwordEncoder.encode(attribute);
    }

    @Override
    //converting을 안하더라도 암호화 된값이라도 가져와야 회원-권한의 관계를 생성할 수 있기때문에 이렇게 했습니다
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }

}

package com.nhnacademy.book.converter;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class TwoWayEncryption {
    private static final String ALGORITHM = "AES";

    // 고정된 키 (예시로 16바이트 길이의 문자열을 사용)
    private static final String FIXED_KEY = "1234567890abcdef"; // 16바이트 고정된 키

    // 고정된 키로 SecretKey 생성
    public static SecretKey getFixedKey() {
        return new SecretKeySpec(FIXED_KEY.getBytes(), ALGORITHM);
    }

    // 암호화
    public static String encrypt(String plainText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // 복호화
    public static String decrypt(String encryptedText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

}
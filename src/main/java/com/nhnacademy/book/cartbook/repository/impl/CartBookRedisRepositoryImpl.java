package com.nhnacademy.book.cartbook.repository.impl;

import com.nhnacademy.book.cartbook.dto.response.ReadCartBookResponseDto;
import com.nhnacademy.book.cartbook.repository.CartBookRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class CartBookRedisRepositoryImpl implements CartBookRedisRepository {
    private final RedisTemplate<String, Object> cartRedisTemplate;

    public CartBookRedisRepositoryImpl(@Qualifier("cartRedisTemplate") RedisTemplate<String, Object> cartRedisTemplate) {
        this.cartRedisTemplate = cartRedisTemplate;
    }

    @Override
    public Long create(String hashName, Long id, ReadCartBookResponseDto readCartBookResponseDto) {
        cartRedisTemplate.opsForHash().put(hashName + ":", id.toString(), readCartBookResponseDto);
        cartRedisTemplate.expire(hashName+":", 1, TimeUnit.HOURS);
        return id;
    }

    @Override
    public Long update(String hashName, Long id, int quantity) {
        log.info("id: {}", id);
        log.info("quantity: {}", quantity);
        log.info("hashName: {}", hashName);
        ReadCartBookResponseDto resp = (ReadCartBookResponseDto) cartRedisTemplate.opsForHash().get(hashName + ":", id.toString());
        log.info("resp: {}", resp);
         assert resp != null;
        ReadCartBookResponseDto updatedResp = ReadCartBookResponseDto.builder()
                .cartId(resp.cartId())
                .cartBookId(resp.cartBookId())
                .sellingBookId(resp.sellingBookId())
                .bookTitle(resp.bookTitle())
                .sellingBookPrice(resp.sellingBookPrice())
                .imageUrl(resp.imageUrl())
                .quantity(quantity) // 수량만 업데이트
                .sellingBookStock(resp.sellingBookStock())
                .build();
        cartRedisTemplate.opsForHash().put(hashName + ":", id.toString(), updatedResp);
        cartRedisTemplate.expire(hashName+":", 1, TimeUnit.HOURS);
        log.info("updatedResp quantity: {}", updatedResp.quantity());
        return id;
    }

    @Override
    public Long delete(String hashName, Long id) {
        cartRedisTemplate.opsForHash().delete(hashName + ":", id.toString());
        return id;
    }

    @Override
    public void deleteAll(String hashName) {
        cartRedisTemplate.delete(hashName + ":");
    }

    @Override
    public List<ReadCartBookResponseDto> readAllHashName(String hashName) {
        List<Object> bookCartList = cartRedisTemplate.opsForHash().values(hashName + ":");

        return bookCartList
                .stream()
                .map(ReadCartBookResponseDto.class::cast)
                .toList();
    }

    @Override
    public boolean isHit(String hashName) {
        return Boolean.TRUE.equals(cartRedisTemplate.hasKey(hashName + ":"));
    }

    @Override
    public boolean isMiss(String hashName) {
        return Boolean.FALSE.equals(cartRedisTemplate.hasKey(hashName + ":"));
    }

    @Override
    public void loadData(List<ReadCartBookResponseDto> bookCartGuestResponses, String hashName) {
        for (ReadCartBookResponseDto bookCartGuestResponseDto : bookCartGuestResponses) {
            if(Objects.nonNull(bookCartGuestResponseDto)) {
                cartRedisTemplate.opsForHash().put(hashName + ":", bookCartGuestResponseDto.cartBookId().toString(), bookCartGuestResponseDto);
            }
            cartRedisTemplate.expire(hashName + ":", 1, TimeUnit.HOURS);
        }
    }
}

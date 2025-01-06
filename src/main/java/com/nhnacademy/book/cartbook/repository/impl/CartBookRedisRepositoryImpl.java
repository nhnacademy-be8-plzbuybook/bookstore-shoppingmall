package com.nhnacademy.book.cartbook.repository.impl;

import com.nhnacademy.book.cartbook.dto.response.ReadGuestCartBookResponseDto;
import com.nhnacademy.book.cartbook.repository.CartBookRedisRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Repository
public class CartBookRedisRepositoryImpl implements CartBookRedisRepository {
    private final RedisTemplate<String, Object> cartRedisTemplate;

    public CartBookRedisRepositoryImpl(@Qualifier("cartRedisTemplate") RedisTemplate<String, Object> cartRedisTemplate) {
        this.cartRedisTemplate = cartRedisTemplate;
    }

    @Override
    public Long create(String hashName, Long id, ReadGuestCartBookResponseDto readGuestCartBookResponseDto) {
        cartRedisTemplate.opsForHash().put(hashName + ":", id.toString(), readGuestCartBookResponseDto);
        cartRedisTemplate.expire(hashName+":", 1, TimeUnit.HOURS);
        return id;
    }

    @Override
    public Long update(String hashName, Long id, int quantity) {
        ReadGuestCartBookResponseDto resp = (ReadGuestCartBookResponseDto) cartRedisTemplate.opsForHash().get(hashName + ":", id.toString());
        assert resp != null;
        ReadGuestCartBookResponseDto updatedResp = ReadGuestCartBookResponseDto.builder()
                .cartBookId(resp.cartBookId())
                .bookId(resp.bookId())
                .bookTitle(resp.bookTitle())
                .sellingBookPrice(resp.sellingBookPrice())
                .imageUrl(resp.imageUrl())
                .quantity(quantity) // 수량만 업데이트
                .sellingBookStock(resp.sellingBookStock())
                .sellingBookPackageable(resp.sellingBookPackageable())
                .used(resp.used())
                .build();
        cartRedisTemplate.opsForHash().put(hashName + ":", id.toString(), updatedResp);
        cartRedisTemplate.expire(hashName+":", 1, TimeUnit.HOURS);
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
    public List<ReadGuestCartBookResponseDto> readAllHashName(String hashName) {
        List<Object> bookCartList = cartRedisTemplate.opsForHash().values(hashName + ":");

        return bookCartList
                .stream()
                .map(ReadGuestCartBookResponseDto.class::cast)
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
    public void loadData(List<ReadGuestCartBookResponseDto> bookCartGuestResponses, String hashName) {
        for (ReadGuestCartBookResponseDto bookCartGuestResponseDto : bookCartGuestResponses) {
            if(Objects.nonNull(bookCartGuestResponseDto)) {
                cartRedisTemplate.opsForHash().put(hashName + ":", bookCartGuestResponseDto.cartBookId().toString(), bookCartGuestResponseDto);
            }
            cartRedisTemplate.expire(hashName + ":", 1, TimeUnit.HOURS);
        }
    }
}

package com.nhnacademy.book.order.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.order.dto.orderRequests.MemberOrderRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.NonMemberOrderRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.service.OrderCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OrderCacheServiceImpl implements OrderCacheService {
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> orderRedisTemplate;

    public OrderCacheServiceImpl(ObjectMapper objectMapper,
                                 @Qualifier("redisTemplate") RedisTemplate<String, Object> orderRedisTemplate) {
        this.objectMapper = objectMapper;
        this.orderRedisTemplate = orderRedisTemplate;
    }


    /**
     * 주문 캐시데이터 저장
     *
     * @param orderId 주문 ID
     * @param order   주문요청 DTO
     */
    @Transactional
    @Override
    public void saveOrderCache(String orderId, OrderRequestDto order) {
        String key = getOrderCacheKey(orderId);
        try {
            String jsonString = objectMapper.writeValueAsString(order);
            orderRedisTemplate.opsForValue().set(key, jsonString, 15, TimeUnit.MINUTES);  // 15분간 주문정보 캐시 유지
            log.info("orderCache: {}", orderRedisTemplate.opsForValue().get(key));

        } catch (JsonProcessingException e) {
            throw new RuntimeException("주문정보 변환 오류가 발생했습니다.", e);
        } catch (Exception e) {
            throw new RuntimeException("주문정보 캐싱 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 주문 캐시데이터 조회
     *
     * @param orderId 주문 ID
     * @return 주문요청 DTO
     */
    @Override
    public OrderRequestDto fetchOrderCache(String orderId) {
        String key = getOrderCacheKey(orderId);
        Object value = orderRedisTemplate.opsForValue().get(key);

        if (value == null) {
            throw new IllegalArgumentException("주문정보가 만료되었습니다.");
        }
        try {
            if (value instanceof MemberOrderRequestDto) {
                return objectMapper.readValue((String) value, MemberOrderRequestDto.class);
            }
            return objectMapper.readValue((String) value, NonMemberOrderRequestDto.class);


        } catch (JsonProcessingException e) {
            throw new RuntimeException("주문정보 변환 오류가 발생했습니다.", e);
        }
    }

    private String getOrderCacheKey(String orderId) {
        return "order:" + orderId;
    }
}

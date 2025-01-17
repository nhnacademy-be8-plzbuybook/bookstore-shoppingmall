package com.nhnacademy.book.order.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.deliveryFeePolicy.exception.StockNotEnoughException;
import com.nhnacademy.book.order.dto.orderRequests.MemberOrderRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.NonMemberOrderRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.enums.OrderType;
import com.nhnacademy.book.order.service.OrderCacheService;
import com.nhnacademy.book.orderProduct.dto.OrderProductWrappingDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final String STOCK_KEY = "stock:";
    private static final String ORDER_KEY = "order:";

    @Autowired
    public OrderCacheServiceImpl(ObjectMapper objectMapper,
                                 @Qualifier("orderRedisTemplate") RedisTemplate<String, Object> orderRedisTemplate) {
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
            if (((String) value).contains(OrderType.NON_MEMBER_ORDER.name())) {
                return objectMapper.readValue((String) value, NonMemberOrderRequestDto.class);
            }
            return objectMapper.readValue((String) value, MemberOrderRequestDto.class);


        } catch (JsonProcessingException e) {
            throw new RuntimeException("주문정보 변환 오류가 발생했습니다.", e);
        }
    }


    /**
     * 재고 캐시 데이터를 차감(재고 선점)
     *
     * @param productId 상품 ID
     * @param quantity  차감할 재고 수량
     */
    @Override
    public Long preemptStockCache(Long productId, Integer quantity) {
        String key = getStockCacheKey(productId);
        if (orderRedisTemplate.opsForValue().get(key) != null) {
            Long stock = orderRedisTemplate.opsForValue().decrement(key, quantity);
            if (stock < 0) {
                stock = orderRedisTemplate.opsForValue().increment(key, quantity); // 차감한 재고 복구
                log.warn("재고 부족: productId={}, 요청 수량={}, 현재 재고={}", productId, quantity, stock);
                throw new StockNotEnoughException("재고가 부족합니다.");
            } else {
                return stock;
            }
        }
        return null;
    }


    //TODO: 상품들과 동기화 꼭!!!!!!!!!!!!!! 해줘야 됨

    /**
     * 재고 캐시 데이터를 추가
     *
     * @param productId 상품 ID
     * @param quantity  추가할 재고 수량
     */
    @Override
    public void addStockCache(Long productId, Long quantity) {
        String key = getStockCacheKey(productId);
        if (orderRedisTemplate.hasKey(key)) {
            orderRedisTemplate.opsForValue().increment(key, quantity);
        } else {
            orderRedisTemplate.opsForValue().set(key, String.valueOf(quantity)); //TODO: 이거 상품이랑 포장지 등 겹치는 id 있을수도 있어서 고려해야됨
        }
    }

    /**
     * 상품 재고 캐시 조회
     *
     * @param productId 상품 ID
     * @return 상품 재고
     */
    @Override
    public int getStockCache(Long productId) {
        String key = getStockCacheKey(productId);
        Object stock = orderRedisTemplate.opsForValue().get(key);

        if (stock == null) {
            throw new RuntimeException("재고 캐시를 찾을 수 없습니다.");
        }
        return Integer.parseInt((String) stock);
    }

    @Override
    public void rollbackOrderedStock(OrderRequestDto orderRequest) {
        for (OrderProductRequestDto orderProductRequest : orderRequest.getOrderProducts()) {
            String key = getStockCacheKey(orderProductRequest.getProductId());
            // 주문상품 재고 추가
            orderRedisTemplate.opsForValue().increment(key, orderProductRequest.getQuantity());
            if (orderProductRequest.getWrapping() != null) {
                OrderProductWrappingDto orderProductWrappingDto = orderProductRequest.getWrapping();
                key = getStockCacheKey(orderProductWrappingDto.getWrappingPaperId());
                // 포장지 재고 추가
                orderRedisTemplate.opsForValue().increment(key, orderProductWrappingDto.getQuantity());
            }
        }
    }

    private String getOrderCacheKey(String orderId) {
        return ORDER_KEY + orderId;
    }

    private String getStockCacheKey(long productId) {
        return STOCK_KEY + productId;
    }
}

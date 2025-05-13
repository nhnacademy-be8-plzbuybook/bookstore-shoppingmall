package com.nhnacademy.book.order.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.book.exception.SellingBookNotFoundException;
import com.nhnacademy.book.book.repository.SellingBookRepository;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.deliveryFeePolicy.exception.StockNotEnoughException;
import com.nhnacademy.book.order.dto.orderRequests.MemberOrderRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.NonMemberOrderRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.enums.OrderType;
import com.nhnacademy.book.order.service.OrderCacheService;
import com.nhnacademy.book.orderProduct.dto.OrderProductWrappingDto;
import com.nhnacademy.book.wrappingPaper.entity.WrappingPaper;
import com.nhnacademy.book.wrappingPaper.repository.WrappingPaperRepository;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.LongCodec;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class
OrderCacheServiceImpl implements OrderCacheService {
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redis;
    private final WrappingPaperRepository wrappingPaperRepository;
    private final SellingBookRepository sellingBookRepository;
    private final RedissonClient redissonClient;

    private static final String STOCK_KEY = "stock:";
    private static final String ORDER_KEY = "order:";

    @Autowired
    public OrderCacheServiceImpl(ObjectMapper objectMapper,
                                 @Qualifier("orderRedisTemplate") RedisTemplate<String, Object> redis,
                                 WrappingPaperRepository wrappingPaperRepository,
                                 SellingBookRepository sellingBookRepository,
                                 RedissonClient redissonClient) {
        this.objectMapper = objectMapper;
        this.redis = redis;
        this.wrappingPaperRepository = wrappingPaperRepository;
        this.sellingBookRepository = sellingBookRepository;
        this.redissonClient = redissonClient;
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
            redis.opsForValue().set(key, jsonString, 15, TimeUnit.MINUTES);  // 15분간 주문정보 캐시 유지
//            redis.opsForValue().set(key, jsonString, 3, TimeUnit.DAYS);  // 3일간 주문정보 캐시 유지
            log.info("orderCache: {}", redis.opsForValue().get(key));

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
        Object value = redis.opsForValue().get(key);

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
        if (redis.opsForValue().get(key) != null) {
            Long stock = redis.opsForValue().decrement(key, quantity);

            if (stock < 0) {
                stock = redis.opsForValue().increment(key, quantity); // 차감한 재고 복구
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
        if (redis.hasKey(key)) {
            redis.opsForValue().increment(key, quantity);
        } else {
            redis.opsForValue().set(key, String.valueOf(quantity)); //TODO: 이거 상품이랑 포장지 등 겹치는 id 있을수도 있어서 고려해야됨
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
        Object stock = redis.opsForValue().get(key);

        if (stock == null) {
            throw new RuntimeException("재고 캐시를 찾을 수 없습니다.");
        }
        return Integer.parseInt((String) stock);
    }

    @Transactional(readOnly = true)
    @Override
    public int getProductStockCache(Long productId) {
        String key = getProductStockKey(productId);
        try {
            Integer productStock = (Integer) redis.opsForValue().get(key);
            if (productStock == null) {
                SellingBook sellingBook = sellingBookRepository.findById(productId).orElseThrow(() -> new SellingBookNotFoundException("판매책을 찾을 수 없습니다."));
                int stock = sellingBook.getSellingBookStock();
                redis.opsForValue().set(key, stock);
                productStock = stock;
            }

            return productStock;
        } catch (Exception e) {
            log.error("상품 재고를 가져오는 중 오류가 발생했습니다. ", e);
            throw new RuntimeException("상품 재고를 가져오는 중 오류가 발생했습니다. ", e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Long getWrappingPaperStockCache(Long wrappingPaperId) {
        String key = getWrappingPaperStockKey(wrappingPaperId);
        Object wrappingPaperStock = redis.opsForValue().get(key);
        if (wrappingPaperStock == null) {
            WrappingPaper wrappingPaper = wrappingPaperRepository.findById(wrappingPaperId).orElseThrow(() -> new NotFoundException("포장지를 찾을 수 없습니다."));
            Long stock = wrappingPaper.getStock();
            redis.opsForValue().set(key, stock);
            wrappingPaperStock = stock;
        }
        return Long.valueOf(String.valueOf(wrappingPaperStock));
    }

    /**
     * 주문재고 선점
     *
     * @param orderRequest 주문요청 DTO
     * @return 주문 재고차감 맵
     */
    // v1
    @Transactional
    @Override
    public Map<String, Integer> preemptOrderStock(String orderId, OrderRequestDto orderRequest) {
        // 롤백을 위해 map으로 차감한 재고 저장
        Map<String, Integer> preemptedStockMap = new HashMap<>();
        try {
            for (OrderProductRequestDto orderProductRequest : orderRequest.getOrderProducts()) {
                preemptProductStock(preemptedStockMap, orderProductRequest);
                preemptWrappingPaperStock(preemptedStockMap, orderProductRequest);
            }
            String stockMapKey = getOrderStockMapKey(orderId);
            redis.opsForValue().set(stockMapKey, preemptedStockMap);
            return preemptedStockMap;
        } catch (Exception e) {
            log.error("재고 선점 중 오류가 발생했습니다.", e);
            throw new RuntimeException("재고 선점 중 오류가 발생했습니다.", e);
        }
    }

    // v2
    @Override
    public Map<String, Integer> preemptOrderStock(String orderId, List<OrderProductRequestDto> orderProducts) {
        // 롤백을 위해 map으로 차감한 재고 저장
        Map<String, Integer> preemptedStockMap = new HashMap<>();
        try {
            for (OrderProductRequestDto orderProductRequest : orderProducts) {
                preemptProductStock(preemptedStockMap, orderProductRequest);
                preemptWrappingPaperStock(preemptedStockMap, orderProductRequest);
            }
            String stockMapKey = getOrderStockMapKey(orderId);
            redis.opsForValue().set(stockMapKey, preemptedStockMap);
            return preemptedStockMap;
        } catch (Exception e) {
            log.error("재고 선점 중 오류가 발생했습니다.", e);
            throw new RuntimeException("재고 선점 중 오류가 발생했습니다.", e);
        }
    }

    //    private void preemptProductStock(Map<String, Integer> preemptedStockMap, OrderProductRequestDto orderProductRequest) {
//        String bookStockKey = getProductStockKey(orderProductRequest.getProductId());
//        // 상품 재고 차감
//        redis.opsForValue().decrement(bookStockKey, orderProductRequest.getQuantity());
//        preemptedStockMap.put(bookStockKey, orderProductRequest.getQuantity());
//    }
    public void preemptProductStock(Map<String, Integer> preemptedStockMap, OrderProductRequestDto orderProductRequest) {
        String bookStockKey = getProductStockKey(orderProductRequest.getProductId());
        // 재고 감소
        decreaseWithLock(bookStockKey, orderProductRequest.getQuantity());
        preemptedStockMap.put(bookStockKey, orderProductRequest.getQuantity());
    }

    public void decrease(String key, int quantity) {
        RBucket<Long> stockBucket = redissonClient.getBucket(key);
        Long stock = stockBucket.get();
        if (stock < quantity) {
            throw new StockNotEnoughException("재고가 부족합니다. 현재 재고(" + stock + "), 주문수량(" + quantity + ")");
        }
        // 상품 재고 차감
        stockBucket.set(stock - quantity);
    }

    public void decreaseWithLock(String key, int quantity) {
        String lockKey = "lock:" + key;
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean isLocked = lock.tryLock(1, 3, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new RuntimeException("락 획득 실패");
            }
            decrease(key, quantity);
        } catch (InterruptedException e) {
            throw new RuntimeException("재고 차감 중 오류가 발생했습니다.", e);
        } finally {
            lock.unlock();
        }
    }

    public void decreaseWithLuaScript(String key, int quantity) {
        RScript rScript = redissonClient.getScript(StringCodec.INSTANCE);
        String luaScript =
                "local stock = redis.call('GET', KEYS[1]);" +
                        "if stock == nil then return -2; end;" +
                        "stock = tonumber(stock)" +  // 키가 없으면 -2 반환
                        "local quantity = tonumber(ARGV[1]);" +
                        "if stock >= quantity then " +
                        "  return redis.call('DECRBY', KEYS[1], quantity);" +
                        "else return -1; end";
        Long result = rScript.eval(
                RScript.Mode.READ_WRITE,
                luaScript,
                RScript.ReturnType.INTEGER,
                Collections.singletonList(key),
                quantity
        );
        if (result == -1) {
            throw new StockNotEnoughException("재고가 부족합니다.");
        } else if (result == -2) {
            throw new RuntimeException("재고가 등록되지 않았습니다.");
        }
    }

    private void preemptWrappingPaperStock(Map<String, Integer> preemptedStockMap, OrderProductRequestDto orderProductRequest) {
        if (orderProductRequest.getWrapping() != null) {
            OrderProductWrappingDto orderProductWrapping = orderProductRequest.getWrapping();
            String wrappingStockKey = getWrappingPaperStockKey(orderProductWrapping.getWrappingPaperId());
            // 포장지 재고 차감
            decreaseWithLock(wrappingStockKey, orderProductWrapping.getQuantity());
            preemptedStockMap.put(wrappingStockKey, orderProductWrapping.getQuantity());
        }
    }

    @Override
    public void rollbackOrderedStock(String orderId) {
        redis.multi();
        try {
            Map<String, Integer> preemptedStockMap = (Map<String, Integer>) redis.opsForValue().get(getOrderStockMapKey(orderId));
            if (preemptedStockMap != null) {
                preemptedStockMap.forEach((key, value)
                        -> redis.opsForValue().increment(key, value)
                );
            }
            redis.exec();
        } catch (Exception e) {
            redis.discard();
            throw new RuntimeException("재고롤백 중 오류가 발생했습니다.", e);
        }

    }

    private String getOrderCacheKey(String orderId) {
        return ORDER_KEY + orderId;
    }

    private String getStockCacheKey(long productId) {
        return STOCK_KEY + productId;
    }

    public String getOrderStockMapKey(String orderId) {
        return "order:" + orderId + ":stock";
    }

    public String getProductStockKey(Long productId) {
        return "product:" + productId + ":stock";
    }

    public String getWrappingPaperStockKey(Long wrappingPaperId) {
        return "wrappingPaper:" + wrappingPaperId + ":stock";
    }

}

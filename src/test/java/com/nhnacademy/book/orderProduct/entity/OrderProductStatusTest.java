package com.nhnacademy.book.orderProduct.entity;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderProductStatusTest {

    @Test
    void fromCode() {
        OrderProductStatus status = OrderProductStatus.fromCode(0);

        assertNotNull(status);
        assertEquals(OrderProductStatus.PAYMENT_PENDING, status);
    }

    @Test
    void fromCode_not_found() {
        int code = 100000;
        NotFoundException exception = assertThrows(NotFoundException.class, ()
                -> OrderProductStatus.fromCode(code));
        assertEquals("주문상태를 찾을 수 없습니다. 코드: " + code, exception.getMessage());
    }

    @Test
    void fromStatus() {
        String status = "결제대기";
        OrderProductStatus orderProductStatus = OrderProductStatus.fromStatus(status);
        assertNotNull(orderProductStatus);
        assertEquals(status, orderProductStatus.getStatus());
    }

    @Test
    void fromStatus_not_found() {
        String status = "없는 주문상태";
        NotFoundException exception = assertThrows(NotFoundException.class, ()
                -> OrderProductStatus.fromStatus(status));
        assertEquals("주문상태를 찾을 수 없습니다. 주문상태: " + status, exception.getMessage());
    }

    @Test
    void getCode() {
        int expectedCode = 8;
        OrderProductStatus status = OrderProductStatus.RETURN_COMPLETED;

        assertEquals(expectedCode, status.getCode());
    }

    @Test
    void getStatus() {
        String expectedStatus = "반품완료";
        OrderProductStatus status = OrderProductStatus.RETURN_COMPLETED;

        assertEquals(expectedStatus, status.getStatus());

    }

    @Test
    void values() {
        OrderProductStatus[] statuses = OrderProductStatus.values();

        assertEquals(9, statuses.length);
    }

    @Test
    void valueOf() {
        String name = "RETURN_COMPLETED";
        OrderProductStatus status = OrderProductStatus.valueOf(name);
        assertNotNull(status);
        assertEquals(OrderProductStatus.RETURN_COMPLETED, status);
    }
}
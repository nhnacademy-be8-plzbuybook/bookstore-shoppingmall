package com.nhnacademy.book.order.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Test
    void saveOrder() {

    }

    @Test
    void findOrder() {

    }

    @Test
    void updateOrder() {

    }

    @Test
    void deleteOrder() {

    }

}
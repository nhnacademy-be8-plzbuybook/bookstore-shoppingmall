package com.nhnacademy.book.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {
        "com.nhnacademy.book.book.repository",
        "com.nhnacademy.book.member.domain.repository",
        "com.nhnacademy.book.wrappingPaper.repository",
        "com.nhnacademy.book.point.repository",
        "com.nhnacademy.book.deliveryFeePolicy.repository",
        "com.nhnacademy.book.order.repository",
        "com.nhnacademy.book.orderProduct.repository",
        "com.nhnacademy.book.payment.repository",
        "com.nhnacademy.book.review.repository",
        "com.nhnacademy.book.skm.properties",
        "com.nhnacademy.book.cartbook.repository",
        "com.nhnacademy.book.cart.repository"

})
public class JpaRepositoryConfig {
    @PersistenceContext
    private EntityManager entityManager;
    @Bean
    public JPAQueryFactory queryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}

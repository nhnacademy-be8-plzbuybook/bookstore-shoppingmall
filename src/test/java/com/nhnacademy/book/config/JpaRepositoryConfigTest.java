//package com.nhnacademy.book.config;
//
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import jakarta.persistence.EntityManager;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//
//
//@SpringBootTest(classes = JpaRepositoryConfig.class)
//class JpaRepositoryConfigTest {
//
//    @Autowired
//    private JPAQueryFactory jpaQueryFactory;
//
//    @Autowired
//    private EntityManager entityManager;
//
//    @Test
//    void testJpaQueryFactoryBean() {
//        assertThat(entityManager).isNotNull();
//        assertThat(jpaQueryFactory).isNotNull();
//    }
//}
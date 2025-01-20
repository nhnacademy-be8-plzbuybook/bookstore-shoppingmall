package com.nhnacademy.book.cart.repository;

import com.nhnacademy.book.member.domain.Cart;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class CartRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CartRepository cartRepository;

    private Member member;
    private Cart cart;
    private MemberGrade memberGrade;
    private MemberStatus memberStatus;


    @BeforeEach
    void setUp() {
        memberGrade = new MemberGrade();
        memberGrade.setMemberGradeName("NORMAL");
        memberGrade.setConditionPrice(BigDecimal.valueOf(100000));
        memberGrade.setGradeChange(LocalDateTime.now());
        memberGrade = entityManager.persist(memberGrade);

        memberStatus = new MemberStatus();
        memberStatus.setMemberStateName("ACTIVE");
        memberStatus = entityManager.persist(memberStatus);

        member = Member.builder()
                .name("Test User")
                .phone("010-1234-5678")
                .email("test@test.com")
                .birth(LocalDate.of(1990, 1, 1))
                .password("password123")
                .memberGrade(memberGrade)
                .memberStatus(memberStatus)
                .build();
        member = entityManager.persist(member);

        // Cart 설정
        cart = new Cart(member);
        cart = entityManager.persist(cart);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("멤버 ID로 장바구니 찾기 성공 테스트")
    void findByMember_MemberId_Success() {
        // when
        Optional<Cart> foundCart = cartRepository.findByMember_MemberId(member.getMemberId());

        // then
        assertThat(foundCart).isPresent();
        assertThat(foundCart.get().getMember().getMemberId()).isEqualTo(member.getMemberId());
    }

    @Test
    @DisplayName("존재하지 않는 멤버 ID로 장바구니 찾기 테스트")
    void findByMember_MemberId_NotFound() {
        // when
        Optional<Cart> foundCart = cartRepository.findByMember_MemberId(999L);

        // then
        assertThat(foundCart).isEmpty();
    }

    @Test
    @DisplayName("장바구니 저장 테스트")
    void save_Success() {
        // given
        Member newMember = Member.builder()
                .name("New User")
                .phone("010-9876-5432")
                .email("new@test.com")
                .birth(LocalDate.of(1995, 1, 1))
                .password("password456")
                .memberGrade(memberGrade)
                .memberStatus(memberStatus)
                .build();
        newMember = entityManager.persist(newMember);

        Cart newCart = new Cart(newMember);

        // when
        Cart savedCart = cartRepository.save(newCart);

        // then
        assertThat(savedCart.getCartId()).isNotNull();
        assertThat(savedCart.getMember()).isEqualTo(newMember);
    }

    @Test
    @DisplayName("장바구니 삭제 테스트")
    void delete_Success() {
        // when
        cartRepository.delete(cart);
        entityManager.flush();
        entityManager.clear();

        // then
        Optional<Cart> deletedCart = cartRepository.findById(cart.getCartId());
        assertThat(deletedCart).isEmpty();
    }

    @Test
    @DisplayName("장바구니 ID로 존재 여부 확인 테스트")
    void existsById_Success() {
        // when
        boolean exists = cartRepository.existsById(cart.getCartId());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 장바구니 ID로 존재 여부 확인 테스트")
    void existsById_NotFound() {
        // when
        boolean exists = cartRepository.existsById(999L);

        // then
        assertThat(exists).isFalse();
    }
}

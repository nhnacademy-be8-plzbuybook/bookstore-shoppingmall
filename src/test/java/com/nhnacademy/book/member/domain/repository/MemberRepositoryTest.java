package com.nhnacademy.book.member.domain.repository;

import com.nhnacademy.book.config.JpaRepositoryConfig;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(JpaRepositoryConfig.class)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberGradeRepository memberGradeRepository;

    @Autowired
    MemberStatusRepository memberStatusRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @Transactional
    @DisplayName("이메일로 회원, 등급, 상태를 조회하는 테스트")
    void testFindByEmailWithGradeAndStatus() {
        // Given: 테스트 데이터 준비
        MemberGrade grade = new MemberGrade(null, "NORMAL", BigDecimal.valueOf(1000), LocalDateTime.now());
        MemberStatus status = new MemberStatus(null, "ACTIVE");

        memberGradeRepository.save(grade);
        memberStatusRepository.save(status);

        Member member = new Member(
                null,
                grade,
                status,
                "윤지호",
                "010-7237-3951",
                "yoonwlgh12@naver.com",
                LocalDate.of(2000, 3, 9),
                "password"
        );
        memberRepository.save(member);

        entityManager.flush();
        entityManager.clear();

        // When: 이메일로 회원 조회
        Optional<Member> optionalMember = memberRepository.findByEmailWithGradeAndStatus("yoonwlgh12@naver.com");

        // Then: 조회 결과 확인
        assertTrue(optionalMember.isPresent());

        Member fetchedMember = optionalMember.get();
        assertEquals("yoonwlgh12@naver.com", fetchedMember.getEmail());
        assertEquals("NORMAL", fetchedMember.getMemberGrade().getMemberGradeName());
        assertEquals("ACTIVE", fetchedMember.getMemberStatus().getMemberStateName());
    }

    @Test
    @DisplayName("이메일로 회원 조회 테스트")
    void testFindByEmail() {
        // Given: 테스트 데이터 준비
        MemberGrade grade = new MemberGrade(null, "NORMAL", BigDecimal.valueOf(1000), LocalDateTime.now());
        MemberStatus status = new MemberStatus(null, "ACTIVE");

        memberGradeRepository.save(grade);
        memberStatusRepository.save(status);

        Member member = new Member(
                null,
                grade,
                status,
                "Jane Doe",
                "010-9876-5432",
                "jane@example.com",
                LocalDate.of(1995, 5, 20),
                "password"
        );
        memberRepository.save(member);

        // When: 이메일로 회원 조회
        Optional<Member> optionalMember = memberRepository.findByEmail("jane@example.com");

        // Then: 조회 결과 확인
        assertTrue(optionalMember.isPresent());

        Member fetchedMember = optionalMember.get();
        assertEquals("jane@example.com", fetchedMember.getEmail());
        assertEquals("Jane Doe", fetchedMember.getName());
    }

    @Test
    @DisplayName("회원 이메일 존재 여부 확인 테스트")
    void testExistsByEmail() {
        // Given: 테스트 데이터 준비
        MemberGrade grade = new MemberGrade(null, "NORMAL", BigDecimal.valueOf(1000), LocalDateTime.now());
        MemberStatus status = new MemberStatus(null, "ACTIVE");

        memberGradeRepository.save(grade);
        memberStatusRepository.save(status);

        Member member = new Member(
                null,
                grade,
                status,
                "John Smith",
                "010-1111-2222",
                "john@example.com",
                LocalDate.of(1985, 3, 15),
                "password"
        );
        memberRepository.save(member);

        // When: 이메일 존재 여부 확인
        boolean exists = memberRepository.existsByEmail("john@example.com");

        // Then: 확인
        assertTrue(exists);
    }
}
package com.nhnacademy.book.member.domain.service.Impl;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.dto.MemberCreateRequestDto;
import com.nhnacademy.book.member.domain.dto.MemberCreateResponseDto;
import com.nhnacademy.book.member.domain.exception.*;
import com.nhnacademy.book.member.domain.repository.MemberGradeRepository;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.member.domain.repository.MemberStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@SpringBootTest
@Slf4j
@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {


    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberGradeRepository memberGradeRepository;

    @Mock
    private MemberStatusRepository memberStatusRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @InjectMocks
    private MemberServiceImpl memberService;

    @BeforeEach
    void setUp() {
    }



    @Test
    //회원 가입 성공
    void createMember_Success() {
        //given
        MemberCreateRequestDto memberCreateRequestDto = new MemberCreateRequestDto();
        memberCreateRequestDto.setMemberGradeId(1L);
        memberCreateRequestDto.setMemberStateId(1L);
        memberCreateRequestDto.setName("윤지호");
        memberCreateRequestDto.setPhone("010-7237-3951");
        memberCreateRequestDto.setEmail("yonnwlgh12@naver.com");
        memberCreateRequestDto.setBirth(LocalDate.now());
        memberCreateRequestDto.setPassword("123456");

        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com",LocalDate.now(),"encodedPassword");

        //mocking
        when(memberRepository.existsByEmail(memberCreateRequestDto.getEmail())).thenReturn(false);
        when(memberGradeRepository.findById(memberCreateRequestDto.getMemberGradeId())).thenReturn(Optional.of(memberGrade));
        when(memberStatusRepository.findById(memberCreateRequestDto.getMemberStateId())).thenReturn(Optional.of(memberStatus));
        when(passwordEncoder.encode(memberCreateRequestDto.getPassword())).thenReturn("encodedPassword");
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        //when
        var response = memberService.createMember(memberCreateRequestDto);

        //then
        assertNotNull(response);
        assertEquals("윤지호", response.getName());
        assertEquals("010-7237-3951", response.getPhone());
        assertEquals("yoonwlgh12@naver.com", response.getEmail());

    }

    @Test
    //회원 가입시 중복 이메일
    void createMember_DuplicateEmailException() {
        //given
        MemberCreateRequestDto memberCreateRequestDto = new MemberCreateRequestDto();
        memberCreateRequestDto.setMemberGradeId(1L);
        memberCreateRequestDto.setMemberStateId(1L);
        memberCreateRequestDto.setName("윤지호");
        memberCreateRequestDto.setPhone("010-7237-3951");
        memberCreateRequestDto.setEmail("yonnwlgh12@naver.com");
        memberCreateRequestDto.setBirth(LocalDate.now());
        memberCreateRequestDto.setPassword("123456");


        //mocking
        when(memberRepository.existsByEmail(memberCreateRequestDto.getEmail())).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> memberService.createMember(memberCreateRequestDto));
    }

    //회원 등급 없을 때
    @Test
    void createMember_MemberGradeNotFoundException() {
        //given
        MemberCreateRequestDto memberCreateRequestDto = new MemberCreateRequestDto();
        memberCreateRequestDto.setMemberGradeId(1L);
        memberCreateRequestDto.setMemberStateId(1L);
        memberCreateRequestDto.setName("윤지호");
        memberCreateRequestDto.setPhone("010-7237-3951");
        memberCreateRequestDto.setEmail("yonnwlgh12@naver.com");
        memberCreateRequestDto.setBirth(LocalDate.now());
        memberCreateRequestDto.setPassword("123456");

        //mocking
        when(memberGradeRepository.findById(memberCreateRequestDto.getMemberGradeId())).thenReturn(Optional.empty());

        assertThrows(MemberGradeNotFoundException.class, () -> memberService.createMember(memberCreateRequestDto));


    }

    //회원 상태 없을 때
    @Test
    void createMember_MemberStatusNotFoundException() {
        //given
        MemberCreateRequestDto memberCreateRequestDto = new MemberCreateRequestDto();
        memberCreateRequestDto.setMemberGradeId(1L);
        memberCreateRequestDto.setMemberStateId(1L);
        memberCreateRequestDto.setName("윤지호");
        memberCreateRequestDto.setPhone("010-7237-3951");
        memberCreateRequestDto.setEmail("yonnwlgh12@naver.com");
        memberCreateRequestDto.setBirth(LocalDate.now());
        memberCreateRequestDto.setPassword("123456");

        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());

        //mocking
        when(memberGradeRepository.findById(memberCreateRequestDto.getMemberGradeId())).thenReturn(Optional.of(memberGrade));
        when(memberStatusRepository.findById(memberCreateRequestDto.getMemberStateId())).thenReturn(Optional.empty());

        assertThrows(MemberStatusNotFoundException.class, () -> memberService.createMember(memberCreateRequestDto));

    }

    //비밀번호 암호화 되어있나
    @Test
    void createMember_PasswordIsEncoded() {
        // given
        MemberCreateRequestDto memberCreateRequestDto = new MemberCreateRequestDto();
        memberCreateRequestDto.setMemberGradeId(1L);
        memberCreateRequestDto.setMemberStateId(1L);
        memberCreateRequestDto.setName("윤지호");
        memberCreateRequestDto.setPhone("010-7237-3951");
        memberCreateRequestDto.setEmail("yonnwlgh12@naver.com");
        memberCreateRequestDto.setBirth(LocalDate.now());
        memberCreateRequestDto.setPassword("123456");

        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");

        when(memberGradeRepository.findById(memberCreateRequestDto.getMemberGradeId())).thenReturn(Optional.of(memberGrade));
        when(memberStatusRepository.findById(memberCreateRequestDto.getMemberStateId())).thenReturn(Optional.of(memberStatus));
        when(passwordEncoder.encode(memberCreateRequestDto.getPassword())).thenReturn("encodedPassword");

        Member savedMember = new Member();
        savedMember.setPassword("encodedPassword");

        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);

        // when
        memberService.createMember(memberCreateRequestDto);

        // then
        verify(memberRepository).save(captor.capture());
        Member capturedMember = captor.getValue();
        assertNotNull(capturedMember);  // capturedMember가 null이 아닌지 확인
        assertEquals("encodedPassword", capturedMember.getPassword()); // 비밀번호 암호화 확인
    }


    //default가 normal, active인가
    @Test
    void testMemberCreationWithDefaultValues() {
        // given
        MemberCreateRequestDto memberCreateRequestDto = new MemberCreateRequestDto();
        memberCreateRequestDto.setName("test");
        memberCreateRequestDto.setPhone("010-1234-5678");
        memberCreateRequestDto.setEmail("test@naver.com");
        memberCreateRequestDto.setBirth(LocalDate.now());
        memberCreateRequestDto.setPassword("1234");

        // MemberGrade ID와 MemberStatus ID를 설정
        memberCreateRequestDto.setMemberGradeId(1L);  // MemberGrade ID 설정
        memberCreateRequestDto.setMemberStateId(1L);  // MemberState ID 설정

        // MemberGrade와 MemberStatus 설정
        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");

        when(memberGradeRepository.findById(1L)).thenReturn(Optional.of(memberGrade));
        when(memberStatusRepository.findById(1L)).thenReturn(Optional.of(memberStatus));

        // 실제 회원 정보
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com", LocalDate.now(), "encodedPassword");

        // save 메서드 mock
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        // when
        var response = memberService.createMember(memberCreateRequestDto);

        // then
        // response가 null이 아니고, 정상적으로 "NORMAL"과 "ACTIVE" 값이 저장되었는지 확인
        assertNotNull(response);
        assertNotNull(response.getMemberGradeName());
        assertNotNull(response.getMemberStateName());

        // 기본값으로 'NORMAL'과 'ACTIVE'가 설정된 값이 맞는지 확인
        assertEquals("NORMAL", response.getMemberGradeName());  // MemberGrade가 NORMAL로 설정되었는지 확인
        assertEquals("ACTIVE", response.getMemberStateName());  // MemberStatus가 ACTIVE로 설정되었는지 확인
    }

    //default grade 조회를 실패 했을 시
    @Test
    void creteMember_memberGradeNotFoundException() {
        //given
        MemberCreateRequestDto memberCreateRequestDto = new MemberCreateRequestDto();
        memberCreateRequestDto.setMemberGradeId(1L);
        memberCreateRequestDto.setMemberStateId(1L);
        memberCreateRequestDto.setName("윤지호");
        memberCreateRequestDto.setPhone("010-7237-3951");
        memberCreateRequestDto.setEmail("yonnwlgh12@naver.com");
        memberCreateRequestDto.setBirth(LocalDate.now());
        memberCreateRequestDto.setPassword("123456");

        //mocking
        when(memberGradeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MemberGradeNotFoundException.class, () -> memberService.createMember(memberCreateRequestDto));
    }

   //default 상태가 없을 때 오류 처리
    @Test
    void createMember_memberStatusNotFoundException() {
        //given
        MemberCreateRequestDto memberCreateRequestDto = new MemberCreateRequestDto();
        memberCreateRequestDto.setMemberGradeId(1L);
        memberCreateRequestDto.setMemberStateId(1L);
        memberCreateRequestDto.setName("윤지호");
        memberCreateRequestDto.setPhone("010-7237-3951");
        memberCreateRequestDto.setEmail("yonnwlgh12@naver.com");
        memberCreateRequestDto.setBirth(LocalDate.now());
        memberCreateRequestDto.setPassword("123456");

        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());

        //mocking
        when(memberGradeRepository.findById(1L)).thenReturn(Optional.of(memberGrade));
        when(memberStatusRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MemberStatusNotFoundException.class, () -> memberService.createMember(memberCreateRequestDto));
    }

    //이메일로 회원을 조회할 때 값이 잘 나오는 지
    @Test
    void getMemberByEmail() {
        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com",LocalDate.now(),"Password");

        when(memberRepository.findByEmail("yoonwlgh12@naver.com")).thenReturn(Optional.of(member));
        when(passwordEncoder.encode(member.getPassword())).thenReturn("encodedPassword");

        var response = memberService.getMemberByEmail("yoonwlgh12@naver.com");

        assertNotNull(response);
        assertEquals("yoonwlgh12@naver.com", response.getEmail());
        assertEquals("encodedPassword", response.getPassword());

        verify(memberRepository).findByEmail("yoonwlgh12@naver.com");
        verify(passwordEncoder).encode(member.getPassword());
    }

    //이메일로 회원을 조회할 때 예외 처리
    @Test
    void getMemberByEmail_MemberEmailNotFoundException() {
        String email = "yoonwlgh12@naver.com";
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(MemberEmailNotFoundException.class, () -> memberService.getMemberByEmail(email));

        verify(memberRepository).findByEmail(email);
    }

    //id 로 특정 회원 조회 잘 되는지
    @Test
    void getMemberById() {
        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com",LocalDate.of(2000, 3, 9),"Password");

        //id로 멤버 조회
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        var response = memberService.getMemberById(1L);

        assertNotNull(response);
        assertEquals("윤지호", response.getName());
        assertEquals("010-7237-3951", response.getPhone());
        assertEquals("yoonwlgh12@naver.com", response.getEmail());
        assertEquals(LocalDate.of(2000, 3, 9), response.getBirth());

        assertEquals("NORMAL", response.getMemberGradeName());
        assertEquals("ACTIVE", response.getMemberStateName());

        verify(memberRepository).findById(1L);
    }


    //id로 회원 조회할 때 해당하는 아이디 없으면 예외처리 잘하는지
    @Test
    void getMemberById_MemberIdNotFoundException() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MemberIdNotFoundException.class, () -> memberService.getMemberById(1L));

        verify(memberRepository).findById(1L);
    }
}


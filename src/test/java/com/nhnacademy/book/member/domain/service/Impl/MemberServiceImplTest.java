package com.nhnacademy.book.member.domain.service.Impl;

import com.nhnacademy.book.member.domain.*;
import com.nhnacademy.book.member.domain.dto.*;
import com.nhnacademy.book.member.domain.exception.*;
import com.nhnacademy.book.member.domain.repository.MemberGradeRepository;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.member.domain.repository.MemberStatusRepository;
import com.nhnacademy.book.member.domain.repository.auth.MemberAuthRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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
    private MemberAuthRepository memberAuthRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @InjectMocks
    private MemberServiceImpl memberService;

    @BeforeEach
    void setUp() {
    }



    @Test
    @DisplayName("회원 가입 성공")
    void createMember_Success() {
        //given
        MemberCreateRequestDto memberCreateRequestDto = new MemberCreateRequestDto();
        memberCreateRequestDto.setName("윤지호");
        memberCreateRequestDto.setPhone("010-7237-3951");
        memberCreateRequestDto.setEmail("yonnwlgh12@naver.com");
        memberCreateRequestDto.setBirth(LocalDate.now());
        memberCreateRequestDto.setPassword("123456");

        // 기본 등급 및 기본 상태 설정
        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com", LocalDate.now(), "encodedPassword");

        //mocking
        when(memberRepository.existsByEmail(memberCreateRequestDto.getEmail())).thenReturn(false);
        when(memberGradeRepository.findById(1L)).thenReturn(Optional.of(memberGrade)); // 기본 등급 mock 설정
        when(memberStatusRepository.findById(1L)).thenReturn(Optional.of(memberStatus)); // 기본 상태 mock 설정
        when(passwordEncoder.encode(memberCreateRequestDto.getPassword())).thenReturn("encodedPassword");
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        //when
        var response = memberService.createMember(memberCreateRequestDto);

        //then
        assertNotNull(response); // 응답이 null이 아닌지 확인
        assertEquals("윤지호", response.getName()); // 이름 확인
        assertEquals("010-7237-3951", response.getPhone()); // 전화번호 확인
        assertEquals("yoonwlgh12@naver.com", response.getEmail()); // 이메일 확인
        assertEquals("NORMAL", response.getMemberGradeName()); // 기본 등급 이름 확인
        assertEquals("ACTIVE", response.getMemberStateName()); // 기본 상태 이름 확인
    }

    @Test
    @DisplayName("회원 가입시 중복 이메일")
    void createMember_DuplicateEmailException() {
        //given
        MemberCreateRequestDto memberCreateRequestDto = new MemberCreateRequestDto();
        memberCreateRequestDto.setName("윤지호");
        memberCreateRequestDto.setPhone("010-7237-3951");
        memberCreateRequestDto.setEmail("yonnwlgh12@naver.com");
        memberCreateRequestDto.setBirth(LocalDate.now());
        memberCreateRequestDto.setPassword("123456");

        //mocking
        when(memberRepository.existsByEmail(memberCreateRequestDto.getEmail())).thenReturn(true);

        //when, then
        assertThrows(DuplicateEmailException.class, () -> memberService.createMember(memberCreateRequestDto));
    }


    @Test
    @DisplayName("회원 등급 없을 때")
    void createMember_MemberGradeNotFoundException() {
        //given
        MemberCreateRequestDto memberCreateRequestDto = new MemberCreateRequestDto();
        memberCreateRequestDto.setName("윤지호");
        memberCreateRequestDto.setPhone("010-7237-3951");
        memberCreateRequestDto.setEmail("yonnwlgh12@naver.com");
        memberCreateRequestDto.setBirth(LocalDate.now());
        memberCreateRequestDto.setPassword("123456");

        //mocking - 기본 등급 조회 실패 Mock
        when(memberGradeRepository.findById(1L)).thenReturn(Optional.empty());

        //when, then
        assertThrows(DefaultMemberGradeNotFoundException.class, () -> memberService.createMember(memberCreateRequestDto));
    }


    @Test
    @DisplayName("회원 상태 없을 때")
    void createMember_MemberStatusNotFoundException() {
        //given
        MemberCreateRequestDto memberCreateRequestDto = new MemberCreateRequestDto();
        memberCreateRequestDto.setName("윤지호");
        memberCreateRequestDto.setPhone("010-7237-3951");
        memberCreateRequestDto.setEmail("yonnwlgh12@naver.com");
        memberCreateRequestDto.setBirth(LocalDate.now());
        memberCreateRequestDto.setPassword("123456");

        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());

        //mocking - 기본 상태 조회 실패 Mock
        when(memberGradeRepository.findById(1L)).thenReturn(Optional.of(memberGrade));
        when(memberStatusRepository.findById(1L)).thenReturn(Optional.empty());

        //when, then
        assertThrows(DefaultStatusGradeNotfoundException.class, () -> memberService.createMember(memberCreateRequestDto));
    }


    @Test
    @DisplayName("비밀번호 암호화 되어있나")
    void createMember_PasswordIsEncoded() {
        // given
        MemberCreateRequestDto memberCreateRequestDto = new MemberCreateRequestDto();
        memberCreateRequestDto.setName("윤지호");
        memberCreateRequestDto.setPhone("010-7237-3951");
        memberCreateRequestDto.setEmail("yonnwlgh12@naver.com");
        memberCreateRequestDto.setBirth(LocalDate.now());
        memberCreateRequestDto.setPassword("123456");

        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");

        //mocking
        when(memberGradeRepository.findById(1L)).thenReturn(Optional.of(memberGrade)); // 기본 등급
        when(memberStatusRepository.findById(1L)).thenReturn(Optional.of(memberStatus)); // 기본 상태
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


    @Test
    @DisplayName("default가 normal, active인가")
    void testMemberCreationWithDefaultValues() {
        // given
        MemberCreateRequestDto memberCreateRequestDto = new MemberCreateRequestDto();
        memberCreateRequestDto.setName("test");
        memberCreateRequestDto.setPhone("010-1234-5678");
        memberCreateRequestDto.setEmail("test@naver.com");
        memberCreateRequestDto.setBirth(LocalDate.now());
        memberCreateRequestDto.setPassword("1234");


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


    @Test
    @DisplayName("default grade 조회를 실패 했을 시")
    void creteMember_memberGradeNotFoundException() {
        //given
        MemberCreateRequestDto memberCreateRequestDto = new MemberCreateRequestDto();
        memberCreateRequestDto.setName("윤지호");
        memberCreateRequestDto.setPhone("010-7237-3951");
        memberCreateRequestDto.setEmail("yonnwlgh12@naver.com");
        memberCreateRequestDto.setBirth(LocalDate.now());
        memberCreateRequestDto.setPassword("123456");

        //mocking
        when(memberGradeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DefaultMemberGradeNotFoundException.class, () -> memberService.createMember(memberCreateRequestDto));
    }

    @Test
    @DisplayName("default 상태가 없을 때 오류 처리")
    void createMember_memberStatusNotFoundException() {
        //given
        MemberCreateRequestDto memberCreateRequestDto = new MemberCreateRequestDto();
        memberCreateRequestDto.setName("윤지호");
        memberCreateRequestDto.setPhone("010-7237-3951");
        memberCreateRequestDto.setEmail("yonnwlgh12@naver.com");
        memberCreateRequestDto.setBirth(LocalDate.now());
        memberCreateRequestDto.setPassword("123456");

        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());

        //mocking
        when(memberGradeRepository.findById(1L)).thenReturn(Optional.of(memberGrade));
        when(memberStatusRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DefaultStatusGradeNotfoundException.class, () -> memberService.createMember(memberCreateRequestDto));
    }


    @Test
    @DisplayName("이메일로 회원을 조회할 때 값이 잘 나오는 지")
    void getMemberByEmail() {
        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Auth auth = new Auth(1L, "ADMIN");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com",LocalDate.now(),"Password");
        MemberAuth memberAuth = new MemberAuth(1L, auth, member);
        List<MemberAuth> memberAuthList = List.of(memberAuth);

        when(memberRepository.findByEmail("yoonwlgh12@naver.com")).thenReturn(Optional.of(member));
        when(memberAuthRepository.findByMember(member)).thenReturn(memberAuthList);

        var response = memberService.getMemberByEmail("yoonwlgh12@naver.com");

        assertNotNull(response);
        assertEquals("yoonwlgh12@naver.com", response.getEmail());
        assertEquals("ADMIN", response.getAuthName());

        verify(memberRepository).findByEmail("yoonwlgh12@naver.com");
        verify(memberAuthRepository).findByMember(member);
    }

    @Test
    @DisplayName("비어 있는 권한 리스트 예외 처리 테스트")
    void getMemberByEmail_exception() {
        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com",LocalDate.now(),"Password");

        when(memberRepository.findByEmail("yoonwlgh12@naver.com")).thenReturn(Optional.of(member));
        when(memberAuthRepository.findByMember(member)).thenReturn(List.of());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> memberService.getMemberByEmail(member.getEmail()));

        assertEquals("해당 멤버에 대한 권한 정보를 찾을 수 없습니다", exception.getMessage());

        verify(memberRepository).findByEmail(member.getEmail());
        verify(memberAuthRepository).findByMember(member);
        verifyNoInteractions(passwordEncoder);



    }

    @Test
    @DisplayName("이메일로 회원을 조회할 때 예외 처리")
    void getMemberByEmail_MemberEmailNotFoundException() {
        String email = "yoonwlgh12@naver.com";
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(MemberEmailNotFoundException.class, () -> memberService.getMemberByEmail(email));

        verify(memberRepository).findByEmail(email);
    }

    @Test
    @DisplayName("id 로 특정 회원 조회 잘 되는지")
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


    @Test
    @DisplayName("id로 회원 조회할 때 해당하는 아이디 없으면 예외처리 잘하는지")
    void getMemberById_MemberIdNotFoundException() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MemberIdNotFoundException.class, () -> memberService.getMemberById(1L));

        verify(memberRepository).findById(1L);
    }

    @Test
    @DisplayName("회원 수정을 잘하는지")
    void memberModify_success() {
        Long memberId = 1L;
        Member member = new Member();
        member.setName("윤지호");
        member.setPhone("010-7237-3951");
        member.setEmail("yoonwlgh12@naver.com");
        member.setBirth(LocalDate.of(2000, 3, 9));
        member.setPassword("encodedPassword");

        MemberModifyRequestDto memberModifyRequestDto = new MemberModifyRequestDto();
        memberModifyRequestDto.setName("융징홍");
        memberModifyRequestDto.setPhone("010-1111-1111");
        memberModifyRequestDto.setEmail("yoonwlgh123@naver.com");
        memberModifyRequestDto.setBirth(LocalDate.of(2000, 3, 10));
        memberModifyRequestDto.setPassword("newPassword");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(memberRepository.existsByEmail(memberModifyRequestDto.getEmail())).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = memberService.modify(memberId, memberModifyRequestDto);

        assertEquals("융징홍", response.getName());
        assertEquals("010-1111-1111", response.getPhone());
        assertEquals("yoonwlgh123@naver.com", response.getEmail());
        assertEquals(LocalDate.of(2000, 3, 10), response.getBirth());
        verify(passwordEncoder).encode("newPassword");
        verify(memberRepository).findById(memberId);
        verify(memberRepository,times(1)).save(any(Member.class));
    }


    @Test
    @DisplayName("회원 수정 값이 없을 때 예외를 잘 처리 하는지")
    void memberModify_DuplicateMemberModificationException() {
        Member member = new Member();
        member.setMemberId(1L);
        member.setName("윤지호");
        member.setPhone("010-7237-3951");
        member.setEmail("yoonwlgh12@naver.com");
        member.setBirth(LocalDate.of(2000, 3, 9));
        member.setPassword("EncodedPassword");

        MemberModifyRequestDto memberModifyRequestDto = new MemberModifyRequestDto();
        memberModifyRequestDto.setName(member.getName());
        memberModifyRequestDto.setPhone(member.getPhone());
        memberModifyRequestDto.setEmail(member.getEmail());
        memberModifyRequestDto.setBirth(member.getBirth());
        memberModifyRequestDto.setPassword("password");

       when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
       when(passwordEncoder.matches("password", "EncodedPassword")).thenReturn(true);

       assertThrows(DuplicateMemberModificationException.class, () -> memberService.modify(member.getMemberId(), memberModifyRequestDto));

       verify(memberRepository).findById(member.getMemberId());
       verify(passwordEncoder).matches("password", "EncodedPassword");

    }


    @Test
    @DisplayName("회원 수정하는데 중복된 이메일로 수정하려 할 떄")
    void memberModify_DuplicateEmailException() {
        Member member = new Member();
        member.setMemberId(1L);
        member.setName("윤지호");
        member.setPhone("010-7237-3951");
        member.setEmail("yoonwlgh12@naver.com");
        member.setBirth(LocalDate.of(2000, 3, 9));
        member.setPassword("EncodedPassword");

        MemberModifyRequestDto memberModifyRequestDto = new MemberModifyRequestDto();
        memberModifyRequestDto.setName("융징홍");
        memberModifyRequestDto.setPhone("010-1111-1111");
        memberModifyRequestDto.setEmail("duplicate@naver.com");
        memberModifyRequestDto.setBirth(LocalDate.of(2000, 3, 10));
        memberModifyRequestDto.setPassword("password");

        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(memberRepository.existsByEmail("duplicate@naver.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> memberService.modify(member.getMemberId(), memberModifyRequestDto));

    }

    @Test
    @DisplayName("삭제시 회원 상태가 withdraw로 변경 되는지")
    void withdrawMember_Success() {
        MemberStatus withdrawalStatus = new MemberStatus();
        withdrawalStatus.setMemberStateName("WITHDRAWAL");

        Member member = new Member();
        member.setMemberId(1L);
        member.setName("윤지호");
        member.setPhone("010-7237-3951");
        member.setEmail("yoonwlgh12@naver.com");
        member.setMemberStatus(new MemberStatus());

        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(memberStatusRepository.findByMemberStateName("WITHDRAWAL")).thenReturn(Optional.of(withdrawalStatus));

        memberService.withdrawMember(member.getMemberId());

        verify(memberRepository).save(any(Member.class));
        assertEquals("WITHDRAWAL", member.getMemberStatus().getMemberStateName());
    }


    @Test
    @DisplayName("withdraw 상태가 없으면 오류 발생 잘 시키는지")
    void withdrawMember_MemberGradeNotFoundException() {
        when(memberStatusRepository.findByMemberStateName("WITHDRAWAL")).thenReturn(Optional.empty());

        assertThrows(MemberGradeNotFoundException.class, () -> memberService.withdrawMember(1L));
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    @DisplayName("탈퇴하려는 id에 해당하는 회원을 불러올 때 없으면 오류 발생 잘 시키는 지")
    void withdrawMember_MemberNotFoundException() {
        MemberStatus withdrawalStatus = new MemberStatus();
        withdrawalStatus.setMemberStateName("WITHDRAWAL");

        when(memberStatusRepository.findByMemberStateName("WITHDRAWAL")).thenReturn(Optional.of(withdrawalStatus));
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MemberIdNotFoundException.class, () -> memberService.withdrawMember(1L));
        verify(memberRepository, never()).save(any(Member.class));
    }


    @Test
    @DisplayName("회원 조회를 성공적으로 하는가")
    void getMembers_success() {
       MemberSearchRequestDto memberSearchRequestDto = new MemberSearchRequestDto();
       memberSearchRequestDto.setPage(0);
       memberSearchRequestDto.setSize(10);

        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com",LocalDate.of(2000, 3, 9),"Password");
        Member member2 = new Member(2L, memberGrade, memberStatus, "윤지호2", "010-7237-3952", "yoonwlgh123@naver.com",LocalDate.of(2001, 3, 9),"Password");

        Page<Member> page = new PageImpl<>(List.of(member, member2));

        when(memberRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<MemberSearchResponseDto> response = memberService.getMembers(memberSearchRequestDto);

        assertNotNull(response);
        assertEquals(2, response.getContent().size());
        assertEquals("윤지호", response.getContent().getFirst().getName());
        assertEquals("010-7237-3951", response.getContent().getFirst().getPhone());
        assertEquals("윤지호2", response.getContent().get(1).getName());
        assertEquals("010-7237-3952", response.getContent().get(1).getPhone());

    }

    @Test
    @DisplayName("회원 조회할 값이 없을 시 예외 처리를 잘하는가")
    void getMembers_MemberNotFoundException() {
        MemberSearchRequestDto memberSearchRequestDto = new MemberSearchRequestDto();
        memberSearchRequestDto.setPage(0);
        memberSearchRequestDto.setSize(10);

        Page<Member> page = Page.empty();


        when(memberRepository.findAll(any(Pageable.class))).thenReturn(page);

        MemberNotFoundException exception = assertThrows(MemberNotFoundException.class, () -> {
            memberService.getMembers(memberSearchRequestDto);
        });

        assertEquals("등록된 회원이 없다!", exception.getMessage());

    }
}


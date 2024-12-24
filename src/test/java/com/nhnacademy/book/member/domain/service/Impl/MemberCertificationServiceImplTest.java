package com.nhnacademy.book.member.domain.service.Impl;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberCertification;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.dto.certification.*;
import com.nhnacademy.book.member.domain.dto.certification.CertificationCreateRequestDto;
import com.nhnacademy.book.member.domain.dto.certification.CertificationCreateResponseDto;
import com.nhnacademy.book.member.domain.exception.*;
import com.nhnacademy.book.member.domain.repository.MemberCertificationRepository;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberCertificationServiceImplTest {

    @Mock
    private MemberCertificationRepository memberCertificationRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberCertificationServiceImpl memberCertificationService;

    @Test
    @DisplayName("회원 인증 생성")
    void createMemberCertification() {
        CertificationCreateRequestDto certificationCreateRequestDto = new CertificationCreateRequestDto();
        certificationCreateRequestDto.setMemberId(1L);
        certificationCreateRequestDto.setCertification("일반");

        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com", LocalDate.now(),"encodedPassword");

        //회원 인증 생성 요청에서 받아온 아이디에 해당하는 member return
        when(memberRepository.findById(certificationCreateRequestDto.getMemberId())).thenReturn(Optional.of(member));

        CertificationCreateResponseDto certificationCreateResponseDto = memberCertificationService.addCertification(certificationCreateRequestDto);

        assertAll(
                () -> assertNotNull(certificationCreateResponseDto),
                () -> assertEquals(certificationCreateRequestDto.getMemberId(), certificationCreateResponseDto.getMemberId()),
                () -> assertEquals(certificationCreateRequestDto.getCertification(), certificationCreateResponseDto.getCertification())
        );

        verify(memberRepository, times(1)).findById(certificationCreateRequestDto.getMemberId());
        verify(memberCertificationRepository, times(1)).save(any(MemberCertification.class));

    }

    @Test
    @DisplayName("중복 회원 인증 생성시 예외")
    void createMemberCertification_duplicateMemberCertification() {
        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com", LocalDate.now(),"encodedPassword");

        MemberCertification memberCertification = new MemberCertification();
        memberCertification.setMemberAuthId(1L);
        memberCertification.setMember(member);
        memberCertification.setLastLogin(LocalDateTime.now());
        memberCertification.setCertificationMethod("일반");

        CertificationCreateRequestDto certificationCreateRequestDto = new CertificationCreateRequestDto();
        certificationCreateRequestDto.setMemberId(1L);
        certificationCreateRequestDto.setCertification("일반");
        when(memberRepository.findById(certificationCreateRequestDto.getMemberId())).thenReturn(Optional.of(member));
        when(memberCertificationRepository.existsByCertificationMethod(certificationCreateRequestDto.getCertification())).thenReturn(true);

        assertThrows(DuplicateCertificationException.class,
                () -> memberCertificationService.addCertification(certificationCreateRequestDto),
                "중복된 인증 생성 시 DuplicateCertificationException이 발생해야 함");

        verify(memberCertificationRepository, never()).save(any(MemberCertification.class));
    }

    @Test
    @DisplayName("회원 인증 생성시 회원 Id로 회원을 가져올 때 오류 발생하는가")
    void createMemberCertification_MemberNotFound() {
        when(memberRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class,
                () -> memberCertificationService.addCertification(new CertificationCreateRequestDto())
                );

        verify(memberCertificationRepository, never()).save(any(MemberCertification.class));
    }

    @Test
    @DisplayName("회원 ID로 인증 정보 조회 성공")
    void getMemberCertificationByMemberId_success() {
        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com", LocalDate.now(),"encodedPassword");

        MemberCertification memberCertification = new MemberCertification();
        memberCertification.setMemberAuthId(1L);
        memberCertification.setMember(member);
        memberCertification.setLastLogin(LocalDateTime.now());
        memberCertification.setCertificationMethod("일반");

        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(memberCertificationRepository.findByMember_MemberId(member.getMemberId())).thenReturn(Optional.of(memberCertification));

        CertificationResponseDto certificationResponseDto = memberCertificationService.getCertificationByMemberId(member.getMemberId());

        assertAll(
                () -> assertNotNull(certificationResponseDto),
                () -> assertEquals(member.getMemberId(), certificationResponseDto.getMemberId()),
                () -> assertEquals("일반", certificationResponseDto.getCertification())
        );

        verify(memberRepository, times(1)).findById(member.getMemberId());
        verify(memberCertificationRepository, times(1)).findByMember_MemberId(member.getMemberId());

    }

    @Test
    @DisplayName("회원 ID로 인증 정보 조회 시 없는 회원ID를 가져올 때 오류 처리 하는가")
    void getMemberCertificationByMemberId_MemberIdNotFound() {
        Long memberId = 1L;
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        assertThrows(MemberIdNotFoundException.class,
                () -> memberCertificationService.getCertificationByMemberId(memberId)
                );
    }

    @Test
    @DisplayName("인증정보가 없는 조회시 오류 처리 하는가")
    void getMemberCertificationByMemberId_MemberCertificationNotFound() {
        Long memberId = 1L;
        Member member = new Member();
        member.setMemberId(memberId);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberCertificationRepository.findByMember_MemberId(memberId)).thenReturn(Optional.empty());

        assertThrows(CertificationNotFoundException.class,
                () -> memberCertificationService.getCertificationByMemberId(memberId)
                );

        verify(memberCertificationRepository, times(1)).findByMember_MemberId(memberId);
        verify(memberRepository, times(1)).findById(memberId);
    }

    @Test
    @DisplayName("인증정보 변경 성공")
    void updateMemberCertification_success() {
        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com", LocalDate.now(),"encodedPassword");

        MemberCertification memberCertification = new MemberCertification();
        memberCertification.setMemberAuthId(1L);
        memberCertification.setMember(member);
        memberCertification.setLastLogin(LocalDateTime.now());
        memberCertification.setCertificationMethod("일반");

       CertificationUpdateRequestDto certificationUpdateRequestDto = new CertificationUpdateRequestDto();
       certificationUpdateRequestDto.setCertification("페이코");

       when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
       when(memberCertificationRepository.findByMember_MemberId(member.getMemberId())).thenReturn(Optional.of(memberCertification));

       CertificationUpdateResponseDto certificationUpdateResponseDto = memberCertificationService.updateCertificationMethod(member.getMemberId(), certificationUpdateRequestDto);

       assertAll(
               () -> assertNotNull(certificationUpdateResponseDto),
               () -> assertEquals(member.getMemberId(), certificationUpdateResponseDto.getMemberId()),
               () -> assertEquals("페이코", certificationUpdateResponseDto.getCertification())
       );

       verify(memberRepository, times(1)).findById(member.getMemberId());
       verify(memberCertificationRepository, times(1)).findByMember_MemberId(member.getMemberId());
       verify(memberCertificationRepository, times(1)).save(any(MemberCertification.class));



    }

    @DisplayName("인증 변경시 Id에 해당하는 member가 없을 시 예외")
    @Test
    void updateMemberCertification_MemberIdNotFound() {
        when(memberRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(MemberIdNotFoundException.class,
                () -> memberCertificationService.updateCertificationMethod(1L, new CertificationUpdateRequestDto())
                );
    }

    @DisplayName("인증 변경 시 해당 회원의 인증 정보가 없을 시 예외")
    @Test
    void updateMemberCertification_CertificationNotFound() {
        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com", LocalDate.now(),"encodedPassword");

        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(memberCertificationRepository.findByMember_MemberId(member.getMemberId())).thenReturn(Optional.empty());
        assertThrows(CertificationNotFoundException.class,
                () -> memberCertificationService.updateCertificationMethod(1L, new CertificationUpdateRequestDto())
        );
    }

    @DisplayName("유효하지 않은 인증으로 수정할 때 예외")
    @Test
    void updateMemberCertification_InvalidCertificationException() {
        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com", LocalDate.now(),"encodedPassword");

        MemberCertification memberCertification = new MemberCertification();
        memberCertification.setMemberAuthId(1L);
        memberCertification.setMember(member);
        memberCertification.setLastLogin(LocalDateTime.now());
        memberCertification.setCertificationMethod("일반");

        CertificationUpdateRequestDto certificationUpdateRequestDto = new CertificationUpdateRequestDto();
        certificationUpdateRequestDto.setCertification("유효하지 않는 인증법");

        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(memberCertificationRepository.findByMember_MemberId(member.getMemberId())).thenReturn(Optional.of(memberCertification));

        assertThrows(InvalidCertificationMethodException.class,
                () -> memberCertificationService.updateCertificationMethod(1L, certificationUpdateRequestDto)
        );
    }

    @Test
    @DisplayName("마지막 로그인 갱신 성공")
    void updateLastLogin_success() {
        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com", LocalDate.now(),"encodedPassword");

        MemberCertification memberCertification = new MemberCertification();
        memberCertification.setMemberAuthId(1L);
        memberCertification.setMember(member);
        memberCertification.setLastLogin(LocalDateTime.of(2000, 3, 8, 23, 59));
        memberCertification.setCertificationMethod("일반");

        UpdateLastLoginRequestDto updateLastLoginRequestDto = new UpdateLastLoginRequestDto();
        updateLastLoginRequestDto.setMemberId(1L);

        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(memberCertificationRepository.findByMember_MemberId(member.getMemberId())).thenReturn(Optional.of(memberCertification));
        //호출된 첫번째 인자에 임의의 시간을 저장해두고 return 한다
        when(memberCertificationRepository.save(any(MemberCertification.class))).thenAnswer(invocation -> {
            MemberCertification saved = invocation.getArgument(0);
            saved.setLastLogin(LocalDateTime.of(2000, 3, 9, 1, 1)); // 저장된 시간 설정
            return saved;
        });

        UpdateLastLoginResponseDto responseDto = memberCertificationService.updateLastLogin(updateLastLoginRequestDto);

        assertAll(
                () -> assertNotNull(responseDto),
                () -> assertEquals(1L, responseDto.getMemberId()),
                () -> assertEquals(LocalDateTime.of(2000,3,9,1,1), responseDto.getLastLogin())
        );

        verify(memberRepository, times(1)).findById(member.getMemberId());
        verify(memberCertificationRepository, times(1)).findByMember_MemberId(member.getMemberId());
        verify(memberCertificationRepository, times(1)).save(any(MemberCertification.class));


    }

    @Test
    @DisplayName("마지막 로그인 갱신시 id에 해당하는 회원이 없을 떄 예외")
    void updateLastLogin_memberIdNotFoundException() {
        UpdateLastLoginRequestDto updateLastLoginRequestDto = new UpdateLastLoginRequestDto();
        updateLastLoginRequestDto.setMemberId(1L);
        when(memberRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(MemberIdNotFoundException.class,
                () -> memberCertificationService.updateLastLogin(updateLastLoginRequestDto)
        );

    }

    @Test
    @DisplayName("마지막 로그인 갱신시 회원에 대항 인증 정보가 없을 때 예외")
    void updateLastLogin_CertificationNotFoundException() {
        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com", LocalDate.now(),"encodedPassword");

        UpdateLastLoginRequestDto updateLastLoginRequestDto = new UpdateLastLoginRequestDto();
        updateLastLoginRequestDto.setMemberId(1L);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberCertificationRepository.findByMember_MemberId(member.getMemberId())).thenReturn(Optional.empty());

        assertThrows(CertificationNotFoundException.class,
                () -> memberCertificationService.updateLastLogin(updateLastLoginRequestDto)
        );

    }

    @Test
    @DisplayName("인증 방식을 성공적으로 삭제")
    void deleteMemberCertification_success() {
        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com", LocalDate.now(),"encodedPassword");

        DeleteCertificationRequestDto deleteCertificationRequestDto = new DeleteCertificationRequestDto();
        deleteCertificationRequestDto.setMemberId(member.getMemberId());

        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(memberCertificationRepository.findByMember_MemberId(member.getMemberId())).thenReturn(Optional.of(new MemberCertification()));

        DeleteCertificationResponseDto deleteCertificationResponseDto = memberCertificationService.deleteCertification(deleteCertificationRequestDto);


        assertAll(
                () -> assertNotNull(deleteCertificationResponseDto),
                () -> assertTrue(deleteCertificationResponseDto.isSuccess()),
                () -> assertEquals("인증 방식이 삭제됨!", deleteCertificationResponseDto.getMessage())
        );

    }

    @Test
    @DisplayName("인증 방식 삭제시 id에 해당 하는 회원이 없을 때 예외")
    void deleteMemberCertification_MemberIdNotFoundException() {
        DeleteCertificationRequestDto deleteCertificationRequestDto = new DeleteCertificationRequestDto();
        deleteCertificationRequestDto.setMemberId(1L);
        when(memberRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(MemberIdNotFoundException.class,
                () -> memberCertificationService.deleteCertification(deleteCertificationRequestDto)
        );
    }

    @Test
    @DisplayName("인증 방식 삭제시 회원의 인증 정보가 없을 때 예외")
    void deleteMemberCertification_CertificationNotFoundException() {
        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com", LocalDate.now(),"encodedPassword");

        DeleteCertificationRequestDto deleteCertificationRequestDto = new DeleteCertificationRequestDto();
        deleteCertificationRequestDto.setMemberId(member.getMemberId());

        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(memberCertificationRepository.findByMember_MemberId(member.getMemberId())).thenReturn(Optional.empty());

        assertThrows(CertificationNotFoundException.class,
                () -> memberCertificationService.deleteCertification(deleteCertificationRequestDto)
        );

    }

    @Test
    @DisplayName("전체 인증 정보를 성공적으로 조회")
    void getAllMemberCertification_success() {
        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com", LocalDate.now(),"encodedPassword");

        MemberCertification memberCertification = new MemberCertification();
        memberCertification.setMemberAuthId(1L);
        memberCertification.setMember(member);
        memberCertification.setCertificationMethod("일반");
        memberCertification.setLastLogin(LocalDateTime.now());

        Member member2 = new Member(2L, memberGrade, memberStatus, "윤지호2", "010-7237-3951", "yoonwlgh123@naver.com", LocalDate.now(),"encodedPassword");

        MemberCertification memberCertification2 = new MemberCertification();
        memberCertification2.setMemberAuthId(2L);
        memberCertification2.setMember(member2);
        memberCertification2.setCertificationMethod("페이코");
        memberCertification2.setLastLogin(LocalDateTime.now());

        List<MemberCertification> memberCertificationList = List.of(memberCertification, memberCertification2);

        when(memberCertificationRepository.findAll()).thenReturn(memberCertificationList);

        List<CertificationResponseDto> certificationResponseDtos = memberCertificationService.getAllCertifications();

        CertificationResponseDto firstResponse = certificationResponseDtos.get(0);
        CertificationResponseDto secondResponse = certificationResponseDtos.get(1);
        assertAll(
                () -> assertNotNull(memberCertificationList),
                () -> assertEquals(2, memberCertificationList.size()),
                () -> assertEquals(1L, firstResponse.getMemberId()),
                () -> assertEquals("윤지호", firstResponse.getMemberName()),
                () -> assertEquals("일반", firstResponse.getCertification()),
                () -> assertEquals(2L, secondResponse.getMemberId()),
                () -> assertEquals("윤지호2", secondResponse.getMemberName()),
                () -> assertEquals("페이코", secondResponse.getCertification())
        );

    }

    @Test
    @DisplayName("전체 인증 정보 조회시 인증 정보가 없을 때 예외")
    void getALlMemberCertification_CertificationNotFoundException() {
        when(memberCertificationRepository.findAll()).thenReturn(List.of());
        assertThrows(CertificationNotFoundException.class,
                () -> memberCertificationService.getAllCertifications()
        );
        verify(memberCertificationRepository, times(1)).findAll();
    }
}

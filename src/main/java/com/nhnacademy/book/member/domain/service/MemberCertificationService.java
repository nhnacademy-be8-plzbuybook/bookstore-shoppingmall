package com.nhnacademy.book.member.domain.service;

import com.nhnacademy.book.member.domain.MemberCertification;
import com.nhnacademy.book.member.domain.dto.certification.*;
import com.nhnacademy.book.member.domain.dto.certification.CertificationCreateRequestDto;
import com.nhnacademy.book.member.domain.dto.certification.CertificationCreateResponseDto;

import java.util.List;

public interface MemberCertificationService {
    // 인증 추가
    CertificationCreateResponseDto addCertification(CertificationCreateRequestDto certificationCreateRequestDto);

    // 인증 정보 조회 (회원 ID로)
    CertificationResponseDto getCertificationByMemberId(Long memberId);

    // 인증 방법 변경
    CertificationUpdateResponseDto updateCertificationMethod(Long memberId, CertificationUpdateRequestDto certificationUpdateRequestDto);

    // 마지막 로그인 시간 갱신
    UpdateLastLoginResponseDto updateLastLogin(UpdateLastLoginRequestDto updateLastLoginRequestDto);

    // 인증 정보 삭제
    DeleteCertificationResponseDto deleteCertification(DeleteCertificationRequestDto deleteCertificationRequestDto);

    // 모든 인증 정보 조회
    List<CertificationResponseDto> getAllCertifications();

    LastLoginResponseDto updateLastLoginByEmail(LastLoginRequestDto lastLoginRequestDto);

}

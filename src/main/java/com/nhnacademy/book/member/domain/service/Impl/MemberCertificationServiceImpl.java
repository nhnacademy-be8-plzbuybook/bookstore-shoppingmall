package com.nhnacademy.book.member.domain.service.Impl;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberCertification;
import com.nhnacademy.book.member.domain.dto.certification.*;
import com.nhnacademy.book.member.domain.dto.certification.CertificationCreateRequestDto;
import com.nhnacademy.book.member.domain.dto.certification.CertificationCreateResponseDto;
import com.nhnacademy.book.member.domain.exception.*;
import com.nhnacademy.book.member.domain.repository.MemberCertificationRepository;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.member.domain.service.MemberCertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberCertificationServiceImpl implements MemberCertificationService {

    private final MemberCertificationRepository memberCertificationRepository;
    private final MemberRepository memberRepository;

    private static final List<String> VALID_CERTIFICATION_METHODS = List.of("일반", "페이코");

    @Override
    public CertificationCreateResponseDto addCertification(CertificationCreateRequestDto certificationCreateRequest) {
        Member member = memberRepository.findById(certificationCreateRequest.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException("ID에 해당하는 회원을 찾을 수 없다!"));

        if(memberCertificationRepository.existsByCertificationMethod(certificationCreateRequest.getCertification())){
            throw new DuplicateCertificationException("이미 존재하는 인증 방법!");
        }

        MemberCertification memberCertification = new MemberCertification();
        memberCertification.setMember(member);
        memberCertification.setLastLogin(LocalDateTime.now());
        memberCertification.setCertificationMethod(certificationCreateRequest.getCertification());


        memberCertificationRepository.save(memberCertification);

        CertificationCreateResponseDto certificationCreateResponse = new CertificationCreateResponseDto();
        certificationCreateResponse.setMemberAuthId(memberCertification.getMemberAuthId());
        certificationCreateResponse.setMemberId(memberCertification.getMember().getMemberId());
        certificationCreateResponse.setCertification(certificationCreateRequest.getCertification());

        return certificationCreateResponse;
    }

    @Override
    public CertificationResponseDto getCertificationByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberIdNotFoundException("ID에 해당하는 member가 없다!"));

        MemberCertification memberCertification = memberCertificationRepository.findByMember_MemberId(member.getMemberId())
                .orElseThrow(() -> new CertificationNotFoundException("해당 회원의 인증 정보가 없다!"));

        CertificationResponseDto certificationResponse = new CertificationResponseDto();
        certificationResponse.setMemberId(memberCertification.getMember().getMemberId());
        certificationResponse.setCertification(memberCertification.getCertificationMethod());
        return certificationResponse;
    }

    @Override
    public CertificationUpdateResponseDto updateCertificationMethod(Long memberId, CertificationUpdateRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberIdNotFoundException("ID에 해당하는 member가 없다!"));

        MemberCertification memberCertification = memberCertificationRepository.findByMember_MemberId(member.getMemberId())
                .orElseThrow(() -> new CertificationNotFoundException("해당 회원의 인증 정보가 없다!"));

        String newCertificationMethod = requestDto.getCertification();
        if(!isValidCertificationMethod(newCertificationMethod)) {
            throw new InvalidCertificationMethodException("유효하지 않은 인증 방법!");
        }

        memberCertification.setCertificationMethod(newCertificationMethod);
        memberCertificationRepository.save(memberCertification);

        CertificationUpdateResponseDto certificationUpdateResponse = new CertificationUpdateResponseDto();
        certificationUpdateResponse.setMemberId(memberCertification.getMember().getMemberId());
        certificationUpdateResponse.setCertification(newCertificationMethod);

        return certificationUpdateResponse;
    }

    private boolean isValidCertificationMethod(String certificationMethod) {
        return VALID_CERTIFICATION_METHODS.contains(certificationMethod);
    }

    @Override
    public UpdateLastLoginResponseDto updateLastLogin(UpdateLastLoginRequestDto requestDto)
    {
        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new MemberIdNotFoundException("ID에 해당하는 member가 없다!"));

        MemberCertification memberCertification = memberCertificationRepository.findByMember_MemberId(member.getMemberId())
                .orElseThrow(() -> new CertificationNotFoundException("해당 회원의 인증 정보가 없다!"));

        memberCertification.setMember(member);
        memberCertification.setLastLogin(LocalDateTime.now());
        memberCertificationRepository.save(memberCertification);

        UpdateLastLoginResponseDto updateLastLoginResponse = new UpdateLastLoginResponseDto();
        updateLastLoginResponse.setMemberId(memberCertification.getMember().getMemberId());
        updateLastLoginResponse.setLastLogin(memberCertification.getLastLogin());

        return updateLastLoginResponse;

    }

    @Override
    public DeleteCertificationResponseDto deleteCertification(DeleteCertificationRequestDto requestDto) {
        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new MemberIdNotFoundException("ID에 해당하는 member가 없다!"));

        MemberCertification memberCertification = memberCertificationRepository.findByMember_MemberId(member.getMemberId())
                .orElseThrow(() -> new CertificationNotFoundException("해당 회원의 인증 정보가 없다!"));

        memberCertificationRepository.delete(memberCertification);

        DeleteCertificationResponseDto deleteCertificationResponseDto = new DeleteCertificationResponseDto();
        deleteCertificationResponseDto.setSuccess(true);
        deleteCertificationResponseDto.setMessage("인증 방식이 삭제됨!");

        return deleteCertificationResponseDto;
    }

    @Override
    public List<CertificationResponseDto> getAllCertifications() {
        List<MemberCertification> memberCertifications = memberCertificationRepository.findAll();
        if(memberCertifications.isEmpty()) {
            throw new CertificationNotFoundException("인증 정보가 없다!");
        }
        return memberCertifications.stream()
                .map(memberCertification -> new CertificationResponseDto(
                        memberCertification.getMember().getMemberId(),
                        memberCertification.getMember().getName(),
                        memberCertification.getCertificationMethod()))
                .toList();
    }
}
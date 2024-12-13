package com.nhnacademy.book.member.domain.service.Impl;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.dto.*;
import com.nhnacademy.book.member.domain.repository.MemberGradeRepository;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.member.domain.repository.MemberStatusRepository;
import com.nhnacademy.book.member.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberGradeRepository memberGradeRepository;
    private final MemberStatusRepository memberStatusRepository;
    private final PasswordEncoder passwordEncoder;

    //회원 생성
    @Override
    public MemberCreateResponseDto createMember(MemberCreateRequestDto memberCreateRequestDto) {
        // 이메일 중복 검사
        if (memberRepository.existsByEmail(memberCreateRequestDto.getEmail())) {
            throw new RuntimeException("이메일이 이미 존재함!");
        }

        // 회원 등급 및 상태 조회
        MemberGrade memberGrade = memberGradeRepository.findById(memberCreateRequestDto.getMemberGradeId())
                .orElseThrow(() -> new RuntimeException("멤버 등급이 없다!"));
        MemberStatus memberStatus = memberStatusRepository.findById(memberCreateRequestDto.getMemberStateId())
                .orElseThrow(() -> new RuntimeException("멤버 상태가 없다!"));

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(memberCreateRequestDto.getPassword());

        // 회원 객체 생성
        Member member = Member.builder()
                .memberGrade(memberGrade)
                .memberStatus(memberStatus)
                .name(memberCreateRequestDto.getName())
                .phone(memberCreateRequestDto.getPhone())
                .email(memberCreateRequestDto.getEmail())
                .birth(memberCreateRequestDto.getBirth())
                .password(encodedPassword)
                .build();

        Member savedMember = memberRepository.save(member);

        // 응답 DTO 생성 및 반환
        return new MemberCreateResponseDto(
                savedMember.getName(),
                savedMember.getPhone(),
                savedMember.getEmail(),
                savedMember.getBirth(),
                memberGrade.getMemberGradeName(),
                memberStatus.getMemberStateName()
        );
    }

    //회원 수정
    @Override
    public MemberModifyResponseDto modify(Long memberId, MemberModifyRequestDto memberModifyRequestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("id에 해당하는 member가 없다!"));

        // 이메일 수정 시 이메일 중복 검사
        if (memberModifyRequestDto.getEmail() != null &&
                !memberModifyRequestDto.getEmail().equals(member.getEmail()) &&
                memberRepository.existsByEmail(memberModifyRequestDto.getEmail())) {
            throw new RuntimeException("이메일이 이미 존재합니다.");
        }

        // 수정 가능한 필드들만 업데이트
        if (memberModifyRequestDto.getName() != null) {
            member.setName(memberModifyRequestDto.getName());
        }

        if (memberModifyRequestDto.getPhone() != null) {
            member.setPhone(memberModifyRequestDto.getPhone());
        }

        if (memberModifyRequestDto.getEmail() != null) {
            member.setEmail(memberModifyRequestDto.getEmail());
        }

        if (memberModifyRequestDto.getBirth() != null) {
            member.setBirth(memberModifyRequestDto.getBirth());
        }

        if (memberModifyRequestDto.getPassword() != null) {
            member.setPassword(passwordEncoder.encode(memberModifyRequestDto.getPassword()));
        }

        // 수정된 회원 저장
        Member updatedMember = memberRepository.save(member);

        // 응답 DTO 생성
        return new MemberModifyResponseDto(
                updatedMember.getName(),
                updatedMember.getPhone(),
                updatedMember.getEmail(),
                updatedMember.getBirth()
        );
    }
    //회원 등급 저장
    @Override
    public MemberGrade save(MemberGradeCreateRequestDto memberGradeCreateRequestDto) {
        MemberGrade memberGrade = new MemberGrade();
        memberGrade.setMemberGradeName(memberGradeCreateRequestDto.getMemberGradeName());
        memberGrade.setConditionPrice(memberGradeCreateRequestDto.getConditionPrice());
        memberGrade.setGradeChange(memberGradeCreateRequestDto.getGradeChange());

        return memberGradeRepository.save(memberGrade);
    }

    //회원 상태 저장
    @Override
    public MemberStatus save(MemberStatusCreateRequestDto memberStatusCreateRequestDto){
        MemberStatus memberStatus = new MemberStatus();
        memberStatus.setMemberStateName(memberStatusCreateRequestDto.getMemberStateName());

        return memberStatusRepository.save(memberStatus);
    }

    @Override
    public MemberGrade findByMemberGradeId(Long id) {
        MemberGrade memberGrade = memberGradeRepository.findById(id).orElseThrow(() -> new RuntimeException("멤버 등급이 없다!"));
        return memberGrade;
    }



    @Override
    public MemberStatus findByMemberStatusId(Long id){
        MemberStatus memberStatus = memberStatusRepository.findById(id).orElseThrow(() -> new RuntimeException("멤버 상태가 없다!"));
        return memberStatus;
    }

    //이메일로 특정 회원 조회
    @Override
    public MemberEmailResponseDto getMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() ->new RuntimeException("이메일에 해당하는 멤버가 없다!"));

        MemberEmailResponseDto memberEmailResponseDto = new MemberEmailResponseDto();
        memberEmailResponseDto.setName(member.getName());
        memberEmailResponseDto.setPhone(member.getPhone());
        memberEmailResponseDto.setEmail(member.getEmail());
        memberEmailResponseDto.setBirth(member.getBirth());
        memberEmailResponseDto.setMemberGradeName(member.getMemberGrade().getMemberGradeName());
        memberEmailResponseDto.setMemberStateName(member.getMemberStatus().getMemberStateName());

        return memberEmailResponseDto;


    }

    //id로 특정 회원 조회
    @Override
    public MemberIdResponseDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Id에 해당하는 멤버가 없다!"));

        MemberGrade memberGrade = memberGradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("멤버 등급이 없다!"));

        MemberStatus memberStatus = memberStatusRepository
                .findById(id).orElseThrow(() -> new RuntimeException("멤버 상태가 없다!"));

        return new MemberIdResponseDto(
                member.getName(),
                member.getPhone(),
                member.getEmail(),
                member.getBirth(),
                member.getMemberGrade().getMemberGradeName(),
                member.getMemberStatus().getMemberStateName()
        );
    }

    @Override
    public void withdrawMember(Long memberId) {
        MemberStatus withdrawStatus = memberStatusRepository.findByMemberStateName("WITHDRAW")
                .orElseThrow(() -> new RuntimeException("withdraw 상태가 없다!"));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원이 존재 하지 않음!"));

        member.setMemberStatus(withdrawStatus);
        memberRepository.save(member);
    }
}

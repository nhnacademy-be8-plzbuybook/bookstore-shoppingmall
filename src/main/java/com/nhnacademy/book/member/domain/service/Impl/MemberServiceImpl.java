package com.nhnacademy.book.member.domain.service.Impl;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberAuth;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.dto.*;
import com.nhnacademy.book.member.domain.exception.*;
import com.nhnacademy.book.member.domain.repository.MemberGradeRepository;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.member.domain.repository.MemberStatusRepository;
import com.nhnacademy.book.member.domain.repository.auth.AuthRepository;
import com.nhnacademy.book.member.domain.repository.auth.MemberAuthRepository;
import com.nhnacademy.book.member.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberGradeRepository memberGradeRepository;
    private final MemberStatusRepository memberStatusRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberAuthRepository memberAuthRepository;


    //회원 생성
    @Override
    public MemberCreateResponseDto createMember(MemberCreateRequestDto memberCreateRequestDto) {
        // 이메일 중복 검사
        if (memberRepository.existsByEmail(memberCreateRequestDto.getEmail())) {
            throw new DuplicateEmailException("이메일이 이미 존재함!");
        }

        // 기본 등급 조회 (예: ID가 1인 기본 등급)
        MemberGrade memberGrade = memberGradeRepository.findById(1L)
                .orElseThrow(() -> new DefaultMemberGradeNotFoundException("기본 회원 등급을 찾을 수 없습니다!"));

        // 기본 상태 조회 (예: ID가 1인 기본 상태)
        MemberStatus memberStatus = memberStatusRepository.findById(1L)
                .orElseThrow(() -> new DefaultStatusGradeNotfoundException("기본 회원 상태를 찾을 수 없습니다!"));

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

    //회원 수정 (수정하려는 값이 하나도 없는데 수정하는 경우 예외 발생)
    @Override
    public MemberModifyResponseDto modify(Long memberId, MemberModifyRequestDto memberModifyRequestDto) {
        boolean isModified = false;

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberIdNotFoundException("id에 해당하는 member가 없다!"));

        if (memberModifyRequestDto.getName() != null && !memberModifyRequestDto.getName().equals(member.getName())) {
            member.setName(memberModifyRequestDto.getName());
            isModified = true;
        }

        if (memberModifyRequestDto.getPhone() != null && !memberModifyRequestDto.getPhone().equals(member.getPhone())) {
            member.setPhone(memberModifyRequestDto.getPhone());
            isModified = true;
        }

        if (memberModifyRequestDto.getEmail() != null && !memberModifyRequestDto.getEmail().equals(member.getEmail())) {
            if (memberRepository.existsByEmail(memberModifyRequestDto.getEmail())) {
                throw new DuplicateEmailException("이메일이 이미 존재!");
            }
            member.setEmail(memberModifyRequestDto.getEmail());
            isModified = true;
        }

        if (memberModifyRequestDto.getBirth() != null && !memberModifyRequestDto.getBirth().equals(member.getBirth())) {
            member.setBirth(memberModifyRequestDto.getBirth());
            isModified = true;
        }

        if (memberModifyRequestDto.getPassword() != null && !passwordEncoder.matches(memberModifyRequestDto.getPassword(), member.getPassword())) {
            member.setPassword(passwordEncoder.encode(memberModifyRequestDto.getPassword()));
            isModified = true;
        }

        if(!isModified) {
            throw new DuplicateMemberModificationException("수정할 내용이 기존 데이터와 같다!");
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

    //이메일로 특정 회원 조회
    @Override
    public MemberEmailResponseDto getMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() ->new MemberEmailNotFoundException("이메일에 해당하는 멤버가 없다!"));

        List<MemberAuth> memberAuthList = memberAuthRepository.findByMember(member);

        if (memberAuthList.isEmpty()) {
            throw new RuntimeException("해당 멤버에 대한 권한 정보를 찾을 수 없습니다");
        }

        String authName = memberAuthList.get(0).getAuth().getAuthName();

        MemberEmailResponseDto memberEmailResponseDto = new MemberEmailResponseDto();
        memberEmailResponseDto.setEmail(member.getEmail());
        memberEmailResponseDto.setPassword(member.getPassword());
        memberEmailResponseDto.setAuthName(authName);// 권한 정보 추가

        return memberEmailResponseDto;


    }

    //id로 특정 회원 조회
    @Override
    public MemberIdResponseDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberIdNotFoundException("Id에 해당하는 멤버가 없다!"));

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
        MemberStatus withdrawStatus = memberStatusRepository.findByMemberStateName("WITHDRAWAL")
                .orElseThrow(() -> new MemberGradeNotFoundException("withdraw 상태가 없다!"));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberIdNotFoundException("id에 해당하는 member가 없다!"));

        member.setMemberStatus(withdrawStatus);
        memberRepository.save(member);
    }

    @Override
    public Page<MemberSearchResponseDto> getMembers(MemberSearchRequestDto memberSearchRequestDto) {
        Pageable pageable = PageRequest.of(memberSearchRequestDto.getPage(), memberSearchRequestDto.getSize());

        Page<Member> members = memberRepository.findAll(pageable);

        if(members.isEmpty()) {
            throw new MemberNotFoundException("등록된 회원이 없다!");
        }
        return memberRepository.findAll(pageable)
                .map(member -> new MemberSearchResponseDto(
                        member.getName(),
                        member.getPhone(),
                        member.getEmail(),
                        member.getBirth(),
                        member.getMemberGrade().getMemberGradeName(),
                        member.getMemberStatus().getMemberStateName()
                ));
    }
}

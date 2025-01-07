package com.nhnacademy.book.member.domain.service.Impl;

import com.nhnacademy.book.feign.CouponClient;
import com.nhnacademy.book.feign.dto.WelComeCouponRequestDto;
import com.nhnacademy.book.member.domain.*;
import com.nhnacademy.book.member.domain.dto.*;
import com.nhnacademy.book.member.domain.exception.*;
import com.nhnacademy.book.member.domain.repository.MemberCertificationRepository;
import com.nhnacademy.book.member.domain.repository.MemberGradeRepository;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.member.domain.repository.MemberStatusRepository;
import com.nhnacademy.book.member.domain.repository.auth.AuthRepository;
import com.nhnacademy.book.member.domain.repository.auth.MemberAuthRepository;
import com.nhnacademy.book.member.domain.service.MemberService;
import com.nhnacademy.book.point.repository.MemberPointRepository;
import com.nhnacademy.book.point.service.MemberPointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Lock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberGradeRepository memberGradeRepository;
    private final MemberStatusRepository memberStatusRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberAuthRepository memberAuthRepository;
    private final MemberPointService memberPointService;

    private final CouponClient couponClient;
    private final AuthRepository authRepository;
    private final MemberCertificationRepository memberCertificationRepository;
    private final Clock clock;

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

        Auth defaultAuth = authRepository.findById(2L)
                .orElseThrow(() -> new DefaultAuthNotfoundException("기본 권한을 찾을 수 없습니다!"));

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

        MemberCertification memberCertification = new MemberCertification();
        memberCertification.setMember(savedMember);
        memberCertification.setLastLogin(null);
        memberCertification.setCertificationMethod("일반");
        memberCertificationRepository.save(memberCertification);

        MemberAuth memberAuth = new MemberAuth();
        memberAuth.setMember(savedMember);
        memberAuth.setAuth(defaultAuth);
        memberAuthRepository.save(memberAuth);

        // 응답 DTO 생성 및 반환
        MemberCreateResponseDto memberCreateResponseDto = new MemberCreateResponseDto(
                savedMember.getName(),
                savedMember.getPhone(),
                savedMember.getEmail(),
                savedMember.getBirth(),
                memberGrade.getMemberGradeName(),
                memberStatus.getMemberStateName()
        );

        // Welcome 쿠폰발급 요청
        issuedWelcomeCoupon(savedMember);

        // 회원가입시 포인트
        issueSignUpPoint(savedMember);

        return memberCreateResponseDto;
    }

    // 회원 등록 -> Welcome 쿠폰발급 요청 메서드 분리
    private void issuedWelcomeCoupon(Member savedMember) {
        try {
            // Welcome 쿠폰(회원가입 쿠폰) 발급 요청
            WelComeCouponRequestDto welComeCouponRequestDto = new WelComeCouponRequestDto(
                    savedMember.getMemberId(),
                    LocalDateTime.now()
            );
            couponClient.issueWelcomeCoupon(welComeCouponRequestDto);
        } catch (Exception e) {
            // 에러를 던지면 로직이 멈추기 떄문에 에러 로그 출력만 하도록 변경
            log.error("Welcome 쿠폰발급이 실패 하였습니다!");
        }
    }

    // 회원등록 -> 회원가입시 포인트
    private void issueSignUpPoint(Member savedMember) {
        try {
            memberPointService.addSignUpPoint(savedMember);
        } catch (Exception e) {
            log.error("회원 가입 포인트 적립이 실패하였습니다! {}", e.getMessage());
        }
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

    //header 이메일을 통해 회원 정보 수정
    @Override
    @Transactional
    public void updateMember(String email, MemberModifyRequestDto memberModifyRequestDto) {
        boolean isModified = false;

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberEmailNotFoundException("이메일에 해당하는 회원이 없다!"));

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

//        memberRepository.save(member);

    }

    //이메일로 특정 회원 조회
    @Override
    public MemberEmailResponseDto getMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberEmailNotFoundException("이메일에 해당하는 멤버가 없다!"));

        List<MemberAuth> memberAuthList = memberAuthRepository.findByMember(member);

        if (memberAuthList.isEmpty()) {
            throw new RuntimeException("해당 멤버에 대한 권한 정보를 찾을 수 없습니다");
        }

        String authName = memberAuthList.get(0).getAuth().getAuthName();

        MemberEmailResponseDto memberEmailResponseDto = new MemberEmailResponseDto();
        memberEmailResponseDto.setEmail(member.getEmail());
        memberEmailResponseDto.setPassword(member.getPassword());
        memberEmailResponseDto.setAuthName(authName);
        memberEmailResponseDto.setMemberStateName(member.getMemberStatus().getMemberStateName());

        return memberEmailResponseDto;

    }

    //이메일로 특정 회원 조회(myPage에서 사용)
    @Override
    public MemberDto getMemberMyByEmail(String email) {
        Member member = memberRepository.findByEmailWithGradeAndStatus(email)
                .orElseThrow(() -> new MemberEmailNotFoundException("해당 이메일의 회원이 존재하지 않다!"));

        if ("WITHDRAWAL".equalsIgnoreCase(member.getMemberStatus().getMemberStateName())) {
            throw new IllegalStateException("탈퇴한 회원입니다.");
        }

        return new MemberDto(
                member.getMemberId(),
                member.getName(),
                member.getPhone(),
                member.getPassword(),
                member.getEmail(),
                member.getBirth(),
                member.getMemberGrade().getMemberGradeName(),
                member.getMemberStatus().getMemberStateName()
        );

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

    // 회원 탈퇴
    @Override
    public void withdrawState(String email) {
        MemberStatus withdrawStatus = memberStatusRepository.findByMemberStateName("WITHDRAWAL")
                .orElseThrow(() -> new MemberGradeNotFoundException("withdraw 상태가 없다!"));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberEmailNotFoundException("해당 이메일의 회원이 존재하지 않다!"));

        if (member.getMemberStatus().getMemberStateName().equals("WITHDRAWAL")) {
            throw new IllegalStateException("이미 탈퇴한 회원입니다.");
        }

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

    // 회원 상태 변경 (dormant -> active)
    @Override
    public void updateActiveStatus(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("이메일에 해당하는 회원이 없습니다."));

        MemberStatus activeStatus = memberStatusRepository.findByMemberStateName("ACTIVE")
                .orElseThrow(() -> new MemberStatusNotFoundException("해당 상태가 존재하지 않습니다."));

        if (member.getMemberStatus().equals(activeStatus)) {
            // 이미 ACTIVE 상태면 아무 작업도 하지 않고 종료
            return;
        }

        member.setMemberStatus(activeStatus);
        memberRepository.save(member);

    }

    // 3개월 이상 미로그인시 (active -> dormant) 로 변경
    public void updateDormantStatus() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now(clock).minusMonths(3);

        List<MemberCertification> inactiveMembers = memberCertificationRepository.findInactiveMember(threeMonthsAgo);

        if (inactiveMembers.isEmpty()) {
            // 3개월 이상 미로그인 회원이 없으면 작업 종료
            return;
        }
        MemberStatus dormantStatus = memberStatusRepository.findByMemberStateName("DORMANT")
                .orElseThrow(() -> new MemberStatusNotFoundException("해당 상태가 존재하지 않습니다."));

        for (MemberCertification certification : inactiveMembers) {
            Member member = certification.getMember();
            member.setMemberStatus(dormantStatus); // 상태 변경
        }

        memberRepository.saveAll(
                inactiveMembers.stream()
                        .map(MemberCertification::getMember)
                        .toList()
        );
    }

    //header 이메일을 통해 회원 정보 수정
    //관리자가 회원의 정보를 수정
    @Override
    public void updateMemberByAdmin(String email, MemberModifyByAdminRequestDto memberModifyByAdminRequestDto) {
        boolean isModified = false;

        // 기존 회원 정보 조회
        Member member = memberRepository.findByEmail(memberModifyByAdminRequestDto.getOriginalEmail())
                .orElseThrow(() -> new MemberEmailNotFoundException("이메일에 해당하는 회원이 없습니다."));

        // 이름 수정
        if (memberModifyByAdminRequestDto.getName() != null &&
                !memberModifyByAdminRequestDto.getName().equals(member.getName())) {
            member.setName(memberModifyByAdminRequestDto.getName());
            isModified = true;
        }

        // 전화번호 수정
        if (memberModifyByAdminRequestDto.getPhone() != null &&
                !memberModifyByAdminRequestDto.getPhone().equals(member.getPhone())) {
            boolean phoneExists = memberRepository.findByPhone(memberModifyByAdminRequestDto.getPhone())
                    .map(existingMember -> !existingMember.getMemberId().equals(member.getMemberId())) // 본인의 전화번호는 제외
                    .orElse(false);

            if (phoneExists) {
                throw new DuplicatePhoneException("이미 사용 중인 전화번호입니다.");
            }
            member.setPhone(memberModifyByAdminRequestDto.getPhone());
            isModified = true;
        }

        // 이메일 수정
        if (memberModifyByAdminRequestDto.getEmail() != null &&
                !memberModifyByAdminRequestDto.getEmail().equals(member.getEmail())) {
            boolean emailExists = memberRepository.findByEmail(memberModifyByAdminRequestDto.getEmail())
                    .map(existingMember -> !existingMember.getMemberId().equals(member.getMemberId())) // 본인의 이메일은 제외
                    .orElse(false);

            if (emailExists) {
                throw new DuplicateEmailException("이미 사용 중인 이메일입니다.");
            }
            member.setEmail(memberModifyByAdminRequestDto.getEmail());
            isModified = true;
        }

        // 생년월일 수정
        if (memberModifyByAdminRequestDto.getBirth() != null &&
                !memberModifyByAdminRequestDto.getBirth().equals(member.getBirth())) {
            member.setBirth(memberModifyByAdminRequestDto.getBirth());
            isModified = true;
        }

        // 회원 등급 수정
        if (memberModifyByAdminRequestDto.getMemberGradeId() != null &&
                !memberModifyByAdminRequestDto.getMemberGradeId().equals(member.getMemberGrade().getMemberGradeId())) {
            MemberGrade newGrade = memberGradeRepository.findById(memberModifyByAdminRequestDto.getMemberGradeId())
                    .orElseThrow(() -> new MemberGradeNotFoundException("회원 등급이 존재하지 않습니다."));
            member.setMemberGrade(newGrade);
            isModified = true;
        }

        // 회원 상태 수정
        if (memberModifyByAdminRequestDto.getMemberStateId() != null &&
                !memberModifyByAdminRequestDto.getMemberStateId().equals(member.getMemberStatus().getMemberStateId())) {
            MemberStatus newStatus = memberStatusRepository.findById(memberModifyByAdminRequestDto.getMemberStateId())
                    .orElseThrow(() -> new MemberStatusNotFoundException("회원 상태가 존재하지 않습니다."));
            member.setMemberStatus(newStatus);
            isModified = true;
        }

        // 변경 사항이 없는 경우 예외 처리
        if (!isModified) {
            throw new DuplicateMemberModificationException("기존 데이터와 동일하여 수정할 내용이 없습니다.");
        }

    }

}

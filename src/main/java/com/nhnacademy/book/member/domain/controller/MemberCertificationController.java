package com.nhnacademy.book.member.domain.controller;

import com.nhnacademy.book.member.domain.dto.certification.*;
import com.nhnacademy.book.member.domain.dto.certification.CertificationCreateRequestDto;
import com.nhnacademy.book.member.domain.dto.certification.CertificationCreateResponseDto;
import com.nhnacademy.book.member.domain.service.MemberCertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberCertificationController {

    private final MemberCertificationService memberCertificationService;

    @PostMapping("/certification")
    public ResponseEntity<CertificationCreateResponseDto> createCertification(@RequestBody CertificationCreateRequestDto certificationCreateRequest) {
        CertificationCreateResponseDto certificationCreateResponse = memberCertificationService.addCertification(certificationCreateRequest);
        return ResponseEntity.ok(certificationCreateResponse);
    }

    @GetMapping("/members/{member_id}/certification")
    public ResponseEntity<CertificationResponseDto> getCertificationByMemberId(@PathVariable Long member_id) {
        CertificationResponseDto certificationResponseDto = memberCertificationService.getCertificationByMemberId(member_id);
        return ResponseEntity.ok(certificationResponseDto);
    }

    @PatchMapping("/members/{member_id}/certification")
    public ResponseEntity<CertificationUpdateResponseDto> updateCertification(@PathVariable Long member_id, @RequestBody CertificationUpdateRequestDto certificationUpdateRequestDto) {
        CertificationUpdateResponseDto certificationUpdateResponseDto = memberCertificationService.updateCertificationMethod(member_id, certificationUpdateRequestDto);
        return ResponseEntity.ok(certificationUpdateResponseDto);
    }

    @PostMapping("/members/{member_id}/last_login")
    public ResponseEntity<UpdateLastLoginResponseDto> updateLastLogin(@PathVariable Long member_id){
        UpdateLastLoginRequestDto updateLastLoginRequestDto = new UpdateLastLoginRequestDto();
        updateLastLoginRequestDto.setMemberId(member_id);
        UpdateLastLoginResponseDto updateLastLoginResponseDto = memberCertificationService.updateLastLogin(updateLastLoginRequestDto);
        return ResponseEntity.ok(updateLastLoginResponseDto);
    }

    @DeleteMapping("/members/{member_id}/certification")
    public ResponseEntity<DeleteCertificationResponseDto> deleteCertification(@PathVariable Long member_id) {
        DeleteCertificationRequestDto deleteCertificationRequestDto = new DeleteCertificationRequestDto();
        deleteCertificationRequestDto.setMemberId(member_id);
        DeleteCertificationResponseDto deleteCertificationResponseDto = memberCertificationService.deleteCertification(deleteCertificationRequestDto);
        return ResponseEntity.ok(deleteCertificationResponseDto);
    }

    @GetMapping("/members/certification")
    public ResponseEntity<List<CertificationResponseDto>> getAllCertification(){
        return ResponseEntity.ok(memberCertificationService.getAllCertifications());
    }

    // 마지막 로그인 일시
    @PostMapping("/members/last-login")
    public ResponseEntity<LastLoginResponseDto> updateLastLoginByEmail(@RequestBody LastLoginRequestDto lastLoginRequestDto) {
        LastLoginResponseDto lastLoginResponseDto = memberCertificationService.updateLastLoginByEmail(lastLoginRequestDto);
        return ResponseEntity.ok(lastLoginResponseDto);
    }

}

package com.senity.waved.domain.admin.controller;

import com.senity.waved.common.ResponseDto;
import com.senity.waved.domain.admin.service.AdminService;
import com.senity.waved.domain.challengeGroup.dto.response.AdminChallengeGroupResponseDto;
import com.senity.waved.domain.verification.dto.response.AdminVerificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/groups")
    public List<AdminChallengeGroupResponseDto> getGroups() {
        return adminService.getGroups();
   }

    @GetMapping("/{challengeGroupId}/verifications")
    public Page<AdminVerificationDto> getGroupVerificationsPaged (
            @PathVariable("challengeGroupId") Long challengeGroupId,
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "limit", defaultValue = "5") int pageSize
    ) {
        return adminService.getGroupVerificationsPaged(challengeGroupId, pageNumber, pageSize);
    }

    @DeleteMapping("/{challengeGroupId}/verifications/{verificationId}")
    public ResponseEntity<ResponseDto> deleteVerification(
            @PathVariable("challengeGroupId") Long challengeGroupId,
            @PathVariable("verificationId") Long verificationId
    ) {
        adminService.deleteVerification(challengeGroupId, verificationId);
        return ResponseDto.of(HttpStatus.OK, "인증 취소 처리가 되었습니다.");
    }
}

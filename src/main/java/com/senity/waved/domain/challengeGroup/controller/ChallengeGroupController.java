package com.senity.waved.domain.challengeGroup.controller;

import com.senity.waved.domain.challengeGroup.dto.response.ChallengeGroupResponseDto;
import com.senity.waved.domain.challengeGroup.dto.response.VerificationListResponseDto;
import com.senity.waved.domain.challengeGroup.service.ChallengeGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challengeGroups")
public class ChallengeGroupController {

    public final ChallengeGroupService challengeGroupService;

    @GetMapping("/info/{challengeGroupId}")
    public ChallengeGroupResponseDto getChallengeGroup(
            @AuthenticationPrincipal User user,
            @PathVariable("challengeGroupId") Long groupId
    ) {
        String email = Objects.isNull(user) ? null : user.getUsername();
        return challengeGroupService.getGroupDetail(email, groupId);
    }

    @PostMapping("/{challengeGroupId}")
    public Long applyChallengeGroup(
            @PathVariable("challengeGroupId") Long groupId,
            @AuthenticationPrincipal User user,
            @RequestParam("deposit") Long deposit
    ) {
        return challengeGroupService.applyForChallengeGroup(user.getUsername(), groupId, deposit);
    }

    @GetMapping("/{challengeGroupId}")
    public ResponseEntity<List<VerificationListResponseDto>> getVerificationsByDate (
            @PathVariable("challengeGroupId") Long challengeGroupId,
            @RequestParam("verificationDate") Timestamp verificationDate,
            @AuthenticationPrincipal User user
    ) {
        List<VerificationListResponseDto> verifications =
                challengeGroupService.getVerifications(user.getUsername(), challengeGroupId, verificationDate);
        return ResponseEntity.ok(verifications);
    }

    @GetMapping("/{challengeGroupId}/myVerifies")
    public ResponseEntity<List<VerificationListResponseDto>> getVerificationsByDateAndMember(
            @PathVariable("challengeGroupId") Long challengeGroupId,
            @RequestParam("verificationDate") Timestamp verificationDate,
            @AuthenticationPrincipal User user) {
        List<VerificationListResponseDto> verifications =
                challengeGroupService.getUserVerifications(user.getUsername(), challengeGroupId, verificationDate);
        return ResponseEntity.ok(verifications);
    }

}
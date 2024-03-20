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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challengeGroups/{challengeGroupId}")
public class ChallengeGroupController {

    public final ChallengeGroupService challengeGroupService;

    @GetMapping
    public ChallengeGroupResponseDto getChallengeGroup(
            @AuthenticationPrincipal User user,
            @PathVariable("challengeGroupId") Long groupId
    ) {
        return challengeGroupService.getGroupDetail(user.getUsername(), groupId);
    }

    @PostMapping("/apply")
    public Long applyChallengeGroup(
            @AuthenticationPrincipal User user,
            @PathVariable("challengeGroupId") Long groupId,
            @RequestParam("deposit") Long deposit

    ) {
        return challengeGroupService.applyForChallengeGroup(user.getUsername(), groupId, deposit);
    }

    @GetMapping("/dates")
    public ResponseEntity<List<VerificationListResponseDto>> getVerificationsByDate (
            @PathVariable("challengeGroupId") Long challengeGroupId,
            @RequestParam("verificationDate") Timestamp verificationDate,
            @AuthenticationPrincipal User user) {
        List<VerificationListResponseDto> verifications =
                challengeGroupService.getVerifications(user.getUsername(), challengeGroupId, verificationDate);
        return ResponseEntity.ok(verifications);
    }
}

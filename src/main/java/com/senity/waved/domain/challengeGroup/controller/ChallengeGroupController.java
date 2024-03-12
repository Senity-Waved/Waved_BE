package com.senity.waved.domain.challengeGroup.controller;


import com.senity.waved.domain.challengeGroup.dto.response.ChallengeGroupResponseDto;
import com.senity.waved.domain.challengeGroup.service.ChallengeGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import com.senity.waved.domain.challengeGroup.dto.response.VerificationListResponseDto;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    public ResponseEntity<String> applyChallengeGroup(
            @PathVariable("challengeGroupId") Long groupId,
            @AuthenticationPrincipal User user
    ) {
        challengeGroupService.applyForChallengeGroup(user.getUsername(), groupId);
        return new ResponseEntity<>("챌린지 그룹 참여 신청을 완료했습니다.", HttpStatus.OK);
    }

    @GetMapping("/dates")
    public ResponseEntity<List<VerificationListResponseDto>> getVerificationsByDate(
            @PathVariable("challengeGroupId") Long challengeGroupId,
            @RequestParam("verificationDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime verificationDate) {
        Timestamp timestamp = Timestamp.valueOf(verificationDate);
        List<VerificationListResponseDto> verifications = challengeGroupService.getVerifications(challengeGroupId, timestamp);
        return ResponseEntity.ok(verifications);
    }
}


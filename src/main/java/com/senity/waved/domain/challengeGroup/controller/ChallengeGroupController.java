package com.senity.waved.domain.challengeGroup.controller;

import com.senity.waved.domain.challengeGroup.dto.response.VerificationListResponseDto;
import com.senity.waved.domain.challengeGroup.service.ChallengeGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challengeGroups/{challengeGroupId}")
public class ChallengeGroupController {

    private final ChallengeGroupService challengeGroupService;

    @GetMapping
    public ResponseEntity<List<VerificationListResponseDto>> getVerificationsByDate(
            @PathVariable("challengeGroupId") Long challengeGroupId,
            @RequestParam("verificationDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime verificationDate) {
        Timestamp timestamp = Timestamp.valueOf(verificationDate);
        List<VerificationListResponseDto> verifications = challengeGroupService.getVerifications(challengeGroupId, timestamp);
        return ResponseEntity.ok(verifications);
    }
}


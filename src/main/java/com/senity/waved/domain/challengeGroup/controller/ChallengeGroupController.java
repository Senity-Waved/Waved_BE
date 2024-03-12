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
}

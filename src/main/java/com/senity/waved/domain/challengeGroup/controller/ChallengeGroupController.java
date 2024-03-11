package com.senity.waved.domain.challengeGroup.controller;

import com.senity.waved.domain.challengeGroup.dto.response.ChallengeGroupResponseDto;
import com.senity.waved.domain.challengeGroup.service.ChallengeGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/groups")
public class ChallengeGroupController {

    public final ChallengeGroupService challengeGroupService;

    @GetMapping("/{groupId}")
    public ChallengeGroupResponseDto getChallengeGroup(@PathVariable("groupId") Long groupId) {
        return challengeGroupService.getGroupDetail(groupId);
    }
}

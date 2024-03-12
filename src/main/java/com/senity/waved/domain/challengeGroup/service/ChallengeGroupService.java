package com.senity.waved.domain.challengeGroup.service;

import com.senity.waved.domain.challengeGroup.dto.response.ChallengeGroupResponseDto;

public interface ChallengeGroupService {

    void applyForChallengeGroup(String email, Long groupId);
    ChallengeGroupResponseDto getGroupDetail(String email, Long groupId);
}

package com.senity.waved.domain.challengeGroup.service;

import com.senity.waved.domain.challengeGroup.dto.response.ChallengeGroupResponseDto;

public interface ChallengeGroupService {

    ChallengeGroupResponseDto getGroupDetail(Long groupId);
}

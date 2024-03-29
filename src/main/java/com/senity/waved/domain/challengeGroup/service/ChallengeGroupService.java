package com.senity.waved.domain.challengeGroup.service;

import com.senity.waved.domain.challengeGroup.dto.response.ChallengeGroupResponseDto;
import com.senity.waved.domain.challengeGroup.dto.response.VerificationListResponseDto;

import java.sql.Timestamp;
import java.util.List;

public interface ChallengeGroupService {

    Long applyForChallengeGroup(String email, Long groupId, Long deposit);
    ChallengeGroupResponseDto getGroupDetail(String email, Long groupId);
    List<VerificationListResponseDto> getVerifications(String email, Long challengeGroupId, Timestamp verificationDate);
    List<VerificationListResponseDto> getUserVerifications(String username, Long challengeGroupId, Timestamp verificationDate);
}
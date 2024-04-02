package com.senity.waved.domain.challengeGroup.service;

import com.senity.waved.domain.challengeGroup.dto.response.ChallengeGroupResponseDto;
import com.senity.waved.domain.verification.dto.response.VerificationResponseDto;

import java.sql.Timestamp;
import java.util.List;

public interface ChallengeGroupService {

    Long applyForChallengeGroup(String email, Long groupId, Long deposit);
    ChallengeGroupResponseDto getGroupDetail(String email, Long groupId);
    List<VerificationResponseDto> getVerifications(String email, Long challengeGroupId, Timestamp verificationDate);
    List<VerificationResponseDto> getUserVerifications(String username, Long challengeGroupId, Timestamp verificationDate);
}
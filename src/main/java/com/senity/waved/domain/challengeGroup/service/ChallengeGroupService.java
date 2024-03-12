package com.senity.waved.domain.challengeGroup.service;

import com.senity.waved.domain.challengeGroup.dto.response.VerificationListResponseDto;

import java.sql.Timestamp;
import java.util.List;

public interface ChallengeGroupService {
      List<VerificationListResponseDto> getVerifications(Long challengeGroupId, Timestamp verificationDate);
}

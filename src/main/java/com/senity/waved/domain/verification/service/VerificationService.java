package com.senity.waved.domain.verification.service;

import com.senity.waved.domain.verification.dto.request.VerificationRequestDto;

public interface VerificationService {
    void verifyChallenge(VerificationRequestDto requestDto, String email, Long challengeGroupId);
    void IsChallengeGroupTextType(Long challengeGroupId);
}

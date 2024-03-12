package com.senity.waved.domain.myChallenge.service;

import com.senity.waved.domain.myChallenge.dto.response.MyChallengeResponseDto;
import com.senity.waved.domain.myChallenge.entity.ChallengeStatus;

import java.util.List;

public interface MyChallengeService {

    void cancelAppliedMyChallenge(Long myChallengeId);

    List<MyChallengeResponseDto> getMyChallengesListed(String email, ChallengeStatus status);
}

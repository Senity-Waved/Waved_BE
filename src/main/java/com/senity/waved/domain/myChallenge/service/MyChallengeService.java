package com.senity.waved.domain.myChallenge.service;

import com.senity.waved.domain.myChallenge.dto.response.MyChallengeResponseDto;

import java.util.List;

public interface MyChallengeService {

    void cancelAppliedMyChallenge(Long myChallengeId);

    List<MyChallengeResponseDto> getMyChallengesInProgressListed(String email);
    List<MyChallengeResponseDto> getMyChallengesWaitingListed(String email);
    List<MyChallengeResponseDto> getMyChallengesCompletedListed(String email);
}

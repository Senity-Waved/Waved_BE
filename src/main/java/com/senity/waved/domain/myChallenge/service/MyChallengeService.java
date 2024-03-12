package com.senity.waved.domain.myChallenge.service;

import com.senity.waved.domain.myChallenge.dto.response.MyChallengeResponseDto;
import com.senity.waved.domain.myChallenge.entity.ChallengeStatus;
import com.senity.waved.domain.myChallenge.entity.MyChallenge;

import java.util.List;

public interface MyChallengeService {
    MyChallenge findMyChallengeById(Long myChallengeId);

    void cancelAppliedMyChallenge(Long myChallengeId);

    List<MyChallengeResponseDto> getMyChallengesListed(String email, ChallengeStatus status);
}

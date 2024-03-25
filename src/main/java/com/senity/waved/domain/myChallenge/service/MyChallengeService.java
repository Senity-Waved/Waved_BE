package com.senity.waved.domain.myChallenge.service;

import com.senity.waved.domain.myChallenge.dto.response.MyChallengeResponseDto;
import com.senity.waved.domain.myChallenge.dto.response.MyVerifsResponseDto;
import com.senity.waved.domain.myChallenge.entity.ChallengeStatus;

import java.util.List;

public interface MyChallengeService {
    MyVerifsResponseDto getMyVerifications(Long myChallengeId);
    void cancelAppliedMyChallenge(String email, Long myChallengeId);
    List<MyChallengeResponseDto> getMyChallengesListed(String email, ChallengeStatus status);
}

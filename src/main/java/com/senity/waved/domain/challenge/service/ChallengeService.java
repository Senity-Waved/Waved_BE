package com.senity.waved.domain.challenge.service;

import com.senity.waved.domain.challengeGroup.dto.response.ChallengeGroupHomeResponseDto;
import com.senity.waved.domain.review.dto.response.ReviewResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ChallengeService {
    List<ChallengeGroupHomeResponseDto> getHomeChallengeGroupsListed();
    Page<ReviewResponseDto> getReviewsPaged(Long challengeId, int pageNumber, int pageSize);
}

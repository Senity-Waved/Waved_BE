package com.senity.waved.domain.challenge.service;

import com.senity.waved.domain.review.dto.response.ReviewResponseDto;
import org.springframework.data.domain.Page;

public interface ChallengeService {
    Page<ReviewResponseDto> getReviewsPaged(Long challengeId, int pageNumber, int pageSize);
}

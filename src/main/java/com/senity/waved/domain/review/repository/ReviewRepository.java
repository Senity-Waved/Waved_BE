package com.senity.waved.domain.review.repository;

import com.senity.waved.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> getReviewByMemberId(Long memberId);
    List<Review> findByChallengeId(Long challengeId);
}

package com.senity.waved.domain.review.repository;

import com.senity.waved.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> getReviewByMemberId(Long memberId, Pageable pageable);
    Page<Review> findByChallengeId(Long challengeId, Pageable pageable);
}

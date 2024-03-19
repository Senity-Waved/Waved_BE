package com.senity.waved.domain.review.service;

public interface ReviewService {
    void createChallengeReview(String email, Long myChallengeId, String content);
    void editChallengeReview(String email, Long reviewId, String content);
    void deleteChallengeReview(String email, Long reviewId);
    String getReviewContentForEdit(String username, Long reviewId);
}

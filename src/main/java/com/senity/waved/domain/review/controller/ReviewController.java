package com.senity.waved.domain.review.controller;

import com.senity.waved.common.ResponseDto;
import com.senity.waved.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/myChallenge/{myChallengeId}")
    public ResponseEntity<ResponseDto> createReview(
            @AuthenticationPrincipal User user,
            @PathVariable("myChallengeId") Long myChallengeId,
            @RequestBody String content
    ) {
        reviewService.createChallengeReview(user.getUsername(), myChallengeId, content);
        return ResponseDto.of(HttpStatus.OK, "리뷰를 저장했습니다.");
    }

    @GetMapping("/{reviewId}")
    public String getReviewForEdit(
            @AuthenticationPrincipal User user,
            @PathVariable("reviewId") Long reviewId
    ) {
        return reviewService.getReviewContentForEdit(user.getUsername(), reviewId);
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<ResponseDto> editReview(
            @AuthenticationPrincipal User user,
            @PathVariable("reviewId") Long reviewId,
            @RequestBody String content
    ) {
        reviewService.editChallengeReview(user.getUsername(), reviewId, content);
        return ResponseDto.of(HttpStatus.OK, "리뷰를 수정했습니다.");
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ResponseDto> deleteReview(
            @AuthenticationPrincipal User user,
            @PathVariable("reviewId") Long reviewId
    ) {
        reviewService.deleteChallengeReview(user.getUsername(), reviewId);
        return ResponseDto.of(HttpStatus.OK, "리뷰를 삭제했습니다.");
    }
}

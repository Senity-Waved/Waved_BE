package com.senity.waved.domain.review.controller;

import com.senity.waved.domain.member.dto.GithubInfoDto;
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
    public ResponseEntity<String> createReview(
            @AuthenticationPrincipal User user,
            @PathVariable("myChallengeId") Long myChallengeId,
            @RequestBody String content
    ) {
        reviewService.createChallengeReview(user.getUsername(), myChallengeId, content);
        return new ResponseEntity<>("리뷰를 저장했습니다.", HttpStatus.OK);
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<String> editReview(
            @AuthenticationPrincipal User user,
            @PathVariable("reviewId") Long reviewId,
            @RequestBody String content
    ) {
        reviewService.editChallengeReview(user.getUsername(), reviewId, content);
        return new ResponseEntity<>("리뷰를 수정했습니다.", HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @AuthenticationPrincipal User user,
            @PathVariable("reviewId") Long reviewId
    ) {
        reviewService.deleteChallengeReview(user.getUsername(), reviewId);
        return new ResponseEntity<>("리뷰를 삭제했습니다.", HttpStatus.OK);
    }
}

package com.senity.waved.domain.review.service;

import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.challengeGroup.repository.ChallengeGroupRepository;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.member.repository.MemberRepository;
import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import com.senity.waved.domain.myChallenge.exception.MemberNotFoundException;
import com.senity.waved.domain.myChallenge.repository.MyChallengeRepository;
import com.senity.waved.domain.review.entity.Review;
import com.senity.waved.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ChallengeGroupRepository challengeGroupRepository;
    private final MyChallengeRepository myChallengeRepository;

    @Transactional
    public void createChallengeReview(String email, Long myChallengeId, String content) {
        Member member = getMemberByEmail(email);
        MyChallenge myChallenge = myChallengeRepository.findById(myChallengeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 마이 챌린지를 찾을 수 없습니다."));

        ChallengeGroup challengeGroup = challengeGroupRepository.findById(myChallenge.getChallengeGroupId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 챌린지 기수를 찾을 수 없습니다."));

        if (challengeGroup.getEndDate().compareTo(getToday()) >= 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "종료된 마이 챌린지만 리뷰 작성이 가능합니다.");
        }

        checkReviewExist(challengeGroup.getId(), member.getId());
        Review newReview = Review.builder()
                .content(content)
                .memberId(member.getId())
                .challengeGroupId(challengeGroup.getId())
                .build();

        reviewRepository.save(newReview);
    }

    @Transactional
    public void editChallengeReview(String email, Long reviewId, String content) {
        Review review = getReviewAndCheckPermission(email, reviewId, "리뷰 작성자만 수정 가능합니다.");
        review.updateContent(content);
        reviewRepository.save(review);
    }

    @Transactional
    public void deleteChallengeReview(String email, Long reviewId) {
        Review review = getReviewAndCheckPermission(email, reviewId, "리뷰 작성자만 삭제 가능합니다.");
        reviewRepository.delete(review);

    }

    private Review getReviewAndCheckPermission(String email, Long reviewId, String errMsg) {
        Member member = getMemberByEmail(email);
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 리뷰를 찾을 수 없습니다."));

        if (!review.getMemberId().equals(member.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, errMsg);
        }
        return review;
    }

    private void checkReviewExist(Long challengeGroupId, Long memberId) {
        Optional<Review> reviews = reviewRepository.findByChallengeGroupIdAndMemberId(challengeGroupId, memberId);
        if (reviews.isPresent()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 챌린지에 이미 리뷰를 남기셨습니다.");
        }
    }

    private Date getToday() {
        LocalDate today = LocalDate.now();
        return Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));
    }
}

package com.senity.waved.domain.review.service;

import com.senity.waved.domain.challenge.entity.Challenge;
import com.senity.waved.domain.challenge.exception.ChallengeNotFoundException;
import com.senity.waved.domain.challenge.repository.ChallengeRepository;
import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.challengeGroup.exception.ChallengeGroupNotCompletedException;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.member.exception.MemberNotFoundException;
import com.senity.waved.domain.member.repository.MemberRepository;
import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import com.senity.waved.domain.myChallenge.exception.MyChallengeNotFoundException;
import com.senity.waved.domain.myChallenge.repository.MyChallengeRepository;
import com.senity.waved.domain.review.entity.Review;
import com.senity.waved.domain.review.exception.ReviewNotFoundException;
import com.senity.waved.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final MyChallengeRepository myChallengeRepository;
    private final ChallengeRepository challengeRepository;

    @Transactional
    public void createChallengeReview(String email, Long myChallengeId, String content) {
        Member member = getMemberByEmail(email);
        MyChallenge myChallenge = getMyChallengeById(myChallengeId);

        ChallengeGroup challengeGroup = myChallenge.getChallengeGroup();
        Challenge challenge = getChallengeById(challengeGroup.getChallengeId());

        if (challengeGroup.getEndDate().isAfter(ZonedDateTime.now())) {
            throw new ChallengeGroupNotCompletedException("종료된 챌린지 그룹에 대해서만 리뷰 작성 가능합니다.");
        }

        checkReviewExist(myChallengeId);
        Review newReview = Review.builder()
                .content(content)
                .memberId(member.getId())
                .challengeId(challenge.getId())
                .groupTitle(challengeGroup.getGroupTitle())
                .build();

        myChallenge.updateIsReviewed();
        reviewRepository.save(newReview);
    }

    @Transactional(readOnly = true)
    public String getReviewContentForEdit(String email, Long reviewId) {
        Review review = getReviewAndCheckPermission(email, reviewId, "리뷰 작성자만 수정 가능합니다.");
        return review.getContent();
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
                .orElseThrow(() -> new ReviewNotFoundException("해당 리뷰를 찾을 수 없습니다."));

        if (!review.getMemberId().equals(member.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, errMsg);
        }

        return review;
    }

    private void checkReviewExist(Long myChallengeId) {
        MyChallenge myChallenge = getMyChallengeById(myChallengeId);
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));
    }

    private Challenge getChallengeById(Long id) {
        return challengeRepository.findById(id)
                .orElseThrow(() -> new ChallengeNotFoundException("해당 챌린지를 찾을 수 없습니다."));
    }

    private MyChallenge getMyChallengeById(Long id) {
        return myChallengeRepository.findById(id)
                .orElseThrow(() -> new MyChallengeNotFoundException("해당 마이챌린지를 찾을 수 없습니다."));
    }
}

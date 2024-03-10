package com.senity.waved.domain.challenge.service;

import com.senity.waved.domain.challenge.entity.Challenge;
import com.senity.waved.domain.challenge.repository.ChallengeRepository;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.member.repository.MemberRepository;
import com.senity.waved.domain.review.dto.response.ReviewResponseDto;
import com.senity.waved.domain.review.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeRepository challengeRepository;

    @Transactional
    public Page<ReviewResponseDto> getReviewsPaged(Long challengeId, int pageNumber, int pageSize) {
        Challenge challenge = getChallengeById(challengeId);
        List<Review> reviews = challenge.getReviews();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<ReviewResponseDto> responseDtoList = getPaginatedReviewResponseDtoList(reviews, pageable);

        return new PageImpl<>(responseDtoList, pageable, reviews.size());
    }

    private List<ReviewResponseDto> getPaginatedReviewResponseDtoList(List<Review> reviews, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), reviews.size());

        return reviews.subList(start, end)
                .stream()
                .map(Review::getChallengeReviewResponse)
                .collect(Collectors.toList());
    }

    private Challenge getChallengeById(Long id) {
        return challengeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, "해당 챌린지를 찾을 수 없습니다."));
    }
}

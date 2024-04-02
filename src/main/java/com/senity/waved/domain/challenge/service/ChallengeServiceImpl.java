package com.senity.waved.domain.challenge.service;

import com.senity.waved.domain.challenge.entity.Challenge;
import com.senity.waved.domain.challenge.exception.ChallengeNotFoundException;
import com.senity.waved.domain.challenge.repository.ChallengeRepository;
import com.senity.waved.domain.challengeGroup.dto.response.ChallengeGroupHomeResponseDto;
import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.challengeGroup.repository.ChallengeGroupRepository;
import com.senity.waved.domain.member.entity.AuthLevel;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.member.repository.MemberRepository;
import com.senity.waved.domain.myChallenge.exception.MyChallengeNotFoundException;
import com.senity.waved.domain.review.dto.response.ChallengeReviewResponseDto;
import com.senity.waved.domain.review.entity.Review;
import com.senity.waved.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeGroupRepository challengeGroupRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ChallengeGroupHomeResponseDto> getHomeChallengeGroupsListed() {
        List<ChallengeGroupHomeResponseDto> homeGroups = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            Challenge challenge = getChallengeById(i * 1L);
            long cnt = challengeGroupRepository.count()/4;

            ChallengeGroup group = challengeGroupRepository.findById((cnt-1) * 4L + i)
                    .orElseThrow(() -> new MyChallengeNotFoundException(""));
            homeGroups.add(ChallengeGroupHomeResponseDto.of(group, challenge));
        }
        return homeGroups;
    }

    @Override
    @Transactional
    public Page<ChallengeReviewResponseDto> getReviewsPaged(Long challengeId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createDate").descending());
        Page<Review> reviewPaged = reviewRepository.findByChallengeId(challengeId, pageable);

        List<ChallengeReviewResponseDto> responseDtoList = getReviewListed(reviewPaged);
        return new PageImpl<>(responseDtoList, pageable, reviewPaged.getTotalElements());
    }

    private List<ChallengeReviewResponseDto> getReviewListed(Page<Review> reviewPaged) {
        return reviewPaged.getContent()
                .stream()
                .map(review -> {
                    Member member = getMemberById(review.getMemberId());
                    return ChallengeReviewResponseDto.of(review, member);
                })
                .collect(Collectors.toList());
    }

    private Challenge getChallengeById(Long id) {
        return challengeRepository.findById(id)
                .orElseThrow(() -> new ChallengeNotFoundException("해당 챌린지를 찾을 수 없습니다."));
    }

    private Member getMemberById(Long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);

        if (optionalMember.isEmpty()) {
            return Member.deletedMember();
        } return optionalMember.get();
    }

    /* 챌린지 그룹 자동 생성 메서드: 테스트 시 사용 X

    @Transactional
    @Scheduled(fixedDelay = 10000) // 10초 단위
    public void makeChallengeGroupScheduled() {
        for (int i = 1; i <= 4; i++) {
            Challenge challenge = getChallengeById(i * 1L);

            int newGroupIndex = challenge.getGroups().size() + 1;
            String newGroupTitle = challenge.getTitle() + " " + newGroupIndex + "기";

            LocalDate newStartDate = challenge.getGroups().get(newGroupIndex - 2).getEndDate().plusDays(1);
            LocalDate newEndDate = newStartDate.plusDays(13);

            ChallengeGroup newGroup = ChallengeGroup.builder()
                    .groupIndex(Long.valueOf(newGroupIndex))
                    .groupTitle(newGroupTitle)
                    .startDate(newStartDate)
                    .endDate(newEndDate)
                    .challenge(challenge)
                    .participantCount(0L)
                    .build();

            groupRepository.save(newGroup);
        }
    }
    */
}

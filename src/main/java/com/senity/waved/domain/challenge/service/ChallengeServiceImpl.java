package com.senity.waved.domain.challenge.service;

import com.senity.waved.domain.challenge.entity.Challenge;
import com.senity.waved.domain.challenge.exception.ChallengeNotFoundException;
import com.senity.waved.domain.challenge.repository.ChallengeRepository;
import com.senity.waved.domain.challengeGroup.dto.response.ChallengeGroupHomeResponseDto;
import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.challengeGroup.repository.ChallengeGroupRepository;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.member.repository.MemberRepository;
import com.senity.waved.domain.myChallenge.exception.MyChallengeNotFoundException;
import com.senity.waved.domain.review.dto.response.ChallengeReviewResponseDto;
import com.senity.waved.domain.review.entity.Review;
import com.senity.waved.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@EnableScheduling
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
            long cnt = challengeGroupRepository.count() / 4;

            ChallengeGroup group = challengeGroupRepository.findById((cnt - 1) * 4L + i)
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
        }
        return optionalMember.get();
    }

    // 챌린지 그룹 자동 생성 메서드: 테스트 시 사용 X
    @Transactional
    // @Scheduled(fixedDelay = 100000) // 10초 단위 (테스트용)
    @Scheduled(cron = "0 0 0 * * MON") // 매주 월요일 0시 메서드 호출
    public void makeChallengeGroupScheduled() {
        List<Challenge> challengeList = challengeRepository.findAll();

        for (Challenge challenge : challengeList) {
            Long latestGroupIndex = challenge.getLatestGroupIndex();
            ChallengeGroup latestGroup = getGroupByChallengeIdAndGroupIndex(challenge.getId(), latestGroupIndex);

            if (latestGroup.getStartDate().equals(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).truncatedTo(ChronoUnit.DAYS))) {
                ChallengeGroup newGroup = ChallengeGroup.from(latestGroup, challenge);
                challengeGroupRepository.save(newGroup);
                challenge.updateLatestGroupIndex();
                // TODO : 이전 챌린지 종료 & 환급 알림
                // TODO : 챌린지 시작 알림
                // push 알림까지 구현할거면 새벽 12시보다 아침 8시로 변경?
            }
        }
    }

    private ChallengeGroup getGroupByChallengeIdAndGroupIndex(Long challengeId, Long groupIndex) {
        List<ChallengeGroup> group = challengeGroupRepository.findByChallengeIdAndGroupIndex(challengeId, groupIndex);
        if (group.isEmpty()) {
            log.error("challenge id {}의 마지막 기수 그룹을 찾을 수 없습니다.", challengeId);
            return null;
        } return group.get(0);
    }
}

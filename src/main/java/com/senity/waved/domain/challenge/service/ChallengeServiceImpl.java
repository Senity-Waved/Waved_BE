package com.senity.waved.domain.challenge.service;

import com.senity.waved.domain.challenge.entity.Challenge;
import com.senity.waved.domain.challenge.exception.ChallengeNotFoundException;
import com.senity.waved.domain.challenge.repository.ChallengeRepository;
import com.senity.waved.domain.challengeGroup.dto.response.ChallengeGroupHomeResponseDto;
import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.challengeGroup.repository.ChallengeGroupRepository;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.member.repository.MemberRepository;
import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import com.senity.waved.domain.myChallenge.repository.MyChallengeRepository;
import com.senity.waved.domain.notification.entity.Notification;
import com.senity.waved.domain.notification.repository.NotificationRepository;
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
    private final MyChallengeRepository myChallengeRepository;
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ChallengeGroupHomeResponseDto> getHomeChallengeGroupsListed() {
        List<ChallengeGroupHomeResponseDto> homeGroups = new ArrayList<>();
        int cnt = Math.toIntExact(challengeRepository.count());

        for (int i = 1; i <= cnt; i++) {
            Challenge challenge = getChallengeById(i * 1L);
            List<ChallengeGroup> group = challengeGroupRepository.findByChallengeIdAndGroupIndex(challenge.getId(), challenge.getLatestGroupIndex());
            homeGroups.add(ChallengeGroupHomeResponseDto.of(group.get(0), challenge));
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

    @Transactional
    // @Scheduled(fixedDelay = 10000)
    // @Scheduled(cron = "0 0 20 * * SUN") // 매주 월요일 5시, 배포 서버: 일요일 20시
    @Scheduled(cron = "0 48 20 * * SUN")
    public void makeChallengeGroupAndDoNotificationScheduled() {
        List<Challenge> challengeList = challengeRepository.findAll();

        for (Challenge challenge : challengeList) {
            Long latestGroupIndex = challenge.getLatestGroupIndex();
            ChallengeGroup latestGroup = getGroupByChallengeIdAndGroupIndex(challenge.getId(), latestGroupIndex);
            log.error("-------------------------------- startdate " + latestGroup.getStartDate());
            log.error("-------------------------------- now " + ZonedDateTime.now(ZoneId.of("GMT")).truncatedTo(ChronoUnit.DAYS));

            if (latestGroup.getStartDate().equals(ZonedDateTime.now(ZoneId.of("GMT")).truncatedTo(ChronoUnit.DAYS))) {

                Long lastGroupIndex = latestGroupIndex - 1;
                ChallengeGroup lastGroup = getGroupByChallengeIdAndGroupIndex(challenge.getId(), lastGroupIndex);

                String endMessage = String.format("%s %d기가 \r\n종료되었습니다. 진행 완료 챌린지 내역에서 \r\n성공 여부를 확인하고 환급 신청해주세요.", challenge.getTitle(), lastGroupIndex);
                notifyMembersAppliedGroup(lastGroup.getId(), "챌린지 종료", endMessage);

                String startMessage = String.format("%s %d기가 \r\n오늘부터 시작됩니다.", challenge.getTitle(), latestGroupIndex);
                notifyMembersAppliedGroup(latestGroup.getId(), "챌린지 시작", startMessage);

                ChallengeGroup newGroup = ChallengeGroup.from(latestGroup, challenge);
                challengeGroupRepository.save(newGroup);
                challenge.updateLatestGroupIndex();
            }
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 18 * * SUN")
    public void deleteOldNotifications() {
        ZonedDateTime deleteBefore = ZonedDateTime.now().toLocalDate().minusDays(14).atStartOfDay(ZoneId.systemDefault());
        notificationRepository.deleteNotificationsByCreateDate(deleteBefore);
    }

    private void notifyMembersAppliedGroup(Long groupId, String title, String message) {
        List<MyChallenge> myChallengeList = myChallengeRepository.findByChallengeGroupIdAndIsPaidTrue(groupId);

        for(MyChallenge myChallenge: myChallengeList) {
            Long memberId = myChallenge.getMemberId();
            Member member = getMemberByIdWithNull(myChallenge.getMemberId());

            if (member != null) {
                Notification newNotification = Notification.of(memberId, title, message);
                notificationRepository.save(newNotification);
                member.updateNewEvent(true);
                memberRepository.flush();
            }
        }
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

    private Member getMemberByIdWithNull(Long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);

        if (optionalMember.isEmpty()) {
            return null;
        }
        return optionalMember.get();
    }

    private ChallengeGroup getGroupByChallengeIdAndGroupIndex(Long challengeId, Long groupIndex) {
        List<ChallengeGroup> group = challengeGroupRepository.findByChallengeIdAndGroupIndex(challengeId, groupIndex);
        if (group.isEmpty()) {
            log.error("challenge id {}의 {}기 그룹을 찾을 수 없습니다.", challengeId, groupIndex);
            return null;
        } return group.get(0);
    }
}

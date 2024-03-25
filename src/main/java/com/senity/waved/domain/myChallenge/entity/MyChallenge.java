package com.senity.waved.domain.myChallenge.entity;

import com.senity.waved.common.BaseEntity;
import com.senity.waved.domain.challenge.entity.Challenge;
import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.myChallenge.dto.response.MyChallengeResponseDto;
import com.senity.waved.domain.verification.exception.AlreadyVerifiedException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MyChallenge extends BaseEntity {

    @Column(name="my_verifs")
    private long myVerifs;

    @Column(name = "success_count")
    private Long successCount;

    @Column(name = "is_reviewed")
    private Boolean isReviewed;

    @Column(name = "is_refund_requested")
    private Boolean isRefundRequested;

    @Column(name = "deposit")
    private Long deposit;

    @Column(name = "imp_urd")
    private String impUid;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "member_id")
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_group_id")
    private ChallengeGroup challengeGroup;

    // 성공(2), 실패(1), 제출 안함(0)
    public void updateVerificationStatus(int dayIndex, boolean isSuccess) {
        if (dayIndex < 15 && dayIndex > 0) {
            // 오늘 인증 했는지
            myVerifs += isSuccess ? 2 * Math.pow(10, 14 - dayIndex) : Math.pow(10, 14 - dayIndex);
        }
    }

    public void updateIsReviewed() {
        isReviewed = true;
    }

    public void updateImpUid(String impUid) {
        this.impUid = impUid;
    }

    public boolean isValidChallengePeriod(ZonedDateTime startDate, ZonedDateTime currentDate) {
        long daysFromStart = ChronoUnit.DAYS.between(startDate, currentDate);
        return daysFromStart >= 0 && daysFromStart < 14;
    }

    public void incrementSuccessCount() {
        if (this.successCount == null) {
            this.successCount = 1L;
        } else {
            this.successCount += 1;
        }
    }

    public boolean isVerified() {
        ZonedDateTime currentDate = ZonedDateTime.now();
        ZonedDateTime startDate = getStartDate();
        long daysFromStart = ChronoUnit.DAYS.between(startDate, currentDate); //startDate부터 오늘 날짜 차이 계산

        if (daysFromStart > -1 && daysFromStart < 14) {
            return (int) (myVerifs / Math.pow(10, 13 - daysFromStart) % 10) != 0;
        }
        return false;
    }

    public void verify() {
        if (this.isVerified()) {
            throw new AlreadyVerifiedException("이미 오늘의 인증을 완료했습니다.");
        }
    }

    public static MyChallengeResponseDto getMyChallengesInProgress(MyChallenge myChallenge, ChallengeGroup group, Challenge challenge, Boolean isGithubConnected) {
        return MyChallengeResponseDto.builder()
                .groupTitle(group.getGroupTitle())
                .startDate(group.getStartDate())
                .endDate(group.getEndDate())
                .successCount(myChallenge.getSuccessCount())
                .deposit(myChallenge.getDeposit())
                .myChallengeId(myChallenge.getId())
                .challengeGroupId(group.getId())
                .isVerified(myChallenge.isVerified())
                .isGithubConnected(isGithubConnected)
                .verificationType(challenge.getVerificationType())
                .build();
    }

    public static MyChallengeResponseDto getMyChallengesWaiting(MyChallenge myChallenge, ChallengeGroup group) {
        return MyChallengeResponseDto.builder()
                .groupTitle(group.getGroupTitle())
                .startDate(group.getStartDate())
                .deposit(myChallenge.getDeposit())
                .endDate(group.getEndDate())
                .challengeGroupId(group.getId())
                .build();
    }

    public static MyChallengeResponseDto getMyChallengesCompleted(MyChallenge myChallenge, ChallengeGroup group, Challenge challenge) {
        Boolean isSuccessed = myChallenge.getSuccessCount() > 10 ? true : false;
        return MyChallengeResponseDto.builder()
                .groupTitle(group.getGroupTitle())
                .startDate(group.getStartDate())
                .endDate(group.getEndDate())
                .challengeGroupId(group.getId())
                .myChallengeId(myChallenge.getId())
                .isReviewed(myChallenge.getIsReviewed())
                .isRefundRequested(myChallenge.getIsRefundRequested())
                .deposit(myChallenge.getDeposit())
                .isSuccessed(isSuccessed)
                .verificationType(challenge.getVerificationType())
                .build();
    }
}

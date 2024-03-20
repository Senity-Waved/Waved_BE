package com.senity.waved.domain.myChallenge.entity;

import com.senity.waved.common.BaseEntity;
import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.myChallenge.dto.response.MyChallengeResponseDto;
import com.senity.waved.domain.verification.exception.AlreadyVerifiedException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MyChallenge extends BaseEntity {

    @ElementCollection
    @Column(name="my_verifs")
    private int[] myVerifs;

    @Column(name = "success_count")
    private Long successCount;
  
    @Column(name = "is_reviewed")
    private Boolean isReviewed;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(name = "deposit")
    private Long deposit;

    @Column(name = "imp_urd")
    private Long impUid;

    @ColumnDefault("FALSE")
    @Column(nullable = false)
    private Boolean isDeleted; // true -> 삭제

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_group_id")
    private ChallengeGroup challengeGroup;

    // 성공(2), 실패(1), 제출 안함(0)
    public void updateVerificationStatus(int dayIndex, boolean isSuccess) {
        if (this.myVerifs != null && dayIndex >= 0 && dayIndex < this.myVerifs.length) {
            this.myVerifs[dayIndex] = isSuccess ? 2 : 1;
        }
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

    public void setSuccessCount(Long successCount) {
        this.successCount = successCount;
    }

    public void markAsDeleted(Boolean b) {
        this.isDeleted = b;
    }

    public void setMyVerifs(int[] myVerifs) {
        this.myVerifs = myVerifs;
        Arrays.fill(this.myVerifs, 0);
    }

    public boolean isVerified() {
        ZonedDateTime currentDate = ZonedDateTime.now();
        ZonedDateTime startDate = this.challengeGroup.getStartDate();
        long daysFromStart = ChronoUnit.DAYS.between(startDate, currentDate); //startDate부터 오늘 날짜 차이 계산

        if (this.myVerifs.length == 0) {
            return false; // myVerifs 배열이 빈 배열이면 false
        }

        if (daysFromStart >= 0 && daysFromStart < this.myVerifs.length) {
            return this.myVerifs[(int)daysFromStart] != 0; // 0이 아니면 true
        }

        return false;
    }

    public void verify() {
        if (this.isVerified()) {
            throw new AlreadyVerifiedException("이미 오늘의 인증을 완료했습니다.");
        }
    }

    public static MyChallengeResponseDto getMyChallengesInProgress(MyChallenge myChallenge, Boolean isVerified, Boolean isGithubConnected) {
        ChallengeGroup group = myChallenge.getChallengeGroup();
        return MyChallengeResponseDto.builder()
                .groupTitle(group.getGroupTitle())
                .startDate(group.getStartDate())
                .endDate(group.getEndDate())
                .successCount(myChallenge.getSuccessCount())
                .myChallengeId(myChallenge.getId())
                .challengeGroupId(group.getId())
                .isVerified(isVerified)
                .isGithubConnected(isGithubConnected)
                .build();
    }

    public static MyChallengeResponseDto getMyChallengesWaiting(MyChallenge myChallenge) {
        ChallengeGroup group = myChallenge.getChallengeGroup();
        return MyChallengeResponseDto.builder()
                .groupTitle(group.getGroupTitle())
                .startDate(group.getStartDate())
                .endDate(group.getEndDate())
                .challengeGroupId(group.getId())
                .build();
    }

    public static MyChallengeResponseDto getMyChallengesCompleted(MyChallenge myChallenge) {
        ChallengeGroup group = myChallenge.getChallengeGroup();
        return MyChallengeResponseDto.builder()
                .groupTitle(group.getGroupTitle())
                .startDate(group.getStartDate())
                .endDate(group.getEndDate())
                .challengeGroupId(group.getId())
                .myChallengeId(myChallenge.getId())
                .isReviewed(myChallenge.getIsReviewed())
                .build();
    }
}

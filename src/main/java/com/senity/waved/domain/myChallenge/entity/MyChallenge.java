package com.senity.waved.domain.myChallenge.entity;

import com.senity.waved.common.BaseEntity;
import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.verification.exception.AlreadyVerifiedException;
import com.senity.waved.domain.verification.exception.FailedVerificationException;
import com.senity.waved.domain.verification.exception.VerifyNotFoundOnDateException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

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

    @ColumnDefault("FALSE")
    @Column(name = "is_paid")
    private boolean isPaid;

    @Column(name = "imp_uid")
    private String impUid;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "challenge_group_id")
    private Long challengeGroupId;

    // 성공(2), 실패(1), 제출 안함(0)
    public void updateVerificationStatus(int dayIndex, boolean isSuccess) {
        if (dayIndex < 15 && dayIndex > 0) {
            myVerifs += isSuccess ? 2 * Math.pow(10, 14 - dayIndex) : Math.pow(10, 14 - dayIndex);
        }
    }

    public void updateIsReviewed() {
        isReviewed = true;
    }

    public void updateIsPaid(boolean b) {
        this.isPaid = b;
    }

    public void updateImpUid(String impUid) {
        this.impUid = impUid;
    }

    public void updateIsRefundRequested() { isRefundRequested = true; }

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
        long daysFromStart = ChronoUnit.DAYS.between(startDate, currentDate);

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

    public void deleteVerification(ZonedDateTime createDate) {
        long daysFromStart = ChronoUnit.DAYS.between(startDate, createDate);

        if (daysFromStart > -1 && daysFromStart < 14) {
            if ((int)(myVerifs / Math.pow(10, 13 - daysFromStart) % 10) == 2) {
                myVerifs -= Math.pow(10, 13 - daysFromStart);
                successCount--;
            } else {
                if ((int)(myVerifs / Math.pow(10, 13 - daysFromStart) % 10) == 1) {
                    throw new FailedVerificationException("실패한 인증 내역은 취소할 수 없습니다.");
                }
                else throw new VerifyNotFoundOnDateException("해당 날짜에 인증내역이 존재하지 않습니다.");
            }
        }
    }

    public static MyChallenge of(Member member, ChallengeGroup group, Long deposit) {
        Boolean isFree = deposit == 0;
        return MyChallenge.builder()
                .challengeGroupId(group.getId())
                .successCount(0L)
                .isReviewed(false)
                .memberId(member.getId())
                .myVerifs(300000000000000L)
                .deposit(deposit)
                .isRefundRequested(false)
                .startDate(group.getStartDate())
                .endDate(group.getEndDate())
                .isPaid(isFree)
                .build();
    }
}

package com.senity.waved.domain.myChallenge.entity;

import com.senity.waved.common.BaseEntity;
import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_group_id")
    private ChallengeGroup challengeGroup;

    public void updateVerificationStatus(int dayIndex, boolean isSuccess) {
        if (this.myVerifs != null && dayIndex >= 0 && dayIndex < this.myVerifs.length) {
            this.myVerifs[dayIndex] = isSuccess ? 1 : 0;
        }
    }

    public boolean isValidChallengePeriod(LocalDate startDate, LocalDate currentDate) {
        long daysFromStart = ChronoUnit.DAYS.between(startDate, currentDate);
        return daysFromStart >= 0 && daysFromStart < 14;
    }

    public void incrementSuccessCount() {
        this.successCount += 1;
    }

    public void setSuccessCount(Long successCount) {
        this.successCount = successCount;
    }

    public void setMyVerifs(int[] myVerifs) {
        this.myVerifs = myVerifs;
    }

}

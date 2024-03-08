package com.senity.waved.domain.myChallenge.entity;

import com.senity.waved.common.BaseEntity;
import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MyChallenge extends BaseEntity {

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name="success_count")
    private Long successCount;

    @Column(name="fail_count")
    private Long failCount;

    @Column(name="total_count")
    private Long totalCount;

    @Column(name="is_today_certification")
    private Boolean isTodayCertification;

    @Column(name="is_reviewed")
    private Boolean isReviewed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_group_id")
    private ChallengeGroup challengeGroup;

}

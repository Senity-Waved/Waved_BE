package com.senity.waved.domain.verification.entity;

import com.senity.waved.common.BaseEntity;
import com.senity.waved.domain.challenge.entity.VerificationType;

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
public class Verification extends BaseEntity {

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_type")
    private VerificationType verificationType;

    @Column(name = "challenge_group_id")
    private Long challengeGroupId;

    @Column(name = "challenge_id")
    private Long memberId;

    public static Verification createGithubVerification(Member member, ChallengeGroup challengeGroup, boolean hasCommitsToday) {
        return Verification.builder()
                .content(String.valueOf(hasCommitsToday))
                .memberId(member.getId())
                .challengeGroupId(challengeGroup.getId())
                .verificationType(VerificationType.GITHUB)
                .build();
    }
}

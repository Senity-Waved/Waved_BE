package com.senity.waved.domain.challengeGroup.dto.response;

import com.senity.waved.domain.challenge.entity.Challenge;
import com.senity.waved.domain.challenge.entity.VerificationType;
import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeGroupResponseDto {
    private String groupTitle;
    private Long participantCount;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private VerificationType verificationType;
    private String description;
    private String verificationDescription;
    private Boolean isApplied;
    private Boolean isFree;

    // 챌린지(전체 그룹) 리뷰 paged 조회를 위한 id값
    private Long challengeId;
    // 챌린지 그룹 조회 시, 신청된 경우 신청 취소에 사용
    private Long myChallengeId;

    public static ChallengeGroupResponseDto of(ChallengeGroup group, Challenge challenge, Long myChallengeId) {
        Boolean isApplied = myChallengeId > 0;
        return ChallengeGroupResponseDto.builder()
                .groupTitle(group.getGroupTitle())
                .participantCount(group.getParticipantCount())
                .startDate(group.getStartDate().plusHours(9))
                .endDate(group.getEndDate().plusHours(9))
                .verificationType(challenge.getVerificationType())
                .description(challenge.getDescription())
                .verificationDescription(challenge.getVerificationDescription())
                .isApplied(isApplied)
                .isFree(challenge.getIsFree())
                .myChallengeId(myChallengeId)
                .challengeId(challenge.getId())
                .build();
    }
}

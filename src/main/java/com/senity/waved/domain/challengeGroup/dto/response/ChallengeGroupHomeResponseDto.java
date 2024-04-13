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
public class ChallengeGroupHomeResponseDto {

    private String groupTitle;
    private VerificationType verificationType;
    private Boolean isFree;
    private Long participantCount;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private Long challengeGroupId;  // group 상세페이지 호출을 위한 id값
    private String imageUrl;

    public static ChallengeGroupHomeResponseDto of(ChallengeGroup group, Challenge challenge) {
        return ChallengeGroupHomeResponseDto.builder()
                .groupTitle(group.getGroupTitle())
                .verificationType(challenge.getVerificationType())
                .isFree(challenge.getIsFree())
                .participantCount(group.getParticipantCount())
                .startDate(group.getStartDate().plusHours(9))
                .endDate(group.getEndDate().plusHours(9))
                .challengeGroupId(group.getId())
                .imageUrl(challenge.getImageUrl())
                .build();
    }
}

package com.senity.waved.domain.challengeGroup.dto.response;

import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminChallengeGroupResponseDto {
    private Long challengeGroupId;
    private String groupTitle;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;

    public static AdminChallengeGroupResponseDto fromChallengeGroup(ChallengeGroup challengeGroup) {
        return AdminChallengeGroupResponseDto.builder()
                .challengeGroupId(challengeGroup.getId())
                .groupTitle(challengeGroup.getGroupTitle())
                .startDate(challengeGroup.getStartDate())
                .endDate(challengeGroup.getEndDate())
                .build();
    }
}

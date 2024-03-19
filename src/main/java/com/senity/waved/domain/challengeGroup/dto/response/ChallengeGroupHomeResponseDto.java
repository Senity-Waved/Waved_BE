package com.senity.waved.domain.challengeGroup.dto.response;

import com.senity.waved.domain.challenge.entity.VerificationType;
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

    // group 상세페이지 호출을 위한 id값
    private Long challengeGroupId;
}

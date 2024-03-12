package com.senity.waved.domain.challengeGroup.dto.response;

import com.senity.waved.domain.challenge.entity.VerificationType;
import com.senity.waved.domain.myChallenge.entity.ChallengeStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeGroupResponseDto {
    private String groupTitle;
    private Long participantCount;
    private LocalDate startDate;
    private LocalDate endDate;
    private VerificationType verificationType;
    private String description;
    private String verificationDescription;
    private Boolean isApplied;

    // 챌린지(전체 그룹) 리뷰 paged 조회를 위한 id값
    private Long challengeId;
}

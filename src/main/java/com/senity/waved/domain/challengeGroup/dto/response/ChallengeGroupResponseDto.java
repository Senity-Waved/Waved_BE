package com.senity.waved.domain.challengeGroup.dto.response;

import com.senity.waved.domain.challenge.entity.VerificationType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeGroupResponseDto {
    private String groupTitle;
    private Long participantCount;
    private Date startDate;
    private Date endDate;
    private VerificationType verificationType;
    private String description;
    private String verificationDescription;

    private Long challengeId;
    // response 보낸 이후 해당 ID를 이용해 챌린지 리뷰 paged 조회
}

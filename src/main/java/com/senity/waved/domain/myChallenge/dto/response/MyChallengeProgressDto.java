package com.senity.waved.domain.myChallenge.dto.response;

import com.senity.waved.domain.challenge.entity.Challenge;
import com.senity.waved.domain.challenge.entity.VerificationType;
import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyChallengeProgressDto extends MyChallengeResponseDto {
    private Long successCount;
    private Boolean isVerified;
    private Boolean isGithubConnected;
    private VerificationType verificationType;
    private Long myChallengeId;

    public static MyChallengeProgressDto of(MyChallenge myChallenge, ChallengeGroup group, Challenge challenge, Boolean isGithubConnected) {
        return MyChallengeProgressDto.builder()
                .groupTitle(group.getGroupTitle())
                .startDate(group.getStartDate())
                .endDate(group.getEndDate())
                .successCount(myChallenge.getSuccessCount())
                .deposit(myChallenge.getDeposit())
                .myChallengeId(myChallenge.getId())
                .challengeGroupId(group.getId())
                .isVerified(myChallenge.isVerified())
                .isGithubConnected(isGithubConnected)
                .verificationType(challenge.getVerificationType())
                .build();
    }
}

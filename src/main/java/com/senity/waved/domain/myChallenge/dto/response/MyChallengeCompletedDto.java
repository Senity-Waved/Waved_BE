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
public class MyChallengeCompletedDto extends MyChallengeResponseDto {
    private Boolean isReviewed;
    private Boolean isRefundRequested;
    private Boolean isSuccessed;
    private VerificationType verificationType;
    private Long myChallengeId;

    public static MyChallengeCompletedDto getMyChallengesCompleted(MyChallenge myChallenge, ChallengeGroup group, Challenge challenge) {
        Boolean isSuccessed = myChallenge.getSuccessCount() > 10 ? true : false;
        return MyChallengeCompletedDto.builder()
                .groupTitle(group.getGroupTitle())
                .startDate(group.getStartDate().plusHours(9))
                .endDate(group.getEndDate().plusHours(9))
                .deposit(myChallenge.getDeposit())
                .challengeGroupId(group.getId())
                .myChallengeId(myChallenge.getId())
                .isReviewed(myChallenge.getIsReviewed())
                .isRefundRequested(myChallenge.getIsRefundRequested())
                .isSuccessed(isSuccessed)
                .verificationType(challenge.getVerificationType())
                .build();
    }
}

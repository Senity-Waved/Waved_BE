package com.senity.waved.domain.myChallenge.dto.response;

import com.senity.waved.domain.challenge.entity.Challenge;
import com.senity.waved.domain.challenge.entity.VerificationType;
import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.temporal.ChronoUnit;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyChallengeCompletedDto extends MyChallengeResponseDto {
    private Boolean isReviewed;
    private Boolean isRefundRequested;
    private Boolean isSuccessed;
    private VerificationType verificationType;
    private Long myChallengeId;

    public static MyChallengeCompletedDto of(MyChallenge myChallenge, ChallengeGroup group, Challenge challenge) {
        long days = ChronoUnit.DAYS.between(group.getStartDate(), group.getEndDate());
        int successCount = days > 10 ? 10 : 5;
        Boolean isSuccessed = myChallenge.getSuccessCount() < successCount ? false : true;
        return MyChallengeCompletedDto.builder()
                .groupTitle(group.getGroupTitle())
                .startDate(group.getStartDate())
                .endDate(group.getEndDate())
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

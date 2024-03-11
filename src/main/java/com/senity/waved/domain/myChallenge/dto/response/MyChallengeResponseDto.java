package com.senity.waved.domain.myChallenge.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyChallengeResponseDto {
    private String groupTitle;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long successCount;
    private Long groupId;
    private Long myChallengeId;
    private Boolean isReviewed;
}

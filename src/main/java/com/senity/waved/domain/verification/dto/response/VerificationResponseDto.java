package com.senity.waved.domain.verification.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationResponseDto {

    private Long verificationId;
    private ZonedDateTime verificationDate;
    private Long likesCount;
    private String nickname;
    private Long memberId;
    private boolean isLiked;
}
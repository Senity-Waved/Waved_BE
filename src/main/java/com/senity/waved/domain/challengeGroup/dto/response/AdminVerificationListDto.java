package com.senity.waved.domain.challengeGroup.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder
public class AdminVerificationListDto {
    private Long verificationId;
    private String content;
    private String link;
    private String imageUrl;
    private ZonedDateTime verificationDate;
    private String nickname;
    private Boolean isDeleted;
}
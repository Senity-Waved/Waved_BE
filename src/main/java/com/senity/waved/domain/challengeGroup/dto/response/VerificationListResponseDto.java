package com.senity.waved.domain.challengeGroup.dto.response;

import com.senity.waved.domain.verification.entity.Verification;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@Setter
public class VerificationListResponseDto {
    private Long verificationId;
    private String content;
    private ZonedDateTime verificationDate;

    public VerificationListResponseDto(Verification verification) {
        this.verificationId = verification.getId();
        this.content = verification.getContent();

        if (verification.getCreateDate() != null) {
            LocalDateTime localDateTime = verification.getCreateDate().toLocalDateTime();
            this.verificationDate = ZonedDateTime.of(localDateTime, ZoneId.of("Asia/Seoul"));
        }
    }
}

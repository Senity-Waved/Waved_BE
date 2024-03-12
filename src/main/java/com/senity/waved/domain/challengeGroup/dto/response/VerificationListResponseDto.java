package com.senity.waved.domain.challengeGroup.dto.response;

import com.senity.waved.domain.verification.entity.Verification;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZoneId;

@Getter
@Setter
public class VerificationListResponseDto {
    private Long verificationId;
    private String content;
    private LocalDate verificationDate;

    public VerificationListResponseDto(Verification verification) {
        this.verificationId = verification.getId();
        this.content = verification.getContent();

        if (verification.getCreateDate() != null) {
            this.verificationDate = verification.getCreateDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }
    }
}


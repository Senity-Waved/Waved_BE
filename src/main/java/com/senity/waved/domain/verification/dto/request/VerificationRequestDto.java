package com.senity.waved.domain.verification.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationRequestDto {
    private Long challengeGroupId;
    private String content;
}

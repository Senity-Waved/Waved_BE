package com.senity.waved.domain.verification.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationRequestDto {
    private String link;
    private String content;
}

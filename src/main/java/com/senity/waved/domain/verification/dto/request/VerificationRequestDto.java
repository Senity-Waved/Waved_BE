package com.senity.waved.domain.verification.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class VerificationRequestDto {
    private String link;
    private String content;
    private MultipartFile imageUrl;
}

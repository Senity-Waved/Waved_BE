package com.senity.waved.domain.verification.dto.response;

import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.verification.entity.Verification;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@Setter
@SuperBuilder
public class TextVerificationResponseDto extends VerificationResponseDto {

    private String content;

    public static TextVerificationResponseDto of(Verification verification, Member member, boolean isLiked) {
        ZonedDateTime verificationDate = null;
        if (verification.getCreateDate() != null) {
            LocalDateTime localDateTime = verification.getCreateDate().toLocalDateTime();
            verificationDate = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        }

        return TextVerificationResponseDto.builder()
                .verificationId(verification.getId())
                .isLiked(isLiked)
                .likesCount(verification.getLikesCount())
                .nickname(member.getNickname())
                .memberId(member.getId())
                .verificationDate(verificationDate)
                .content(verification.getContent())
                .build();
    }
}



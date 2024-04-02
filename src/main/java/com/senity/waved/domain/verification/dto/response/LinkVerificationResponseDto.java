package com.senity.waved.domain.verification.dto.response;

import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.verification.entity.Verification;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LinkVerificationResponseDto extends VerificationResponseDto {

    private String link;
    private String content;

    public static LinkVerificationResponseDto of(Verification verification, Member member, boolean isLiked) {
        ZonedDateTime verificationDate = null;
        if (verification.getCreateDate() != null) {
            LocalDateTime localDateTime = verification.getCreateDate().toLocalDateTime().plusHours(9);
            verificationDate = ZonedDateTime.of(localDateTime, ZoneId.of("Asia/Seoul"));
        }

        return LinkVerificationResponseDto.builder()
                .verificationId(verification.getId())
                .verificationDate(verificationDate)
                .likesCount(verification.getLikesCount())
                .isLiked(isLiked)
                .nickname(member.getNickname())
                .memberId(member.getId())
                .link(verification.getLink())
                .content(verification.getContent())
                .build();
    }
}

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
public class PictureVerificationResponseDto extends VerificationResponseDto {

    private String imageUrl;

    public static PictureVerificationResponseDto of(Verification verification, Member member, boolean isLiked) {
        ZonedDateTime verificationDate = null;
        if (verification.getCreateDate() != null) {
            LocalDateTime localDateTime = verification.getCreateDate().toLocalDateTime().plusHours(9);
            verificationDate = ZonedDateTime.of(localDateTime, ZoneId.of("Asia/Seoul"));
        }

        return PictureVerificationResponseDto.builder()
                .verificationId(verification.getId())
                .verificationDate(verificationDate)
                .likesCount(verification.getLikesCount())
                .isLiked(isLiked)
                .nickname(member.getNickname())
                .memberId(member.getId())
                .imageUrl(verification.getImageUrl())
                .build();
    }
}

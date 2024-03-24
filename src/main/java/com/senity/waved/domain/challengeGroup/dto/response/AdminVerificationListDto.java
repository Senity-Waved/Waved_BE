package com.senity.waved.domain.challengeGroup.dto.response;

import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.verification.entity.Verification;
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

    public static AdminVerificationListDto getAdminVerifications(Verification verification, Member member) {
        return AdminVerificationListDto.builder()
                .nickname(member.getNickname())
                .content(verification.getContent())
                .imageUrl(verification.getImageUrl())
                .link(verification.getLink())
                .verificationId(verification.getId())
                .verificationDate(verification.getCreateDate())
                .isDeleted(verification.getIsDeleted())
                .build();
    }
}